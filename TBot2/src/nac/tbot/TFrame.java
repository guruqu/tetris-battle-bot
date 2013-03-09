package nac.tbot;


import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;



public class TFrame extends JFrame {
	private final TPanel panel = new TPanel();
	private final JButton button = new JButton();
	public final Dimension d = new Dimension(10 * 18 + 200, 20 * 18 + 50);
	public final JCheckBox showImage = new JCheckBox();
	private boolean tsupport = true;
	
	public boolean isTsupport() {
		return tsupport;
	}

	public void setTsupport(boolean tsupport) {
		this.tsupport = tsupport;
	}

	public TFrame() {
		super("T-Bot");
		setSize(d);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Timer t = new Timer(30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.repaint();

			}
		});
		
		button.setText("Set location...");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(panel.isSetLoc()){
					button.setText("Set location...");
					panel.resetLoc();
					if(tsupport){
						TFrame.this.setOpacity(0.55f);
					}
					t.stop();
				}else{
					button.setText("Location set...");
					if(tsupport){
						panel.setLoc();
						TFrame.this.setOpacity(1f);
					}else{
						panel.setLoc(getLocationOnScreen());
					}
					t.start();
				}
			}
		});
		
	
		
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(panel);
		getContentPane().add(button);
	}

	public static void main(String[] args) {
		// Determine if the GraphicsDevice supports translucency.
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		// If translucent windows aren't supported, exit.
		if (gd.isWindowTranslucencySupported(TRANSLUCENT)) {
			JFrame.setDefaultLookAndFeelDecorated(true);
			final TFrame tf = new TFrame();
			tf.setTsupport(true);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					
					// Set the window to 55% opaque (45% translucent).
					tf.setOpacity(0.55f);

					// Display the window.
					tf.setVisible(true);
				}
			});
		}else{
			
			final TFrame tf = new TFrame();
			tf.setTsupport(false);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Display the window.
					tf.setVisible(true);
				}
			});
		}
		
	}
	
}