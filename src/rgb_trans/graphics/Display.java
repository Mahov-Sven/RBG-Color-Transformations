package rgb_trans.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rgb_trans.util.math.ColorTransformationMaths;
import rgb_trans.util.math.Mat4f;
import rgb_trans.util.math.Vec4f;

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
	
	private static Color backgroundColor = new Color(1, 7, 18);
	private static Color backgroundColor1 = new Color(19, 23, 31);
	private static Color borderColor = new Color(28, 31, 38);
	private static Color borderColor1 = new Color(36, 38, 45);
	private static Color textColor = new Color(150, 18, 39);
	
	private ColorImageFrame imageFrame;
	private ColorGraphFrame graphFrame;
	private JPanel sideBar;
	private JPanel buttonContainer;
	private JLabel buttonHeader;
	private JButton brightnessButton;
	private JButton luminanceButton;
	private JButton saturationButton;
	private JButton offsetButton;
	private JButton rotationButton;
	private JButton resetButton;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem openFileItem;
	private JMenuItem centerImageItem;
	private int[] brightnessSliders = {50, 50, 50};
	
	private int screen = 0;
	
	//private Dimension sideBarDefaultWidth = 
	
	public Display(int WIDTH, int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void drawMain(){
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		openFileItem = new JMenuItem("Open Image");
		openFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser openDialog = new JFileChooser();
				if(openDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					try{
						BufferedImage img = ImageIO.read(openDialog.getSelectedFile());
						int pixels[] = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
						imageFrame.setBasePixels(pixels, img.getWidth(), img.getHeight());
					} catch (IOException ex){
						ex.printStackTrace();
					}
				}
			}
		});
		
		centerImageItem = new JMenuItem("Center Image");
		centerImageItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				imageFrame.centerPosition();
			}
		});
		menu.add(openFileItem);
		menu.add(centerImageItem);
		menuBar.add(menu);
		
		imageFrame = new ColorImageFrame(3*WIDTH/4, HEIGHT, backgroundColor, this);
		
		graphFrame = new ColorGraphFrame(HEIGHT, HEIGHT);
		graphFrame.setPreferredSize(new Dimension(0, 0));
		
		//Creates a sidbar using a JPanel that is 1/4 of the width and the full height.
		sideBar = new JPanel();
		sideBar.setPreferredSize(new Dimension(WIDTH/4, HEIGHT));
		sideBar.setBackground(backgroundColor1);
		
		buttonHeader = new JLabel("Color Transformations");
		buttonHeader.setHorizontalTextPosition(JLabel.CENTER);
		buttonHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
		buttonHeader.setForeground(textColor);
		
		buttonContainer = new JPanel();
		buttonContainer.setBackground(backgroundColor1);
		buttonContainer.setPreferredSize(new Dimension(WIDTH/4, 31*HEIGHT/32));
		
		
		//Creates the buttons and adds them to the sidebar.
		brightnessButton = new JButton("Change Brightness");
		luminanceButton = new JButton("Change Luminance");
		saturationButton = new JButton("Change Saturation");
		offsetButton = new JButton("Change Offset");
		rotationButton = new JButton("Change Rotation");
		resetButton = new JButton("Reset");
		
		brightnessButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		luminanceButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		saturationButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		offsetButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		rotationButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		resetButton.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/10));
		
		brightnessButton.setForeground(textColor);
		luminanceButton.setForeground(textColor);
		saturationButton.setForeground(textColor);
		offsetButton.setForeground(textColor);
		rotationButton.setForeground(textColor);
		resetButton.setForeground(textColor);
		
		brightnessButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 brightnessPress();
			 }
		});
		luminanceButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 luminancePress();
			 }
		});
		saturationButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 saturationPress();
			 }
		});
		offsetButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 offsetPress();
			 }
		});
		rotationButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 rotationPress();
			 }
		});
		resetButton.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 reset();
			 }
		});
		
		brightnessButton.setName("brightness");
		luminanceButton.setName("luminance");
		saturationButton.setName("saturation");
		offsetButton.setName("offset");
		rotationButton.setName("rotation");
		resetButton.setName("reset");
		
		buttonContainer.add(brightnessButton, BorderLayout.CENTER);
		buttonContainer.add(luminanceButton, BorderLayout.CENTER);
		buttonContainer.add(saturationButton, BorderLayout.CENTER);
		buttonContainer.add(offsetButton, BorderLayout.CENTER);
		buttonContainer.add(rotationButton, BorderLayout.CENTER);
		buttonContainer.add(resetButton, BorderLayout.CENTER);
		
		sideBar.add(buttonHeader, BorderLayout.PAGE_START);
		sideBar.add(buttonContainer);
		
		
		frame.setJMenuBar(menuBar);
		frame.add(graphFrame);
		frame.add(imageFrame);
		frame.add(sideBar, BorderLayout.LINE_END);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
		
		int[] pixels = {6556160, 2660, 680960, 0};
		imageFrame.setBasePixels(pixels, 2, 2);
		
		run();
	}
	
	private void storeSliderValues(String sliderText, int sliderValue){
		if (sliderText == "Red:"){
			brightnessSliders[0] = sliderValue;
		}else if (sliderText == "Green:"){
			brightnessSliders[1] = sliderValue;
		}else if (sliderText == "Blue:"){
			brightnessSliders[2] = sliderValue;
		}
	}
	
	private int getSliderValues(String sliderText){
		if (sliderText == "Red:"){
			return brightnessSliders[0];
		}else if (sliderText == "Green:"){
			return brightnessSliders[1];
		}else if (sliderText == "Blue:"){
			return brightnessSliders[2];
		}else{
			return 50;
		}
	}
	
	public ColorImageFrame getImageFrame(){
		return imageFrame;
	}
	
	private JPanel createColorSlider(String text){
		JPanel colorPanel = new JPanel();
		colorPanel.setBackground(backgroundColor1);
		colorPanel.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/25));
		colorPanel.setLayout(new GridLayout(0, 3));
		JLabel label = new JLabel(text);
		label.setForeground(textColor);
		label.setHorizontalAlignment(JLabel.RIGHT);
		JSlider slider = new JSlider(0, 100, getSliderValues(text));
		slider.setBackground(backgroundColor1);
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				storeSliderValues(label.getText(), slider.getValue());
				brightnessChange();
			}
		});
		colorPanel.add(label);
		colorPanel.add(slider);
		return colorPanel;
	}
	
	private void brightnessChange(){
		Mat4f transformation = ColorTransformationMaths.brightnessMatrix(brightnessSliders[0]/50f, brightnessSliders[1]/50f, brightnessSliders[2]/50f);
		int[] pixelArray = imageFrame.getBasePixels().clone();
		for (int i = 0; i<pixelArray.length; i++){
			Vec4f color = ColorTransformationMaths.loadColorToVec4f(new Color(pixelArray[i]));
			color = Mat4f.mul(transformation, color);
			pixelArray[i] = ColorTransformationMaths.vec4fToColor(color).getRGB();
		}
		imageFrame.setPixels(pixelArray);
	}
	
	private void brightnessPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "brightness" && buttonContainer.getComponent(i + 1).getName() != null){
				JPanel panel = new JPanel();
				panel.setBackground(backgroundColor1);
				panel.setPreferredSize(new Dimension(WIDTH/4, 3 * HEIGHT/24));
				JPanel redPanel = createColorSlider("Red:");
				JPanel greenPanel = createColorSlider("Green:");
				JPanel bluePanel = createColorSlider("Blue:");
				panel.add(redPanel, BorderLayout.CENTER);
				panel.add(greenPanel, BorderLayout.CENTER);
				panel.add(bluePanel, BorderLayout.CENTER);
				buttonContainer.add(panel, i+1);
				break;
			}else if(buttonContainer.getComponent(i).getName() == "brightness" && buttonContainer.getComponent(i + 1).getName() == null){
				buttonContainer.remove(i+1);
				break;
			}
		}
		buttonContainer.validate();
	}
	
	private void luminancePress(){
		Mat4f transformation = ColorTransformationMaths.luminanceMatrix();
		int[] pixelArray = imageFrame.getBasePixels().clone();
		for (int i = 0; i<pixelArray.length; i++){
			Vec4f color = ColorTransformationMaths.loadColorToVec4f(new Color(pixelArray[i]));
			color = Mat4f.mul(transformation, color);
			pixelArray[i] = ColorTransformationMaths.vec4fToColor(color).getRGB();
		}
		imageFrame.setPixels(pixelArray);
	}
	
	private void saturationPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "saturation" && buttonContainer.getComponent(i + 1).getName() != null){
				buttonContainer.add(new JPanel(), i+1);
				buttonContainer.getComponent(i+1).setPreferredSize(new Dimension(WIDTH/4, HEIGHT/5));
				break;
			}else if(buttonContainer.getComponent(i).getName() == "saturation" && buttonContainer.getComponent(i + 1).getName() == null){
				buttonContainer.remove(i+1);
				break;
			}
		}
		buttonContainer.validate();
	}
	
	private void offsetPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "offset" && buttonContainer.getComponent(i + 1).getName() != null){
				buttonContainer.add(new JPanel(), i+1);
				buttonContainer.getComponent(i+1).setPreferredSize(new Dimension(WIDTH/4, HEIGHT/5));
				break;
			}else if(buttonContainer.getComponent(i).getName() == "offset" && buttonContainer.getComponent(i + 1).getName() == null){
				buttonContainer.remove(i+1);
				break;
			}
		}
		buttonContainer.validate();
	}
	
	private void rotationPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "rotation" && buttonContainer.getComponent(i + 1).getName() != null){
				buttonContainer.add(new JPanel(), i+1);
				buttonContainer.getComponent(i+1).setPreferredSize(new Dimension(WIDTH/4, HEIGHT/5));
				break;
			}else if(buttonContainer.getComponent(i).getName() == "rotation" && buttonContainer.getComponent(i + 1).getName() == null){
				buttonContainer.remove(i+1);
				break;
			}
			buttonContainer.validate();
		}
		buttonContainer.validate();
	}
	
	private void reset(){
		 brightnessSliders[0] = 50;
		 brightnessSliders[1] = 50;
		 brightnessSliders[2] = 50;
		 brightnessChange();
		 if(buttonContainer.getComponent(1).getName() != "luminance"){
			 ((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(1)).getComponent(1)).getComponent(1)).setValue(50);
			 ((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(1)).getComponent(0)).getComponent(1)).setValue(50);
			 ((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(1)).getComponent(2)).getComponent(1)).setValue(50);
		 }
	}
	
	
	private void run(){
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
	
	public void toggleScreen(int nextScreen){
		switch(nextScreen){
		/* To the Image Screen */
		case 0:
			imageFrame.setSize(3*WIDTH/4, HEIGHT);
			graphFrame.setSize(0, 0);
			break;
		/* To the Graph Screen */
		case 1:
			imageFrame.setSize(WIDTH / 2, HEIGHT / 2);
			graphFrame.setSize(WIDTH / 2, HEIGHT / 2);
			break;
		}
		screen = nextScreen;
	}
}
