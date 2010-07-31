parameter.message = 0;
[f_audio,sideinfo] = wav_to_audio('/Users/prateek/Desktop/MATLAB-Chroma-Toolbox/data_WAV/','','Systematic_Chord-C-Major_Eight-Instruments.wav',parameter);
parameter.win_len = 4410;
parameter.fs = sideinfo.wav.fs;
parameter.save = 0;
parameter.visualize = 0;
[f_pitch,sideinfo] = audio_to_pitchSTMSP_via_FB(f_audio,parameter,sideinfo);
log_f_pitch = log(5*f_pitch+1);
seg_num = 17;

MAX = [];
MAX_INDEX = [];
NOTE = [];

for i=1:seg_num
	MAX(i) = log_f_pitch(1,1);
	MAX_INDEX(i) = 1;
end

for i=1:seg_num
	for j=1:120
		if log_f_pitch(i,j) > MAX(i)
			MAX(i) = log_f_pitch(i,j);
			MAX_INDEX(i) = j;
		end
	end
end
fid = fopen('test_wave.txt','wt');
fprintf(fid,'%i ',MAX_INDEX);
