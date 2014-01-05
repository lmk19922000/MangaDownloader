import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.nodes.Element;

public class UpdateProgressThread implements Runnable{
	
	Label progress;
	Element e;
	Shell shell;
	
	public UpdateProgressThread(Shell shell, Label progress, Element e) {
		this.shell = shell;
		this.progress = progress;
		this.e = e;
	}

	@Override
	public void run() {
		if (!shell.isDisposed()){
			progress.setText("Currently downloading "
					+ e.text());
			shell.redraw ();
		}
	}
}
