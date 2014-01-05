
public class BackgroundThread extends Thread {
	
	BackgroundRunnable runnableThread;
	
	public BackgroundThread(Runnable r) {
		super(r);
		this.runnableThread = (BackgroundRunnable) r;
	}
	
	public void cancel(){
		runnableThread.setCancel();
	}
	
}
