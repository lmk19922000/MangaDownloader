import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Downloader extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Text link;
	private Text path;
	private Label progress;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Downloader(Composite parent, int style) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new FormLayout());
		
		Label lblLink = new Label(this, SWT.NONE);
		FormData fd_lblLink = new FormData();
		fd_lblLink.top = new FormAttachment(0, 27);
		fd_lblLink.left = new FormAttachment(0, 10);
		lblLink.setLayoutData(fd_lblLink);
		toolkit.adapt(lblLink, true, true);
		lblLink.setText("Link");
		
		Label lblSaveTo = new Label(this, SWT.NONE);
		FormData fd_lblSaveTo = new FormData();
		fd_lblSaveTo.top = new FormAttachment(lblLink, 23);
		fd_lblSaveTo.left = new FormAttachment(lblLink, 0, SWT.LEFT);
		lblSaveTo.setLayoutData(fd_lblSaveTo);
		toolkit.adapt(lblSaveTo, true, true);
		lblSaveTo.setText("Save to");
		
		link = new Text(this, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(lblLink, 39);
		link.setLayoutData(fd_text);
		toolkit.adapt(link, true, true);
		
		path = new Text(this, SWT.BORDER);
		fd_text.right = new FormAttachment(path, 0, SWT.RIGHT);
		fd_text.bottom = new FormAttachment(path, -17);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(0, 62);
		fd_text_1.left = new FormAttachment(lblSaveTo, 23);
		path.setLayoutData(fd_text_1);
		toolkit.adapt(path, true, true);
		fd_text_1.right = new FormAttachment(100, -10);
		
		Button btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
					String url;
					//url = "http://www.mangareader.net/426/rave.html";
					url = link.getText();
					Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get();
					String baseURL = "http://www.mangareader.net";
					int page = 1;
					
					try {
						Elements ee = doc.select("#chapterlist #listing tbody tr td a");
						for (Element e : ee) {
							System.out.println(e.text());
							System.out.println(e.attr("href"));
							
							progress.setText("Currently downloading " + e.text());
							
							String chapterURL = baseURL + e.attr("href");
							Document chapter = Jsoup.connect(chapterURL)
									.userAgent("Mozilla").timeout(0).get();
							Elements ee2 = chapter
									.select("#topchapter #navi #selectpage #pageMenu option");

							for (Element e2 : ee2) {

								String pageURL = baseURL + e2.attr("value");

								Document pageinchapter = Jsoup.connect(pageURL)
										.userAgent("Mozilla").timeout(0).get();

								String src = pageinchapter.select("#imgholder a img")
										.first().attr("src");

								String imgName = StringUtils.substringAfterLast(src,
										"/");
								String fileType = StringUtils.substringAfterLast(src,
										".");

								//String folderPath = "C:/Users/AdminNUS/Downloads/feed/";
								String folderPath = path.getText();
								folderPath = folderPath.replaceAll("\\\\", "/");
								folderPath = folderPath + "/";
								
								URL url2 = new URL(src);
								InputStream in = url2.openStream();
								OutputStream out = new BufferedOutputStream(
										new FileOutputStream(folderPath
												+ String.valueOf(page) + "." + fileType));
								page++;
								for (int b; (b = in.read()) != -1;) {
									out.write(b);
								}
								out.close();
								in.close();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.top = new FormAttachment(path, 16);
		fd_btnNewButton_1.left = new FormAttachment(0, 176);
		fd_btnNewButton_1.right = new FormAttachment(100, -170);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
		toolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("Save");
		
		Label lblProgress = new Label(this, SWT.NONE);
		FormData fd_lblProgress = new FormData();
		fd_lblProgress.left = new FormAttachment(0, 10);
		fd_lblProgress.top = new FormAttachment(lblSaveTo, 62);
		lblProgress.setLayoutData(fd_lblProgress);
		toolkit.adapt(lblProgress, true, true);
		lblProgress.setText("Progress");
		
		progress = new Label(this, SWT.NONE);
		FormData fd_lblChap = new FormData();
		fd_lblChap.right = new FormAttachment(link, 0, SWT.RIGHT);
		fd_lblChap.left = new FormAttachment(lblProgress, 16);
		fd_lblChap.top = new FormAttachment(btnNewButton_1, 18);
		progress.setLayoutData(fd_lblChap);
		toolkit.adapt(progress, true, true);
		progress.setText("Not started");
	}

	public static void main(String[] args){
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    Downloader calc = new Downloader(shell, SWT.NONE);
	    calc.pack();
	    shell.pack();
	    shell.open();
	    while(!shell.isDisposed()){
	        if(!display.readAndDispatch()) display.sleep();
	    }
	} 
}
