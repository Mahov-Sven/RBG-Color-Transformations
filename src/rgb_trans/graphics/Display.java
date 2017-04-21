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
		sideBar = new JPanel();
		sideBar.setLayout(new GridLayout(10,1));
		sideBar.setBackground(Color.darkGray);
		sideBar.setPreferredSize(new Dimension(WIDTH/4, HEIGHT));

		brightnessButton = new JButton("Change Brightness");
		luminanceButton = new JButton("Change Luminance");
		saturationButton = new JButton("Change Saturation");
		offsetButton = new JButton("Change Offset");
		rotationButton = new JButton("Change Rotation");
		
		Dimension buttonSize = new Dimension(WIDTH/4, HEIGHT/10);
		
		brightnessButton.setSize(buttonSize);
		luminanceButton.setSize(buttonSize);
		saturationButton.setSize(buttonSize);
		offsetButton.setSize(buttonSize);
		rotationButton.setSize(buttonSize);
		
		sideBar.add(brightnessButton);
		sideBar.add(luminanceButton);
		sideBar.add(saturationButton);
		sideBar.add(offsetButton);
		sideBar.add(rotationButton);
		
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
		frame.add(sideBar, BorderLayout.LINE_END);
		frame.setLocationByPlatform(true);
		//frame.pack();
		frame.setVisible(true);
		
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
