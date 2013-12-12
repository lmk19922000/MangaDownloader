import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

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
	private static boolean isExit = false;

	/**
	 * Create the composite.
	 * 
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
		fd_text.right = new FormAttachment(100, -69);
		fd_text.left = new FormAttachment(lblLink, 31);
		fd_text.top = new FormAttachment(lblLink, -3, SWT.TOP);
		link.setLayoutData(fd_text);
		toolkit.adapt(link, true, true);

		path = new Text(this, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.left = new FormAttachment(link, 0, SWT.LEFT);
		fd_text_1.top = new FormAttachment(0, 62);
		path.setLayoutData(fd_text_1);
		toolkit.adapt(path, true, true);
		fd_text_1.right = new FormAttachment(100, -69);

		Button btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				
				try {
					String url;
					// url = "http://www.mangareader.net/426/rave.html";
					url = link.getText();
					Document doc = Jsoup.connect(url).userAgent("Mozilla")
							.timeout(0).get();
					String baseURL = "http://www.mangareader.net";
					int page = 1;

					try {
						Elements ee = doc
								.select("#chapterlist #listing tbody tr td a");
						for (Element e : ee) {
							if (isExit) {
								break;
							}

							System.out.println(e.text());
							System.out.println(e.attr("href"));

							progress.setText("Currently downloading "
									+ e.text());

							String chapterURL = baseURL + e.attr("href");
							Document chapter = Jsoup.connect(chapterURL)
									.userAgent("Mozilla").timeout(0).get();
							Elements ee2 = chapter
									.select("#topchapter #navi #selectpage #pageMenu option");

							for (Element e2 : ee2) {

								String pageURL = baseURL + e2.attr("value");

								Document pageinchapter = Jsoup.connect(pageURL)
										.userAgent("Mozilla").timeout(0).get();

								String src = pageinchapter
										.select("#imgholder a img").first()
										.attr("src");

								String imgName = StringUtils
										.substringAfterLast(src, "/");
								String fileType = StringUtils
										.substringAfterLast(src, ".");

								// String folderPath =
								// "C:/Users/AdminNUS/Downloads/feed/";
								String folderPath = path.getText();
								folderPath = folderPath.replaceAll("\\\\", "/");
								folderPath = folderPath + "/";

								URL url2 = new URL(src);
								InputStream in = url2.openStream();
								OutputStream out = new BufferedOutputStream(
										new FileOutputStream(folderPath
												+ String.valueOf(page) + "."
												+ fileType));
								page++;
								for (int b; (b = in.read()) != -1;) {
									out.write(b);
								}
								out.close();
								in.close();
							}
							progress.setText("Download completed");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		});
		FormData btnSave = new FormData();
		btnSave.top = new FormAttachment(path, 16);
		btnSave.left = new FormAttachment(0, 94);
		btnSave.right = new FormAttachment(100, -252);
		btnNewButton_1.setLayoutData(btnSave);
		toolkit.adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("Download");

		Label lblProgress = new Label(this, SWT.NONE);
		FormData fd_lblProgress = new FormData();
		fd_lblProgress.left = new FormAttachment(0, 10);
		fd_lblProgress.top = new FormAttachment(lblSaveTo, 62);
		lblProgress.setLayoutData(fd_lblProgress);
		toolkit.adapt(lblProgress, true, true);
		lblProgress.setText("Progress");

		progress = new Label(this, SWT.NONE);
		FormData fd_lblChap = new FormData();
		fd_lblChap.top = new FormAttachment(btnNewButton_1, 18);
		fd_lblChap.left = new FormAttachment(lblProgress, 16);
		fd_lblChap.right = new FormAttachment(100, -10);
		progress.setLayoutData(fd_lblChap);
		toolkit.adapt(progress, true, true);
		progress.setText("Not started");

		Button btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// parent component of the dialog
				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setDialogTitle("Specify a folder to save");

				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File folderToSave = fileChooser.getSelectedFile();
					System.out.println("Save to folder: "
							+ folderToSave.getAbsolutePath());
					path.setText(folderToSave.getAbsolutePath());
				}
			}
		});
		FormData fd_btnBrowse = new FormData();
		fd_btnBrowse.top = new FormAttachment(lblSaveTo, -5, SWT.TOP);
		fd_btnBrowse.right = new FormAttachment(progress, 0, SWT.RIGHT);
		btnBrowse.setLayoutData(fd_btnBrowse);
		toolkit.adapt(btnBrowse, true, true);
		btnBrowse.setText("Browse");

		Button btnCancel = new Button(this, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isExit = true;
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(btnNewButton_1, 142, SWT.RIGHT);
		fd_btnCancel.top = new FormAttachment(path, 16);
		fd_btnCancel.left = new FormAttachment(btnNewButton_1, 38);
		btnCancel.setLayoutData(fd_btnCancel);
		toolkit.adapt(btnCancel, true, true);
		btnCancel.setText("Cancel");
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Downloader calc = new Downloader(shell, SWT.NONE);
		calc.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
