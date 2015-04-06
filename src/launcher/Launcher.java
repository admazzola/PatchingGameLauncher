package launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import launcher.net.FileClient;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class Launcher {

	public static final String CHECKSUM_FAIL_MESSAGE = "nofilefound";

	static Launcher launcher;

	static JFrame launcherFrame;
	static LauncherPanel launcherPanel;
	
	
	public static PipedOutputStream pos = new PipedOutputStream();
	public static PrintStream out;

	public static void main(String args[]) {
		  try {
			UIManager.setLookAndFeel(
			            UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		out = new PrintStream(pos,true);
		
		//System.setOut(ps);
		
		
		launcher = new Launcher();

		try {
			launcher.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}

	private FileClient fileClient;

	private void initialize() throws UnknownHostException {

		new File(SharedData.PATH_TO_CLIENT_JAR).mkdir();

		JFrame frame = new JFrame();
		frame.setTitle("Sands Launcher " + SharedData.VERSION);
		frame.setSize(725, 525);
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
		
		
		URL imgURL = getClass().getResource("/launcher/assets/icon64.png");
		ImageIcon ii = new ImageIcon(imgURL); //Toolkit.getDefaultToolkit().getImage(ii)
		frame.setIconImage(ii.getImage()); 
		

		launcherPanel = new LauncherPanel();
		launcherPanel.getSidebar().setAlignmentY(Component.BOTTOM_ALIGNMENT);
		launcherPanel.getSidebar().setAlignmentX(Component.CENTER_ALIGNMENT);
		setLookAndFeel();

		frame.getContentPane().add(launcherPanel);

		// frame.pack();
		frame.setVisible(true);

		try {
			fileClient = new FileClient(SharedData.SERVERIP, SharedData.SERVERPORT);
			
			new Thread(fileClient).start();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	

		while (true) {
			update();
		}
	}

	public static String getCheckSum() {
		try {
			File file = new File(SharedData.PATH_TO_CLIENT_JAR
					+ "sandsofosiris.jar");
			HashCode hc = Files.hash(file, Hashing.sha1());
			return hc.toString();
		} catch (Exception e) {
			return CHECKSUM_FAIL_MESSAGE;
		}
	}

	boolean readyToLaunch = false;

	private void update() {

		if (!readyToLaunch) {

			if (fileClient != null) {
				
				if (fileClient.isFinished()) {
					readyToLaunch = true;
					getPanel().getSidebar().setReadyToLaunch();
					getPanel().getSidebar().getProgressBar().setVisible(false);
				} else {
					
					if(fileClient.isErrored()){
						
						getPanel().getSidebar().setReadyToLaunchOffline();
						
					}else{

						if(fileClient.isDownloading()){
					
					getPanel().getSidebar().getProgressBar().setVisible(true);
					getPanel().getSidebar().getProgressBar().setValue((int) (fileClient.getProgress() * 100));
					getPanel().getSidebar().getProgressBar().setStringPainted(true);
					getPanel().getSidebar().getProgressBar().setString((fileClient.getCurrentByteCount()/1024) + "k / " + (fileClient.getTotalByteCount()/1024)+"k"  );
					
					
					System.out.println("setting progress to "
							+ fileClient.getProgress() * 100);
						}
						
						
						
					}
				}

			}

		}

		getPanel().getSidebar().getStatusLabel()
				.setStatus(fileClient.getStatus());

		
		customLogger.update();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception f) {
				f.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public static LauncherPanel getPanel() {
		return launcherPanel;

	}

	public static void println(String s) {
		System.out.println(s);
		getPanel().getTabPanel().getConsole().print(s);
	}

	public static void setGameUpToDate() {

		Launcher.getPanel().getSidebar().getLaunchButton().setVisible(true);

	}

	static CustomLogger customLogger = new CustomLogger();
	public static CustomLogger getLogger() {		
		return customLogger;
	}
	
	
}
