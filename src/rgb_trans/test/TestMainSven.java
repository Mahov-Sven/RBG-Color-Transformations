package rgb_trans.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

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
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		
		JMenuItem openFileItem = new JMenuItem("Open Image");
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
		
		menu.add(openFileItem);
		menuBar.add(menu);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
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
