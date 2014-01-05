import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BackgroundThread implements Runnable {
	
	Display display;
	Shell shell;
	Document doc;
	Label progress;
	
	String baseURL;
	String folderPath;
	int page;

	public BackgroundThread(Display display, Shell shell, Document doc,
			Label progress, String baseURL, String folderPath, int page) {
		 this.display = display;
		 this.shell = shell;
		 this.doc = doc;
		 this.progress = progress;
		 this.baseURL = baseURL;
		 this.folderPath = folderPath;
		 this.page = page;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Elements ee = doc
					.select("#chapterlist #listing tbody tr td a");
			for (Element e : ee) {
				System.out.println(e.text());
				System.out.println(e.attr("href"));
				
				display.asyncExec (new UpdateProgressThread (shell, progress, e));

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
				
				display.asyncExec (new Runnable () {
				      public void run () {
				         if (shell.isDisposed())
				        	progress.setText("Download completed");
				            shell.redraw ();
				      }
				   });
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	

}
