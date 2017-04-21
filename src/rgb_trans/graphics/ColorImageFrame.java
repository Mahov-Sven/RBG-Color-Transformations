package rgb_trans.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

public class ColorImageFrame extends JPanel{

	private static final long serialVersionUID = 1L;
	private int[] pixels;
	private int width, height;
	
	private int pixelWidth;
	private int windowX, windowY;
	private int selected;

	public ColorImageFrame(int width, int height) {
		this.pixels = new int[0];
		this.width = 0;
		this.height = 0;
		this.pixelWidth = 32;
		this.windowX = 100;
		this.windowY = 100;
		this.selected = -1;
		
		setPreferredSize(new Dimension(width, height));
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			private boolean dragged = false;
			private int startX, startY;
			
            public void mouseDragged(MouseEvent e){
            	System.out.println("hello");
            	if(!dragged){
            		startX = e.getX();
            		startY = e.getY();
            		dragged = true;
            	}
            	moveImage(e.getX() - startX, e.getY() - startY);
            	startX = e.getX();
        		startY = e.getY();
            }
            
            public void mouseReleased(MouseEvent e){
            	dragged = false;
            	System.out.println("hello");
            }
            
            public void mouseWheelMoved(MouseWheelEvent e){
            	zoomImage(e.getWheelRotation());
            }
        };
		
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}
	
	public void zoomImage(int deltaZ){
		double deltaZoom = Math.pow(2, deltaZ);
    	pixelWidth *= deltaZoom;
    	
    	if(pixelWidth < 1)
    		pixelWidth = 1;
    	
    	if(pixelWidth > 256)
    		pixelWidth = 256;
    	
    	repaint();
	}
	
	public void moveImage(int deltaX, int deltaY){
		this.windowX += deltaX;
		this.windowY += deltaY;
		System.out.println("working");
		repaint();
	}
	
	public void setPixels(int[] pixels, int width, int height){
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		
		repaint();
	}
	
	public void setPixel(int x, int y, Color color){
		pixels[y * width + x] = (color.getRed() << 16 + color.getGreen() << 8 + color.getBlue());
		
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				g.setColor(new Color(pixels[y * width + x]));
				g.fillRect(windowX + x * pixelWidth, windowY + y * pixelWidth, pixelWidth, pixelWidth);
			}
		}
	}
}
