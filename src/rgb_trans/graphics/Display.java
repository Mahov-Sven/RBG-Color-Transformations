package rgb_trans.graphics;

import java.awt.Color;

import javax.swing.JFrame;

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
	
	private static DrawCanvas canvas;
	
	public Display(int WIDTH, int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void drawMain(){
		canvas = new DrawCanvas(this.WIDTH, this.HEIGHT, Color.BLACK);
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		run();
	}
	
	private static void render(){
		canvas.ready();
		canvas.clear();
		canvas.cleanUp();
	}
	
	private static void run(){
		boolean running = true;
		long lastUpTime = System.nanoTime();
		long lastSecTime = System.nanoTime();
		int ups = 0;
		while(running){
			if(System.nanoTime() - lastUpTime > NANO/UPS){
				render();
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
