package schemas;
import java.util.concurrent.Semaphore;

/**
 * This is the base class for brain schemas. To simulate the parallel processes
 * of brain schemas, each brain schema runs its own thread. Schemas communicate 
 * by sending each other input and producing output.
 *   
 * @author Prateek Tandon
 */
public abstract class BrainSchema {
	
	/**
	 * If the semaphore is released, it indicates that the brain schema
	 * has received input and thus might require an update. The moment schemas
	 * receive input, they can asynchronously generate output.
	 */
	private Semaphore receivedInput = new Semaphore(1,true);
	
	/**
	 * The thread that the schema runs it.
	 */
	private SchemaThread thread;
	
	/**
	 * The name of the brain schema.
	 */
	private String name;
	
	/**
	 * Constructor
	 */
	protected BrainSchema(String name) {
		this.name = name;
	}

	/**
	 * Is called in the code every time a schema receives input.
	 */
	protected void receivedInput() {
		receivedInput.release();
	}

	/** 
	 * Method called to produce output of schema and give it
	 * to schemas that need it.
	 */
	protected abstract boolean produceOutput();
	
	/**
	 * Prints a debug message.
	 * @param msg The message to print
	 */
	protected void printDebug(String msg) {
		System.out.println(name + ": " + msg);
	}
	
	/**
	 * Starts the brain schema thread so it can start recieving input
	 * and producing output.
	 */
	public synchronized void startThread() {
		if (thread == null) {
			thread = new SchemaThread(name);
			thread.start();
		} else {
			thread.interrupt();
		}
	}

	/**
	 * Called to kill the schema thread if need be.
	 */
	public void stopThread() {
		if (thread != null) {
			thread.stopSchema();
			thread = null;
		}
	}

	/**
	 * Thread that schema lives in.
	 * @author Prateek Tandon
	 */
	private class SchemaThread extends Thread {
		private volatile boolean running = false;

		private SchemaThread(String name) {
			super(name);
		}

		public void run() {
			running = true;

			while (running) {
				try {
					receivedInput.acquire();
					while (produceOutput());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void stopSchema() {
			running = false;
			this.interrupt();
		}
	}
	
	public String getName() {
		return name;
	}
}

