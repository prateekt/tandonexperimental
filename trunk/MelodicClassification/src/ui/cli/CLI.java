package ui.cli;
import ui.UserOperations;
import util.sys.StringBuffer2;
import util.sys.SysUtil;

public class CLI {
	
	UserOperations op;
		
	public CLI() {
		op = new UserOperations();
		stateMachine();
	}
	
	public int getMainMenuChoice() {
		StringBuffer2 msg = new StringBuffer2();
		msg.appendN("--Options--");
		msg.appendN("(1) Generate Training Examples");
		msg.appendN("(2) Export to WAVs");
		msg.appendN("(3) Record for Transcription");
		msg.appendN("(4) Record for Training");
		msg.appendN("(5) Record for classification"); 
/*		msg.appendN("(3) Train all classifiers");
		msg.appendN("(4) Record clip for classification");
		msg.appendN("(5) Record clip for database");*/
		msg.appendN("(6) Exit");
		return SysUtil.promptInt(msg.toString(), 1, 6);
	}	

	public void stateMachine() {
		smLoop:while(true) {
			int val = getMainMenuChoice();
			switch(val) {
				case 1:
					op.handleGenerateTrainingExamples();
					break;
				case 2:
					op.handleExportToWavs();
					break;
				case 3:
					op.handleRecordTrainedTranscription();
					break;
				case 4:
					op.handleTrainChromaRecord();
					break;
				case 5:
					op.handleChromaClassificationRecord2();
					break;
				case 6:
					System.exit(0);
					break;
			}
		}
	}	
	
/*	public void stateMachine() {
		smLoop:while(true) {
			int val = getMainMenuChoice();
			switch(val) {
				case 1:
					op.handleGenerateTrainingExamples();
					break;
				case 2:
					op.handleExportToWavs();
					break;
				case 3:
					op.handleTrainClassifiers();
					break;
				case 4:
					op.handleClassificationRecord();
					break;
				case 5:
					op.handleDBClipRecord();
					break;
				case 6:
					System.exit(0);
					break;
			}
		}
	}*/
}
