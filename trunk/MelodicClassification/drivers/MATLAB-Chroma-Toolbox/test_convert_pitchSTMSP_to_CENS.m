%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: test_convert_pitchSTMSP_to_CENS.m
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description: 
% * Computes CENS features (f_CENS) from pitch features (f_pitch) 
% * CENS is a chroma-like feature 
%   (Chroma Energy Normalized Statistics)
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
filename = 'Burgmueller_Op100-02-FirstPart_Meinard_SE.wav';
%filename = 'Systematic_Cadence-C-Major_Meinard_portato.wav';
%filename = 'Systematic_Cadence-C-Major_Meinard_staccato.wav';
%filename = 'Systematic_Scale-C-Major_Meinard_fast.wav';
%filename = 'Systematic_Scale-C-Major_Meinard_middle.wav';
%filename = 'Systematic_Chord-C-Major_Eight-Instruments.wav';


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Loads pitch features (f_pitch) and computes CENS features (f_CENS)
%
% Note: feature filename is specified by WAV filename
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

win_len = 4410;
filename_STMSP = strcat(filename(1:length(filename)-4),'_pitchSTMSP_',num2str(win_len));
load(strcat(directory,filename_STMSP)); % load f_pitch and sideinfo;

parameter.stat_window_length  = 41;
parameter.stat_downsample     = 10;
parameter.featureRate = sideinfo.pitchSTMSP.featureRate;
[f_CENS,f_chroma,sideinfo] = pitchSTMSP_to_CENS(f_pitch,parameter,sideinfo);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization of chromagrams (f_CENS,f_chroma)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

parameter.featureRate = sideinfo.pitchSTMSP.featureRate;
parameter.xlabel = 'Time [Seconds]';
parameter.imagerange = [0 1];
parameter.title = 'Chromagram';
visualize_chroma(f_chroma,parameter);

parameter.featureRate = sideinfo.CENS.featureRate;
parameter.xlabel = 'Time [Seconds]';
parameter.title = 'CENS chromagram';
parameter.imagerange = [0 1];
visualize_chroma(f_CENS,parameter);

set(gcf,'Position',[521 183 560 420]);


