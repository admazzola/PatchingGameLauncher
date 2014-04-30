package launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.Weaver;
import com.network.NodeInfo;
import com.orb.WeaverOrb;

import launcher.auth.VersionManager;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Launcher {

	static Launcher launcher;
	
	static LauncherFrame launcherFrame;
	static LauncherPanel launcherPanel;
	
	public VersionManager versionManager;
	
	public static void main(String args[]){
		launcher = new Launcher();	
		
		try{
		launcher.initialize();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	private Weaver weaver;
	 
	 
	private void initialize() throws UnknownHostException {
		
		new File(SharedData.PATH_TO_CLIENT_JAR).mkdir();
				
		LauncherFrame frame = new LauncherFrame();
		frame.setTitle("Sands Launcher " + SharedData.VERSION);
        frame.setSize(725,525);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              System.exit(0);
            }
        });
        
        
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());
        
        
		launcherPanel = new LauncherPanel();
		launcherPanel.getSidebar().setAlignmentY(Component.BOTTOM_ALIGNMENT);
		launcherPanel.getSidebar().setAlignmentX(Component.CENTER_ALIGNMENT);
		setLookAndFeel();
		
		frame.getContentPane().add(launcherPanel);
		
		//frame.pack();
        frame.setVisible(true);
        
       
        //107.170.122.137
		//pass in where the file should be located (where it WILL be located for masters)
        weaver = new Weaver( new NodeInfo[]{new NodeInfo("192.168.62.187",2232)} , 2242 );
                
        WeaverOrb orb = new WeaverOrb( SharedData.PATH_TO_CLIENT_JAR + "sandsofosiris.jar", weaver );
		orb.start();		 
		      
		//pass in addresses of the master nodes		
		weaver.registerOrb(orb);
		weaver.start();
		
		
		while(true){
			update();
		}
	}
	
	boolean readyToLaunch = false;
	private void update() {
		
		if(!readyToLaunch ){
			if( getWeaver().getIsSeeding()){
				readyToLaunch = true;
				getPanel().getSidebar().setReadyToLaunch();  
				getPanel().getSidebar().getProgressBar().setVisible(false);
			}else{
					getPanel().getSidebar().getProgressBar().setVisible(true);
					getPanel().getSidebar().getProgressBar().setValue((int) (getWeaver().getLeechProgress() * 100) );
				
			}
		}
		
		getPanel().getSidebar().getStatusLabel().setStatus(getWeaver().getStatus());
		
	}


	public Weaver getWeaver(){
	return weaver;
	}
	
	
	
	
	
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName() );
			} catch (Exception f) {			
				f.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}

	public static LauncherPanel getPanel(){
		return launcherPanel;	
		
	}

	public static void println(String s) {
		System.out.println(s);
		getPanel().getTabPanel().getConsole().print(s);
	}

	public static void setGameUpToDate() {
		
		Launcher.getPanel().getSidebar().getLaunchButton().setVisible(true);
		
	}
	
	
	
}
