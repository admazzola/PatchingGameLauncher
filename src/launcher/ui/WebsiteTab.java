package launcher.ui;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

import launcher.Launcher;
import launcher.OperatingSystem;
import org.fit.cssbox.layout.BrowserCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.fit.cssbox.css.DOMAnalyzer;

public class WebsiteTab extends JScrollPane{

	/** The swing canvas for displaying the rendered document */
    private javax.swing.JPanel browserCanvas;
	
    /** Root DOM Element of the document body */
	private Element docroot;
	
	/** The CSS analyzer of the DOM tree */
    private DOMAnalyzer decoder;
    
	public WebsiteTab(){
		
		  browserCanvas = new BrowserCanvas(docroot, decoder, new java.awt.Dimension(1000, 600), baseurl);
		  
		  
		  
		  
		
		blog.setEditable(false);
		blog.setMargin(null);
		blog.setBackground(Color.DARK_GRAY);
		blog.setContentType("text/html");
		blog.addHyperlinkListener(new HyperlinkListener(){

			public void hyperlinkUpdate(HyperlinkEvent he) {
				if(he.getEventType() == EventType.ACTIVATED){
					try{
						OperatingSystem.openLink(he.getURL().toURI());						
					}catch(Exception e){
						Launcher.println("failed to open url");
					}				
			}
			
				
			}
			
			
			
		});
		
		
		setViewportView(blog);
		
	}
	
	public void setPage(final String url) {
			    Thread thread = new Thread("Update website tab")
			    {
			      public void run() {
			        try {
			          blog.setPage(new URL(url));
			        } catch (Exception e) {
			          Launcher.println("Unexpected exception loading " + url);
			          blog.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Failed to get page</h1><br>" + e.toString() + "</center></font></body></html>");
			        }
			      }
			    };
			    thread.setDaemon(true);
			    thread.start();
			  }
	
}
