package rgb_trans.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import rgb_trans.graphics.DrawCanvas;
import rgb_trans.graphics.Mesh2D;
import rgb_trans.graphics.PixelVertex;

public class TestMainSven {
	
	private static final int UPS = 60;
	private static final int WIDTH = 1500;
	private static final int HEIGHT = 1000;
	private static final long NANO = 1000000000;
	
	private static DrawCanvas canvas;
	
	private static Mesh2D mesh;

	public static void main(String... args){
		canvas = new DrawCanvas(WIDTH, HEIGHT, Color.BLACK);
		
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		
		PixelVertex p1 = new PixelVertex(0, 0);
		PixelVertex p2 = new PixelVertex(100, 0);
		PixelVertex p3 = new PixelVertex(200, 400);
		PixelVertex p4 = new PixelVertex(0, 600);
		
		mesh = new Mesh2D(new PixelVertex(0,0), p1, p2, p3, p4);
		
		run();
	}
	
	private static int iteration = 0;
	
	public static void update(){
		PixelVertex meshPos = mesh.getPos();
		meshPos.x = (meshPos.x + iteration) % 500;
		meshPos.y = (meshPos.y + 2 * iteration) % 400;
		mesh.setPos(meshPos);
	}
	
	public static void render(){
		canvas.ready();
		canvas.clear();
		canvas.drawPoly(mesh, Color.BLUE);
		canvas.cleanUp();
		iteration++;
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
