package rgb_trans.test;

import javax.swing.JFrame;

import rgb_trans.graphics.ColorImageFrame;

public class TestMainSven {
	
	private static final int UPS = 60;
	private static final int WIDTH = 1500;
	private static final int HEIGHT = 900;
	private static final long NANO = 1000000000;
	
	private static ColorImageFrame imageFrame;

	public static void main(String... args){
		imageFrame = new ColorImageFrame(WIDTH, HEIGHT);
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(imageFrame);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
		
		int[] pixels = {6556160, 2660, 680960, 0};
		imageFrame.setPixels(pixels, 2, 2);
		
		run();
	}
	
	public static void update(){
		
	}
	
	public static void render(){
		
	}
	
	public static void run(){
		boolean running = true;
		long lastUpTime = System.nanoTime();
		long lastSecTime = System.nanoTime();
		int ups = 0;
		while(running){
			if(System.nanoTime() - lastUpTime > NANO/UPS){
				update();
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
