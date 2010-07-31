function [f_CRP, sideinfo] = pitchSTMSP_to_CRP(f_pitch,parameter,sideinfo)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: pitchSTMSP_to_CRP
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description:
% - Calculates CRP (Chroma DCT-reduced Log Pitch) Features
% (see "Towards Timbre-Invariant Audio Features for Harmony-Based Music" by
%  Meinard Mueller and Sebastian Ewert)
% - It is possible to apply temporal smoothing to CRP features in a CENS
% like fashion by setting parameter.stat_window_length and
% parameter.stat_downsample.
% - If stat_window_length==1 and stat_downsample==1 then the
% complete smoothing code is skipped
% - To keeping group delayes at control the stat_window_length
% should contain an odd value and stat_downsample an even value
%
% Input:
%         f_pitch
%         parameter.coefRange = [55:120];
%         parameter.logParamMult = 1000;   % a value of zero disables the log step
%         parameter.logParamAdd = 1;
%         parameter.stat_window_length = 1;
%         parameter.stat_downsample = 1;
%         parameter.featureRate = 10;
%         parameter.save = 1;
%         parameter.saveDir = '';
%         parameter.saveFilename = '';
%         parameter.vis = 0;
%         sideinfo
%
% Output:
%         f_CRP
%         sideinfo
%         featureRate
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Check parameters
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if nargin<3
    sideinfo=[];
end
if nargin<2
    parameter=[];
end
if nargin<1
    error('Please specify input data f_pitch');
end

if isfield(parameter,'coefRange')==0
    parameter.coefRange = [55:120];
end
if isfield(parameter,'logParamMult')==0
    parameter.logParamMult = 1000;
end
if isfield(parameter,'logParamAdd')==0
    parameter.logParamAdd = 1;
end
if isfield(parameter,'norm_p')==0
    parameter.norm_p = 2;
end
if isfield(parameter,'stat_window_length')==0
    parameter.stat_window_length = 1;
end
if isfield(parameter,'stat_downsample')==0
    parameter.stat_downsample = 1;
end
if isfield(parameter,'featureRate')==0
    parameter.featureRate = 10;
end
if isfield(parameter,'save')==0
    parameter.save = 0;
end
if isfield(parameter,'saveDir')==0
    parameter.saveDir = '';
end
if isfield(parameter,'saveFilename')==0
    parameter.saveFilename = '';
end
if isfield(parameter,'vis')==0
    parameter.vis = 0;
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Main program
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

seg_num = size(f_pitch,1);

if parameter.logParamMult ~= 0
    f_pitch_log = log10(parameter.logParamAdd+f_pitch*parameter.logParamMult);
else
    f_pitch_log = f_pitch;
end

DCT = internal_DCT(size(f_pitch_log,2));
DCTcut = DCT;
DCTcut(setdiff([1:120],parameter.coefRange),:) = 0;
DCT_filter = DCT'*DCTcut;
f_pitch_log_DCT = DCT_filter*f_pitch_log';
f_pitch_log_DCT = f_pitch_log_DCT';

% calculate energy for each chroma band
f_CRP = zeros(seg_num,12);
for p=1:120
    chroma = mod(p,12)+1;
    f_CRP(:,chroma) = f_CRP(:,chroma)+f_pitch_log_DCT(:,p);
end

% normalise the vectors according the norm l^p
p = parameter.norm_p;
unit_vec = ones(1,12);
unit_vec = unit_vec/norm(unit_vec,p);
for k=1:seg_num
    n = norm(f_CRP(k,:),p);
    if n==0
        f_CRP(k,:) = unit_vec;
    else
        f_CRP(k,:) = f_CRP(k,:)/n;
    end
end

featureRate = parameter.featureRate;

% Temporal Smoothing
if (parameter.stat_window_length ~= 1) || (parameter.stat_downsample ~= 1)
    stat_window_length = parameter.stat_window_length;
    stat_downsample = parameter.stat_downsample;
    stat_window = hanning(stat_window_length);
    stat_window = stat_window/sum(stat_window);
    
    % upfirdn filters and downsamples each column of f_stat_help
    f_CRP_stat = zeros(seg_num,12);
    f_CRP_stat = upfirdn(f_CRP,stat_window,1,stat_downsample);
    stat_num = ceil(seg_num/stat_downsample);
    cut = floor((stat_window_length-1)/(2*stat_downsample));
    f_CRP_stat = f_CRP_stat((1+cut:stat_num+cut),:);            %adjust group delay
    
    % last step: renormalize each vector with its l^p norm
    p = parameter.norm_p;
    unit_vec = ones(1,12);
    unit_vec = unit_vec/norm(unit_vec);
    for k=1:stat_num
        n = norm(f_CRP_stat(k,:),p);
        if n==0
            f_CRP_stat(k,:) = unit_vec;
        else
            f_CRP_stat(k,:) = f_CRP_stat(k,:)/n;
        end
    end
    
    f_CRP = f_CRP_stat;
    featureRate = parameter.featureRate / stat_downsample;
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Update sideinfo
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

sideinfo.CRP.version = 1;
sideinfo.CRP.coefRange = parameter.coefRange;
sideinfo.CRP.logParamMult = parameter.logParamMult;
sideinfo.CRP.logParamAdd = parameter.logParamAdd;
sideinfo.CRP.norm_p = parameter.norm_p;
sideinfo.CRP.featureRate = featureRate;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Saving to file
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
if parameter.save
    filename = strcat(parameter.saveFilename,'_CRP_',num2str(parameter.stat_window_length),'_',num2str(parameter.stat_downsample));
    save(strcat(parameter.saveDir,filename),'f_CRP','sideinfo');
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
if parameter.vis    
    chroma_names                 = ['C ';'C#';'D ';'D#';'E ';'F ';'F#';'G ';'G#';'A ';'A#';'B '];
    
    figure;
    set(gcf,'renderer','painters');
    h = imagesc(f_CRP',[-1,1]);
    set(gca,'YTick',[1:12]);
    set(gca,'YTickLabel',chroma_names);
    set(gca,'YDir','normal');
    colormap(hot);
    colorbar;
    title('CRP chromagram');
    xlabel('Time [samples]');
    drawnow;
end

end

function matrix = internal_DCT(l)

matrix = zeros(l,l);

for m = 0:l-1
    for n = 0:l-1
        matrix(m+1,n+1) = sqrt(2/l)*cos((m*(n+0.5)*pi)/l);
    end
end

matrix(1,:) = matrix(1,:)/sqrt(2);

end
