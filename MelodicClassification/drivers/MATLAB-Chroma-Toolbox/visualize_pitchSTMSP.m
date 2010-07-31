function visualize_pitchSTMSP(f_pitch,parameter);
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: visualize_pitchSTMSP
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description:
% Visualization of f_pitch
%
% Input: 
%        f_pitch
%        parameter: if not specified, fields are specified by default values
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if nargin<2
   parameter=[]; 
end

if isfield(parameter,'midi_min')==0
   parameter.midi_min = 21;
end

if isfield(parameter,'midi_max')==0
   parameter.midi_max = 108;
end

if isfield(parameter,'featureRate')==0
   parameter.featureRate = 0;
end

if isfield(parameter,'colorbar')==0
   parameter.colorbar = 1;
end

if (isfield(parameter,'colormap')==0) || (isstr(parameter.colormap) && strcmpi(parameter.colormap,'hot2'))
    hot2 = hot(64);
    hot2 = [hot2; hot2(32:64,:); hot2(32:64,:)];
    hot2 = sort(hot2);
    parameter.colormap = hot2;
end

if isfield(parameter,'print')==0
   parameter.print = 0;
end

if isfield(parameter,'printFile')==0
   parameter.printFile = 'figure.eps';
end

if isfield(parameter,'printDir')==0
   parameter.printDir = '';
end

if isfield(parameter,'title')==0
   parameter.title = '';
end

if isfield(parameter,'xlabel')==0
   parameter.xlabel = '';
end

if isfield(parameter,'ylabel')==0
   parameter.ylabel = '';
end

if isfield(parameter,'imagerange')==0
   parameter.imagerange = 0; %[0 1];
end

if isfield(parameter,'usePitchNameLabels')==0
   parameter.usePitchNameLabels = 0;
end
if isfield(parameter,'PitchNameLabels')==0
   parameter.PitchNameLabels = ...
            ['   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C0 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C1 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C2 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C3 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C4 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C5 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C6 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C7 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C8 ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';'   ';...
       'C9 '];
end

if isfield(parameter,'Ytick')==0
   parameter.Ytick = [1 10 20 30 40 50 60 70 80 90 100 110 120]; % not used when usePitchNameLabels==1
end


if isfield(parameter,'printPaperPosition')==0
   parameter.printPaperPosition = [1   10   26  15]; %[left, bottom, width, height]
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
figure;

if parameter.featureRate == 0
    t_axis = (1:size(f_pitch,1));
else
    t_axis = (0:size(f_pitch,1)-1)/parameter.featureRate;
end

p_axis = (parameter.midi_min:parameter.midi_max);
f_image = f_pitch(:,p_axis)';

if parameter.imagerange == 0
    imagesc(t_axis,p_axis,f_image);
else
    imagesc(t_axis,p_axis,f_image,parameter.imagerange);
end

set(gca,'YTick',parameter.Ytick);
set(gca,'YDir','normal');

if parameter.usePitchNameLabels
    set(gca,'YTick',[parameter.midi_min:parameter.midi_max]);
    set(gca,'YTickLabel',parameter.PitchNameLabels(parameter.midi_min:parameter.midi_max,:));
end

title(parameter.title);
xlabel(parameter.xlabel);
ylabel(parameter.ylabel);

colormap(parameter.colormap);
if parameter.colorbar == 1
    colorbar;
end

drawnow;

if parameter.print == 1
    set(gcf,'PaperPosition',parameter.printPaperPosition);
    print('-depsc2',strcat(parameter.printDir,parameter.printFile));    
end


