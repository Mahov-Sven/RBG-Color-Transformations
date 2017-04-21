package rgb_trans.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ColorGraphFrame extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private Color color = Color.BLACK;
	private Arrow redAxis, greenAxis, blueAxis, colorArrow;
	
	private static final int RED_ANGLE = 350;
	private static final int GREEN_ANGLE = 230;
	private static final int BLUE_ANGLE = 90;

	public ColorGraphFrame(int width, int height) {
		setPreferredSize(new Dimension(width, height));
	}
	
	public void setColor(Color color){
		this.color = color;
		repaint();
	}
	
	public void update(){
		int numb = getWidth() < getHeight() ? getWidth() : getHeight();
		int length = (int) (numb * 0.45d);
		int width = length/25;
		
		redAxis = new Arrow(length, width, RED_ANGLE, 0.08d, 0.4d, Color.RED);
		greenAxis = new Arrow((int) (length * .96), width, GREEN_ANGLE, 0.08d, 0.4d, Color.GREEN);
		blueAxis = new Arrow(length, width, BLUE_ANGLE, 0.08d, 0.4d, Color.BLUE);
		colorArrow = calculateColorArrow(length, width, 0.08d, 0.4d, color);
		
		
		redAxis.setPos(new PixelVertex(getWidth() / 2, getHeight() / 2));
		greenAxis.setPos(new PixelVertex(getWidth() / 2, getHeight() / 2));
		blueAxis.setPos(new PixelVertex(getWidth() / 2, getHeight() / 2));
		colorArrow.setPos(new PixelVertex(getWidth() / 2, getHeight() / 2));
	}
	
	private static Arrow calculateColorArrow(int length, int width, double headLengthPercent, double headWidthPercent, Color color){
		double y = (color.getRed() / 256d * Math.sin(Math.toRadians(RED_ANGLE))) + 
				   (color.getGreen() / 256d * Math.sin(Math.toRadians(GREEN_ANGLE))) + 
				   (color.getBlue() / 256d * Math.sin(Math.toRadians(BLUE_ANGLE)));
		
		double x = (color.getRed() / 256d * Math.cos(Math.toRadians(RED_ANGLE))) + 
				   (color.getGreen() / 256d * Math.cos(Math.toRadians(GREEN_ANGLE))) + 
				   (color.getBlue() / 256d * Math.cos(Math.toRadians(BLUE_ANGLE)));
		
		int distance = (int) (Math.sqrt(x * x + y * y) * length);
		double angle = Math.toDegrees(Math.atan(y / x));
		
		if(x < 0)
			angle += 180;
		
		return new Arrow(distance, width, angle, headLengthPercent, headWidthPercent, color);
	}
	
	public void paintComponent(Graphics g){
		update();
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		drawMesh(g, redAxis.getMesh(), redAxis.getColor());
		drawMesh(g, blueAxis.getMesh(), blueAxis.getColor());
		drawMesh(g, greenAxis.getMesh(), greenAxis.getColor());
		drawMesh(g, colorArrow.getMesh(), colorArrow.getColor());
	}
	
	public void drawMesh(Graphics g, Mesh2D mesh, Color color){
		g.setColor(color);
		g.fillPolygon(mesh.getXVerteces(), mesh.getYVerteces(), mesh.length());
	}
}
