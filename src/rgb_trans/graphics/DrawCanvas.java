package rgb_trans.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class DrawCanvas extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected Graphics g;
	protected Color backgroundColor;
	protected int width, height;
	
	public DrawCanvas(int width, int height, Color backgroundColor){
		this.width = width;
		this.height = height;
		this.backgroundColor = backgroundColor;
		this.setPreferredSize(new Dimension(width, height));
	}
	
	public void ready(){
		g = getGraphics();
	}
	
	public void clear(){
		graphicsCreated();
		
		drawRect(0, 0, width, height, backgroundColor);
	}
	
	public void drawPixel(int x, int y, Color color){
		graphicsCreated();
		
		drawRect(x, y, x + 1, y + 1, color);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, Color color, int width){
		graphicsCreated();
		
		g.setColor(color);
		
		for(int l = 0; l < width; l++){
			int adj = (int) Math.ceil((l - width) / 2);
			g.drawLine(x1 + adj, y1 + adj, x2 + adj, y2 + adj);
		}
	}
	
	public void drawRect(int x1, int y1, int x2, int y2, Color color){
		graphicsCreated();
		
		g.setColor(color);
		g.fillRect(x1, y1, x2 - x1, y2 - y1);
	}
	
	public void drawPoly(Mesh2D mesh, Color color){
		graphicsCreated();
		
		g.setColor(color);
		g.fillPolygon(mesh.getXVerteces(), mesh.getYVerteces(), mesh.length());
	}
	
	public void cleanUp(){
		graphicsCreated();
		
		g.dispose();
	}
	
	private void graphicsCreated(){
		if(g == null)
			throw new IllegalStateException("You must ready the Canvas before rendering or disposing it.\n\tTry calling 'canvas.ready()' before rendering next time.");
	}
}
