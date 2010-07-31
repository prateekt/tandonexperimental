%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: test_convert_pitchSTMSP_to_chroma.m
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description: 
% * Computes chroma features (f_chroma) from pitch features (f_pitch) 
%
% Reference: 
% Details on the feature computation can be found in the following book:
%
% Meinard Mueller: Information Retrieval for Music and Motion,
%                  Springer 2007
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear;
close all hidden;

directory = 'data_feature/';


%filename = 'Bach_BWV988-Aria-Measures1-4_Meinard_fast.wav';
%filename = 'Burgmueller_Op100-02-FirstPart_Meinard_SE.wav';
%filename = 'Systematic_Cadence-C-Major_Meinard_portato.wav';
%filename = 'Systematic_Cadence-C-Major_Meinard_staccato.wav';
%filename = 'Systematic_Scale-C-Major_Meinard_fast.wav';
%filename = 'Systematic_Scale-C-Major_Meinard_middle.wav';
filename = 'Systematic_Chord-C-Major_Eight-Instruments.wav';

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Loads pitch features (f_pitch) and computes chroma features (f_chroma)
%
% Note: feature filename is specified by WAV filename
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

win_len = 4410;
filename_STMSP = strcat(filename(1:length(filename)-4),'_pitchSTMSP_',num2str(win_len));
load(strcat(directory,filename_STMSP)); % load f_pitch and sideinfo;

parameter.vis = 0;
%parameter.save = 1;
%parameter.save_dir = 'data_feature/';
%parameter.save_filename = strcat(sideinfo.wav.filename(1:length(sideinfo.wav.filename)-4));

[f_chroma_norm,f_chroma,sideinfo] = pitchSTMSP_to_chroma(f_pitch,parameter,sideinfo);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization of chromagrams (f_chroma_norm,f_chroma)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

parameter.featureRate = sideinfo.pitchSTMSP.featureRate;
parameter.xlabel = 'Time [Seconds]';
parameter.title = 'Chromagram';    
visualize_chroma(f_chroma,parameter); 

parameter.title = 'Normalized chromagram';
parameter.imagerange = [0 1];
visualize_chroma(f_chroma_norm,parameter);
set(gcf,'Position',[521 183 560 420]);