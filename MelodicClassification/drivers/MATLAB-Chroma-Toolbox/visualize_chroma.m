function visualize_chroma(f_chroma,parameter)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: visualize_chroma
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description:
% Visualization of f_chroma
%
% Input: 
%        f_chroma
%        parameter: if not specified, fields are specified by default values
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

if nargin<2
   parameter=[]; 
end

if isfield(parameter,'featureRate')==0
   parameter.featureRate = 0;
end

if isfield(parameter,'colorbar')==0
   parameter.colorbar = 1;
end

if isfield(parameter,'colormap')==0
    parameter.colormap = 'hot';
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

if isfield(parameter,'imagerange')==0
   parameter.imagerange = 0;
end


if isfield(parameter,'printPaperPosition')==0
   parameter.printPaperPosition = [1   10   26  15]; %[left, bottom, width, height]
end


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


seg_num = size(f_chroma,1);

% hot2 = hot;
% hot2 = [hot2; hot2(32:64,:); hot2(32:64,:)];
% hot2 = sort(hot2);


chroma_names                 = ['C ';'C#';'D ';'D#';'E ';'F ';'F#';'G ';'G#';'A ';'A#';'B '];
figure;
set(gcf,'renderer','painters');

if parameter.featureRate == 0
    t = (1:seg_num);
else
    t = (0:seg_num-1)/parameter.featureRate;
end


% t = [0:seg_num-1]/parameter.win_res;
if parameter.imagerange == 0
    imagesc(t,[1:12],f_chroma');
else
    imagesc(t,[1:12],f_chroma',parameter.imagerange);
end
set(gca,'YTick',[1:12]);
set(gca,'YTickLabel',chroma_names);
set(gca,'YDir','normal');
if t(end)>t(1)
    set(gca,'XLim',[t(1),t(end)]);
end

title(parameter.title);
xlabel(parameter.xlabel);
colormap(parameter.colormap);

if parameter.colorbar == 1
    colorbar;
end
drawnow;

if parameter.print == 1
    %pos = get(h,'Position');
    %pos(3)=pos(3)/3;
    %set(h,'Position',pos)        
    set(gcf,'PaperPosition',parameter.printPaperPosition);
    print('-depsc2',strcat(parameter.printDir,parameter.printFile));
end




