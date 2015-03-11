package launcher.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
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
import org.fit.cssbox.*;
import org.fit.cssbox.swingbox.*;
import org.fit.cssbox.swingbox.demo.SwingBrowserHyperlinkHandler;
import org.fit.cssbox.swingbox.util.GeneralEventListener;
import org.fit.net.DataURLHandler;


public class WebsiteTab extends JScrollPane{

	
    
	public WebsiteTab(){
		
		 
		/*
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
		
		*/
		
		
	}
	
	public void displayURL(String urlstring)
    {
        try {
            if (!urlstring.startsWith("http:") &&
                    !urlstring.startsWith("https:") &&
                    !urlstring.startsWith("ftp:") &&
                    !urlstring.startsWith("file:") &&
                    !urlstring.startsWith("data:"))
                        urlstring = "http://" + urlstring;
                
            URL url = DataURLHandler.createURL(null, urlstring);
           

            displayURLSwingBox(url);
        } catch (Exception e) {
            System.err.println("*** Error: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
	 BrowserPane swingbox = null;
	 
    protected void displayURLSwingBox(URL url) throws IOException
    {
        if (swingbox == null)
        {
            swingbox = new BrowserPane();
           this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
           this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            swingbox.setPreferredSize(new Dimension(300, 200));
            //this.add( swingbox );
            setViewportView(swingbox);
        }
        swingbox.setPage(url);
    }
    
    
    
 
	
	public void setPage(final String url) {
			    Thread thread = new Thread("Update website tab")
			    {
			      public void run() {
			    	  displayURL( url );
			    	 
			        try {
			        //  blog.setPage(new URL(url));
			        } catch (Exception e) {
			          Launcher.println("Unexpected exception loading " + url);
			        //  blog.setText("<html><body><font color=\"#808080\"><br><br><br><br><br><br><br><center><h1>Failed to get page</h1><br>" + e.toString() + "</center></font></body></html>");
			        }
			      }
			    };
			    thread.setDaemon(true);
			    thread.start();
			  }
	
}
