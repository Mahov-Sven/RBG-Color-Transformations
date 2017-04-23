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

	private int[] basePixels;
	private int[] pixels;
	private int arrayWidth, arrayHeight;

	private int pixelSize;
	private int windowX, windowY;
	private int selected;
	private Color backgroundColor;

	public ColorImageFrame(int width, int height, Color backgroundColor) {
		this.pixels = new int[0];
		this.arrayWidth = 0;
		this.arrayHeight = 0;
		this.pixelSize = 32;
		this.windowX = 100;
		this.windowY = 100;
		this.selected = -1;
		this.backgroundColor = backgroundColor;

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

	public void centerPosition() {
		this.windowX = this.getWidth() / 2 - arrayWidth * pixelSize / 2;
		this.windowY = this.getHeight() / 2 - arrayHeight * pixelSize / 2;

		repaint();
	}

	private void clickSelected(int x, int y) {
		int pixelX = selected % arrayWidth;
		int pixelY = selected / arrayHeight;
		System.out.println("pixelX: " + pixelX + ", x:" + x);
		if (x < windowX || y < windowY || x > windowX + arrayWidth * pixelSize || y > windowY + arrayWidth * pixelSize
				|| (x > windowX + pixelX * pixelSize && y > windowY + pixelY * pixelSize
						&& x < windowX + (pixelX + 1) * pixelSize && y < windowY + (pixelY + 1) * pixelSize)) {
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

	public Color getSelectedColor() {
		return new Color(pixels[selected]);
	}

	public int getSelectedX() {
		return selected % arrayWidth;
	}

	public int getSelectedY() {
		return selected / arrayHeight;
	}
	
	public int[] getBasePixels(){
		return basePixels;
	}

	public int[] getPixels() {
		return pixels;
	}

	public void moveImage(int deltaX, int deltaY) {
		windowX += deltaX;
		windowY += deltaY;

		repaint();
	}

	public void paintComponent(Graphics g) {
		g.setColor(this.backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.GRAY);
		g.fillRect(windowX - BORDER_SIZE, windowY - BORDER_SIZE, arrayWidth * pixelSize + 2 * BORDER_SIZE,
				arrayHeight * pixelSize + 2 * BORDER_SIZE);

		for (int y = 0; y < arrayHeight; y++) {
			for (int x = 0; x < arrayWidth; x++) {
				if (selected == y * arrayWidth + x || windowX + (x + 1) * pixelSize < 0
						|| windowY + (y + 1) * pixelSize < 0 || windowX + x * pixelSize > getWidth()
						|| windowY + y * pixelSize > getHeight())
					continue;

				g.setColor(new Color(pixels[y * arrayWidth + x]));
				g.fillRect(windowX + x * pixelSize, windowY + y * pixelSize, pixelSize, pixelSize);
			}
		}

		if (selected >= 0) {
			int x = selected % arrayWidth;
			int y = selected / arrayHeight;

			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(windowX - SELECTED_BORDER_SIZE + x * pixelSize, windowY - SELECTED_BORDER_SIZE + y * pixelSize,
					pixelSize + 2 * SELECTED_BORDER_SIZE, pixelSize + 2 * SELECTED_BORDER_SIZE);

			g.setColor(new Color(pixels[selected]));
			g.fillRect(windowX + x * pixelSize, windowY + y * pixelSize, pixelSize, pixelSize);
		}
	}

	public boolean pixelSelected() {
		return selected > -1 ? true : false;
	}

	public void setPixel(int x, int y, Color color) {
		pixels[y * arrayWidth + x] = (color.getRed() << 16 + color.getGreen() << 8 + color.getBlue());

		repaint();
	}
	
	public void setBasePixels(int[] pixels, int width, int height){
		this.selected = -1;
		this.basePixels = pixels;
		this.arrayWidth = width;
		this.arrayHeight = height;
		
		setPixels(pixels);
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;

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
