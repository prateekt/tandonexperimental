
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Name: test_convert_audio_to_pitchSTMSP.m
% Date of Revision: 15.12.2009
% Programmer: Meinard Mueller, Sebastian Ewert
%
% Description: 
% * Computes pitch subband decomposition of WAV file
%   (default: MIDI pitches 21 to 108) 
% * each pitch subband contains short time mean-square power (STMSP) 
% * Features are computed in a batch modus
% * Features are stored in folder 'data_feature/'
%
% Reference: 
% Details on the feature computation can be found in the following book:
%
% Meinard Mueller: Information Retrieval for Music and Motion,
%                  Springer 2007
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
clear;
close all hidden;


dirFileNames = {
 %'data_WAV/','Bach_BWV988-Aria-Measures1-4_Meinard_fast.wav';
 %'data_WAV/','Burgmueller_Op100-02-FirstPart_Meinard_SE.wav';
 %'data_WAV/','Systematic_Cadence-C-Major_Meinard_portato.wav';
 %'data_WAV/','Systematic_Cadence-C-Major_Meinard_staccato.wav';
 %'data_WAV/','Systematic_Scale-C-Major_Meinard_fast.wav';
 %'data_WAV/','Systematic_Scale-C-Major_Meinard_middle.wav';
 'data_WAV/','Systematic_Chord-C-Major_Eight-Instruments.wav';
};

for n=1:size(dirFileNames,1)
    clear parameter;
    parameter.message = 1;
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Convert WAV to expected audio format (mono, 22050 Hz)
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    [f_audio,sideinfo] = wav_to_audio('', dirFileNames{n,1}, dirFileNames{n,2},parameter);

    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Compute FBpitchSTMSP
    % 
    % Input: audio file of format: mono, 22050 Hz
    %
    % Output: sequence of pitch vectors 
    %         (specified by N x 120 matrix f_pitch)
    %         Only subband for MIDI pitches 21 to 108 are computed, the
    %         other subbands are set to zero.
    %
    % Parameter: parameter.win_len specifies window length (in samples)
    %            with window overlap of half size  
    %            Example: audio sampling rate: 22050 Hz
    %                     parameter.win_len = 4410
    %                     Resulting feature rate: 10 Hz
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    parameter.win_len = 4410;
    parameter.fs = sideinfo.wav.fs;
    parameter.save = 1;
    parameter.saveDir = 'data_feature/';
    parameter.saveFilename = strcat(sideinfo.wav.filename(1:length(sideinfo.wav.filename)-4));
    parameter.visualize = 1;

    [f_pitch,sideinfo] = audio_to_pitchSTMSP_via_FB(f_audio,parameter,sideinfo);
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % Visualization of pitch decomposition (f_pitch)
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
    parameter.usePitchNameLabels = 1;
    parameter.title = 'Logarithmic compression of amplitude';
    parameter.featureRate = (parameter.fs*2/parameter.win_len);
    parameter.xlabel = 'Time [Seconds]';
    parameter.ylabel = 'Pitch';
    visualize_pitchSTMSP(log(5*f_pitch+1),parameter);
    set(gcf,'Position',[521 183 560 420]);
end
