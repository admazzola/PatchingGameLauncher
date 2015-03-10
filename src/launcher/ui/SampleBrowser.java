package launcher.ui;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * An example of using CSSBox for the HTML page rendering and display.
 * It parses the style sheets and creates a box tree describing the
 * final layout. As the HTML parser, jTidy is used.
 * 
 * @author  burgetr
 */
public class SampleBrowser extends javax.swing.JFrame 
{
	private static final long serialVersionUID = -1336331141597077348L;
	
	/** The swing canvas for displaying the rendered document */
    private javax.swing.JPanel browserCanvas;
    
    /** Scroll pane for the canvas */ 
    private javax.swing.JScrollPane documentScroll;
    
    /** Root DOM Element of the document body */
	private Element docroot;
	
	/** The CSS analyzer of the DOM tree */
    private DOMAnalyzer decoder;

    
    /** 
     * Creates a new application window and displays the rendered document
     * @param root The root DOM element of the document body
     * @param baseurl The base URL of the document used for completing the relative paths
     * @param decoder The CSS analyzer that provides the effective style of the elements 
     */
    public SampleBrowser(Element root, URL baseurl, DOMAnalyzer decoder)
    {
        docroot = root;
        this.decoder = decoder;
        initComponents(baseurl);
    }
    
    /**
     * Creates and initializes the GUI components
     * @param baseurl The base URL of the document used for completing the relative paths
     */
    private void initComponents(URL baseurl) 
    {
        documentScroll = new javax.swing.JScrollPane();
        
        //Create the browser canvas
        browserCanvas = new BrowserCanvas(docroot, decoder, new java.awt.Dimension(1000, 600), baseurl);

        //A simple mouse listener that displays the coordinates clicked
        browserCanvas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("Click: " + e.getX() + ":" + e.getY());
            }
            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
        });
        
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        setTitle("CSSBox Browser");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        documentScroll.setViewportView(browserCanvas);
        getContentPane().add(documentScroll);
        pack();
    }
    
    /** 
     * Exit the Application 
     */
    private void exitForm(java.awt.event.WindowEvent evt)
    {
        System.exit(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
    	if (args.length != 1)
    	{
    		System.err.println("Usage: SimpleBrowser <url>");
    		System.exit(0);
    	}
    	
        try {
            //Open the network connection 
            DocumentSource docSource = new DefaultDocumentSource(args[0]);
            
            //Parse the input document
            DOMSource parser = new DefaultDOMSource(docSource);
            Document doc = parser.parse();
            
            //Create the CSS analyzer
            DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
            da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
            da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
            da.getStyleSheets(); //load the author style sheets
            
            //Display the result
            SampleBrowser test = new SampleBrowser(da.getRoot(), docSource.getURL(), da);
            test.setSize(1275, 750);
            test.setVisible(true);
            
            docSource.close();
            
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
}