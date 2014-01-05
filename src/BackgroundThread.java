
public class BackgroundThread extends Thread {
	
	BackgroundRunnable runnableThread;
	
	public BackgroundThread(Runnable r) {
		// TODO Auto-generated constructor stub
		super(r);
		this.runnableThread = (BackgroundRunnable) r;
	}
	
	public void cancel(){
		runnableThread.setCancel();
	}
	
}
