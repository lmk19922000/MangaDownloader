import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.nodes.Element;


public class UpdateProgressThread implements Runnable{
	
	Label progress;
	Element e;
	Shell shell;
	
	public UpdateProgressThread(Shell shell, Label progress, Element e) {
		// TODO Auto-generated constructor stub
		this.shell = shell;
		this.progress = progress;
		this.e = e;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (!shell.isDisposed()){
			progress.setText("Currently downloading "
					+ e.text());
			shell.redraw ();
		}
			
            
	}

}
