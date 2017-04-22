package rgb_trans.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

public class ColorImageFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int BORDER_SIZE = 10;
	private static final int SELECTED_BORDER_SIZE = 8;

	private int[] pixels;
	private int arrayWidth, arrayHeight;
	
	private int pixelSize;
	private int windowX, windowY;
	private int selected;

	public ColorImageFrame(int width, int height) {
		this.pixels = new int[0];
		this.arrayWidth = 0;
		this.arrayHeight = 0;
		this.pixelSize = 32;
		this.windowX = 100;
		this.windowY = 100;
		this.selected = -1;

		setPreferredSize(new Dimension(width, height));

		MouseAdapter mouseAdapter = new MouseAdapter() {
			private boolean dragged = false;
			private int startX, startY;

			public void mouseClicked(MouseEvent e) {
				clickSelected(e.getX(), e.getY());
			}

			public void mouseDragged(MouseEvent e) {
				if (dragged) {
					moveImage(e.getX() - startX, e.getY() - startY);
					startX = e.getX();
					startY = e.getY();
				}
			}

			public void mousePressed(MouseEvent e) {
				if (e.getX() < windowX - BORDER_SIZE || e.getY() < windowY - BORDER_SIZE
						|| e.getX() > windowX + BORDER_SIZE + arrayWidth * pixelSize
						|| e.getY() > windowY + BORDER_SIZE + arrayHeight * pixelSize)
					return;

				dragged = true;
				startX = e.getX();
				startY = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				dragged = false;
			}

			public void mouseWheelMoved(MouseWheelEvent e) {
				zoomImage(e.getWheelRotation(), e.getX(), e.getY());
			}
		};

		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}
	
	public void centerPosition(){
		this.windowX = this.getWidth()/2;
		this.windowY = this.getHeight()/2;
		
		repaint();
	}
	
	private void clickSelected(int x, int y){
		int pixelX = selected % arrayWidth;
		int pixelY = selected / arrayHeight;

		if (x < windowX || y < windowY || x > windowX + arrayWidth * pixelSize
				|| y > windowY + arrayWidth * pixelSize
				|| (x > pixelX && y > pixelY && x < pixelX + pixelSize && y < pixelY + pixelSize)) {
			selected = -1;
		} else {
			x -= windowX;
			y -= windowY;
			x /= pixelSize;
			y /= pixelSize;
			
			selected = y * arrayWidth + x;
		}
		
		repaint();
	}
	
	public Color getSelectedColor(){
		return new Color(pixels[selected]);
	}
	
	public int getSelectedX(){
		return selected % arrayWidth;
	}
	
	public int getSelectedY(){
		return selected / arrayHeight;
	}
	
	public void moveImage(int deltaX, int deltaY) {
		windowX += deltaX;
		windowY += deltaY;

		repaint();
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(1, 7, 18));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.GRAY);
		g.fillRect(windowX - BORDER_SIZE, windowY - BORDER_SIZE, arrayWidth * pixelSize + 2 * BORDER_SIZE,
				arrayHeight * pixelSize + 2 * BORDER_SIZE);

		for (int y = 0; y < arrayHeight; y++) {
			for (int x = 0; x < arrayWidth; x++) {
				if(selected == y * arrayWidth + x)
					continue;
				
				g.setColor(new Color(pixels[y * arrayWidth + x]));
				g.fillRect(windowX + x * pixelSize, windowY + y * pixelSize, pixelSize, pixelSize);
			}
		}
		
		if(selected >= 0){
			int x = selected % arrayWidth;
			int y = selected / arrayHeight;
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(windowX - SELECTED_BORDER_SIZE + x * pixelSize, windowY - SELECTED_BORDER_SIZE + y * pixelSize, pixelSize + 2 * SELECTED_BORDER_SIZE, pixelSize + 2 * SELECTED_BORDER_SIZE);
		
			g.setColor(new Color(pixels[selected]));
			g.fillRect(windowX + x * pixelSize, windowY + y * pixelSize, pixelSize, pixelSize);
		}
	}

	public boolean pixelSelected(){
		return selected > -1 ? true : false;
	}

	public void setPixel(int x, int y, Color color) {
		pixels[y * arrayWidth + x] = (color.getRed() << 16 + color.getGreen() << 8 + color.getBlue());

		repaint();
	}

	public void setPixels(int[] pixels, int width, int height) {
		this.selected = -1;
		this.pixels = pixels;
		this.arrayWidth = width;
		this.arrayHeight = height;

		repaint();
	}

	public void zoomImage(int deltaZ, int x, int y) {
		deltaZ *= -1;

		if ((pixelSize <= 1 && deltaZ < 0) || (pixelSize >= 256 && deltaZ > 0))
			return;

		/* Moving window to proper location */
		x -= windowX;

		if (x < 0)
			x = 0;

		if (x > arrayWidth * pixelSize)
			x = arrayWidth * pixelSize;

		y -= windowY;

		if (y < 0)
			y = 0;

		if (y > arrayHeight * pixelSize)
			y = arrayHeight * pixelSize;

		windowX -= x * (3 * deltaZ + 1) / 4;
		windowY -= y * (3 * deltaZ + 1) / 4;

		/* Changing Pixel Size */
		pixelSize *= Math.pow(2, deltaZ);

		repaint();
	}
}
