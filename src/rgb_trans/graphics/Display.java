package rgb_trans.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Display {

	/*
	 * TODO
	 * should contain all of the
	 * components of the program.
	 */
	private static final int UPS = 60;
	private int WIDTH;
	private int HEIGHT;
	private static final long NANO = 1000000000;
	
	private static Color backgroundColor = new Color(1, 7, 18);
	private static Color backgroundColor1 = new Color(19, 23, 31);
	private static Color borderColor = new Color(28, 31, 38);
	private static Color borderColor1 = new Color(36, 38, 45);
	private static Color textColor = new Color(150, 18, 39);
	
	private ColorImageFrame imageFrame;
	private JPanel sideBar;
	private JPanel buttonContainer;
	private JLabel buttonHeader;
	private JButton brightnessButton;
	private JButton luminanceButton;
	private JButton saturationButton;
	private JButton offsetButton;
	private JButton rotationButton;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem openFileItem;
	private JMenuItem centerImageItem;
	
	public Display(int WIDTH, int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void drawMain(){
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		openFileItem = new JMenuItem("Open Image");
		openFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser openDialog = new JFileChooser();
				if(openDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					try{
						BufferedImage img = ImageIO.read(openDialog.getSelectedFile());
						int pixels[] = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
						imageFrame.setPixels(pixels, img.getWidth(), img.getHeight());
					} catch (IOException ex){
						ex.printStackTrace();
					}
				}
			}
		});
		
		centerImageItem = new JMenuItem("Center Image");
		centerImageItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				imageFrame.centerPosition();
			}
		});
		menu.add(openFileItem);
		menu.add(centerImageItem);
		menuBar.add(menu);
		
		imageFrame = new ColorImageFrame(3*WIDTH/4, HEIGHT, backgroundColor);
		
		//Creates a sidbar using a JPanel that is 1/4 of the width and the full height.
		sideBar = new JPanel();
		sideBar.setPreferredSize(new Dimension(WIDTH/4, HEIGHT));
		sideBar.setBackground(backgroundColor1);
		
		buttonHeader = new JLabel("Color Transformations");
		buttonHeader.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/32));
		
		buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(10,1));
		buttonContainer.setBackground(backgroundColor1);
		buttonContainer.setPreferredSize(new Dimension(WIDTH/4, 31*HEIGHT/32));
		
		
		//Creates the buttons and adds them to the sidebar.
		brightnessButton = new JButton("Change Brightness");
		luminanceButton = new JButton("Change Luminance");
		saturationButton = new JButton("Change Saturation");
		offsetButton = new JButton("Change Offset");
		rotationButton = new JButton("Change Rotation");
		
		buttonContainer.add(brightnessButton);
		buttonContainer.add(luminanceButton);
		buttonContainer.add(saturationButton);
		buttonContainer.add(offsetButton);
		buttonContainer.add(rotationButton);
		
		sideBar.add(buttonHeader, BorderLayout.PAGE_START);
		sideBar.add(buttonContainer);
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
		frame.setJMenuBar(menuBar);
		frame.add(imageFrame);
		frame.add(sideBar, BorderLayout.LINE_END);
		frame.setLocationByPlatform(true);
		//frame.pack();
		frame.setVisible(true);
		
		int[] pixels = {6556160, 2660, 680960, 0};
		imageFrame.setPixels(pixels, 2, 2);
		run();
	}

	private static void run(){
		boolean running = true;
		long lastUpTime = System.nanoTime();
		long lastSecTime = System.nanoTime();
		int ups = 0;
		while(running){
			if(System.nanoTime() - lastUpTime > NANO/UPS){
				lastUpTime = System.nanoTime();
				ups++;
			}
			if(System.nanoTime() - lastSecTime > NANO){
				//System.out.println(ups);
				lastSecTime = System.nanoTime();
				ups = 0;
			}
		}
	}
}
