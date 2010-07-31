%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: test_convert_pitchSTMSP_to_CRP.m
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description: 
% * Computes CRP features (f_crp) from pitch features (f_pitch) 
% * CRP is a chroma-like feature tuned for timbre-invariance
%
% Reference: 
% Details on the feature computation can be found in the following articles:
%
% Meinard M�ller, Sebastian Ewert, and Sebastian Kreuzer
% Making chroma features more robust to timbre changes.
% Proceedings of IEEE International Conference on Acoustics, Speech, and
% Signal Processing (ICASSP), Taipei, Taiwan, pp. 1869-1872, 2009.
%
% Meinard M�ller, and Sebastian Ewert
% Towards Timbre-Invariant Audio Features for Harmony-Based Music.
% IEEE Transactions on Audio, Speach, and Language Processing (to appear). 
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
% Loads pitch features (f_pitch) and computes CRP features (f_crp)
%
% Note: feature filename is specified by WAV filename
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

win_len = 4410;
filename_STMSP = strcat(filename(1:end-4),'_pitchSTMSP_',num2str(win_len));
load(strcat(directory,filename_STMSP)); % load f_pitch and sideinfo;

parameter.vis = 0;
parameter.stat_window_length = 1;
parameter.stat_downsample = 1;
parameter.featureRate = sideinfo.pitchSTMSP.featureRate;
[f_crp,sideinfo] = pitchSTMSP_to_CRP(f_pitch,parameter,sideinfo);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Visualization of CRP chromagram 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

parameter.featureRate = sideinfo.CRP.featureRate;
parameter.xlabel = 'Time [Seconds]';
parameter.title = 'CRP chromagram';
parameter.imagerange = [-1 1];

blueSepia = gray(64);
blueSepia(:,1:2) = blueSepia(:,1:2) * 0.5;
parameter.colormap = [flipud(blueSepia) ; hot(64)];
visualize_chroma(f_crp,parameter);
