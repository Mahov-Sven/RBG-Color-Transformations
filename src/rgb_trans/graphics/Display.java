package rgb_trans.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
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
	
	private ColorImageFrame imageFrame;
	private JPanel sideBar;
	private JButton brightnessButton;
	private JButton luminanceButton;
	private JButton saturationButton;
	private JButton offsetButton;
	private JButton rotationButton;
	
	
	
	public Display(int WIDTH, int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void drawMain(){
		imageFrame = new ColorImageFrame(3*WIDTH/4, HEIGHT);
		
		//Creates a sidbar using a JPanel that is 1/4 of the width and the full height.
		sideBar = new JPanel();
		sideBar.setLayout(new GridLayout(10,1));
		sideBar.setBackground(new Color(19, 23, 31));
		sideBar.setPreferredSize(new Dimension(WIDTH/4, HEIGHT));
		
		//Creates the buttons and adds them to the sidebar.
		brightnessButton = new JButton("Change Brightness");
		luminanceButton = new JButton("Change Luminance");
		saturationButton = new JButton("Change Saturation");
		offsetButton = new JButton("Change Offset");
		rotationButton = new JButton("Change Rotation");

		sideBar.add(brightnessButton);
		sideBar.add(luminanceButton);
		sideBar.add(saturationButton);
		sideBar.add(offsetButton);
		sideBar.add(rotationButton);
		
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
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
