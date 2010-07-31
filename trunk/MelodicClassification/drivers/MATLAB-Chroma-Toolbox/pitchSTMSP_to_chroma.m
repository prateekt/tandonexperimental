function [f_chroma_norm,f_chroma,sideinfo] = pitchSTMSP_to_chroma(f_pitch,parameter,sideinfo);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: pitchSTMSP_to_chroma 
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description:
% Computes normalized chroma vectors from STMSP pitch subband (f_pitch)
%
% Input:  
%         f_pitch
%         parameter.midi_min = 1
%         parameter.midi_max = 120
%         parameter.featureRate = 10
%         parameter.stat_thresh = 0.001
%         parameter.norm_p = 2
%
%         parameter.vis = 0;
%            parameter.vis_Chroma = 1
%            parameter.vis_ChromaNorm = 1
%         parameter.save = 0;
%            parameter.save_dir = '';
%            parameter.save_filename = '';
%
% Output: 
%         f_chroma_norm
%         f_chroma
%         sideinfo
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

if isfield(parameter,'save')==0
   parameter.save = 0;
end

if isfield(parameter,'vis')==0
   parameter.vis = 0;
end

if isfield(parameter,'midi_min')==0
   parameter.midi_min = 1;
end

if isfield(parameter,'midi_max')==0
   parameter.midi_max = 120;
end

if isfield(parameter,'vis_Chroma')==0
   parameter.vis_Chroma = 1;
end

if isfield(parameter,'vis_ChromaNorm')==0
   parameter.vis_ChromaNorm = 1;
end

if isfield(parameter,'stat_thresh')==0
   parameter.stat_thresh = 0.001;
end

if isfield(parameter,'norm_p')==0
   parameter.norm_p = 2;
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Main program
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

seg_num = size(f_pitch,1);

% calculate energy for each chroma band
f_chroma =  zeros(seg_num,12);
for p=parameter.midi_min:parameter.midi_max
    chroma = mod(p,12)+1;
    f_chroma(:,chroma) = f_chroma(:,chroma)+f_pitch(:,p);
end

% normalise the chroma vectors according the norm l^p
f_chroma_norm = zeros(seg_num,12);
p = parameter.norm_p;
unit_vec = ones(1,12);
unit_vec = unit_vec/norm(unit_vec,p);
for k=1:seg_num
    n = norm(f_chroma(k,:),p);
    if n<parameter.stat_thresh
        f_chroma_norm(k,:) = unit_vec;
    else
        f_chroma_norm(k,:) = f_chroma(k,:)/n;
    end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Update sideinfo
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
sideinfo.chroma.midi_min = parameter.midi_min;
sideinfo.chroma.midi_max = parameter.midi_max;
sideinfo.chroma.stat_thresh = parameter.stat_thresh;
sideinfo.chroma.norm_p = parameter.norm_p;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Saving to file
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
if parameter.save == 1
    if isfield(parameter,'save_dir')==0
        parameter.save_dir = '';
    end
    if isfield(parameter,'save_filename')==0
        parameter.save_filename = '';
    end
    
    filename = strcat(parameter.save_filename,'_chroma');
    save(strcat(parameter.save_dir,filename),'f_chroma_norm','f_chroma','sideinfo');
end


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
if parameter.vis==1
    
    chroma_names                 = ['C ';'C#';'D ';'D#';'E ';'F ';'F#';'G ';'G#';'A ';'A#';'B '];

    if parameter.vis_Chroma==1
        figure;
        set(gcf,'renderer','painters');
        h = imagesc(f_chroma');
        set(gca,'YTick',[1:12]);
        set(gca,'YTickLabel',chroma_names);
        set(gca,'YDir','normal');
        colormap(hot);
        colorbar;
        title('Chromagram');
        xlabel('Time [samples]');        
        drawnow;
    end

    if parameter.vis_ChromaNorm==1
        figure;
        set(gcf,'renderer','painters');
        h = imagesc(f_chroma_norm',[0,1]);
        set(gca,'YTick',[1:12]);
        set(gca,'YTickLabel',chroma_names);
        set(gca,'YDir','normal');
        colormap(hot);
        colorbar;        
        title(['Normalized chromagram, p=',int2str(p)]);
        xlabel('Time [samples]');
        drawnow;
    end
end


% if parameter.vis_STMSP == 1
%     figure;
%     set(gcf,'renderer','painters');
%     t = [0:seg_num-1]/parameter.featureRate;
%     h = imagesc(t,[],f_pitch');
%     %set(gca,'YTick',[1:12]);
%     %set(gca,'YTickLabel',chroma_names);
%     set(gca,'YDir','normal');
%     if t(end)>t(1)
%         set(gca,'XLim',[t(1),t(end)]);
%     end
%     colorbar;
%     title('STMSP pitch decomposition');
%     xlabel('Time in seconds');
%     ylabel('MIDI note number');
%     drawnow;
% end