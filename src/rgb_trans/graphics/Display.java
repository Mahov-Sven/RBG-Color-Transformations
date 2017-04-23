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
import javax.swing.BoxLayout;
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
import rgb_trans.util.math.Maths;
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
	
	private JFrame frame;
	private ColorImageFrame imageFrame;
	private ColorGraphFrame graphFrame;
	private JPanel frameContainer;
	private JPanel sideBar;
	private JPanel colorEditor;
	private JLabel colorHeader;
	private JPanel editRed;
	private JPanel editGreen;
	private JPanel editBlue;
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
	private int[] colorEditorSliders = {0, 0, 0};
	private int[] brightnessSliders = {50, 50, 50};
	private int saturationSliders = 50;
	private int[] offsetSliders = {0, 0, 0};
	private int rotationSliders = 0;
	private Mat4f[] appliedTransformations = {null, null, null, null, null};
	private int[] transformationIndeces = {-1, -1, -1, -1, -1};
	
	private int screen = 0;
	
	//private Dimension sideBarDefaultWidth = 
	
	public Display(int WIDTH, int HEIGHT){
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
	}
	
	public void drawMain(){
		frame = new JFrame("Title");
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
		
		graphFrame = new ColorGraphFrame(0, 0);
		
		//Creates a container to hold the GraphFrame and ImageFrame objects
		frameContainer = new JPanel();
		frameContainer.setLayout(new BoxLayout(frameContainer, BoxLayout.Y_AXIS));
		
		//Creates a sidebar using a JPanel used to edit the selected color
		colorEditor = new JPanel();
		colorEditor.setPreferredSize(new Dimension(0, 0));
		colorEditor.setBackground(backgroundColor1);
		
		colorHeader = new JLabel("Edit Color");
		colorHeader.setHorizontalTextPosition(JLabel.CENTER);
		colorHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
		colorHeader.setForeground(textColor);
		
		editRed = createColorEditorSlider("Red:");
		editGreen = createColorEditorSlider("Green:");
		editBlue = createColorEditorSlider("Blue:");
		
		//Creates a sidebar using a JPanel that is 1/4 of the width and the full height.
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
		
		frameContainer.add(graphFrame);
		frameContainer.add(imageFrame);
		
		colorEditor.add(colorHeader);
		colorEditor.add(editRed);
		colorEditor.add(editGreen);
		colorEditor.add(editBlue);
		
		
		frame.setJMenuBar(menuBar);
		frame.add(frameContainer, BorderLayout.LINE_START);
		frame.add(colorEditor, BorderLayout.CENTER);
		frame.add(sideBar, BorderLayout.LINE_END);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);
		
		int[] pixels = new int[64];
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = Maths.randColorInt();
		}
		
		imageFrame.setBasePixels(pixels, 8, 8);
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
		addTransformation("brightness", transformation);
		applyTransformations();
		
		if(imageFrame.pixelSelected()){
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
		}
	}
	
	private void brightnessPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "brightness" && buttonContainer.getComponent(i + 1).getName() != "brightnessPanel"){
				closePanel();
				JPanel panel = new JPanel();
				panel.setBackground(backgroundColor1);
				panel.setPreferredSize(new Dimension(WIDTH/4, 3 * HEIGHT/24));
				panel.setName("brightnessPanel");
				JPanel redPanel = createColorSlider("Red:");
				JPanel greenPanel = createColorSlider("Green:");
				JPanel bluePanel = createColorSlider("Blue:");
				panel.add(redPanel, BorderLayout.CENTER);
				panel.add(greenPanel, BorderLayout.CENTER);
				panel.add(bluePanel, BorderLayout.CENTER);
				if (buttonContainer.getComponent(i).getName() == "brightness"){
					buttonContainer.add(panel, i+1);
				}else{
					buttonContainer.add(panel, i);
				}
				break;
			}else if(buttonContainer.getComponent(i).getName() == "brightnessPanel"){
				buttonContainer.remove(i);
				break;
			}
		}
		buttonContainer.revalidate();
	}
	
	private void luminancePress(){
		closePanel();
		buttonContainer.revalidate();
		Mat4f transformation = ColorTransformationMaths.luminanceMatrix();
		int index = getTransformationIndex("luminance");
		if(transformationIndeces[index] < 0){
			addTransformation("luminance", transformation);
			applyTransformations();
		}else{
			removeTransformation("luminance");
		}
		
		if(imageFrame.pixelSelected()){
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
		}
	}
	
	private void saturationChange(){
		Mat4f transformation = ColorTransformationMaths.saturationMatrix(saturationSliders/50f);
		addTransformation("saturation", transformation);
		applyTransformations();
		
		if(imageFrame.pixelSelected()){
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
		}
	}
	
	private void saturationPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "saturation" && buttonContainer.getComponent(i + 1).getName() != "saturationPanel"){
				closePanel();
				JPanel panel = new JPanel();
				panel.setBackground(backgroundColor1);
				panel.setPreferredSize(new Dimension(WIDTH/4,HEIGHT/24));
				panel.setLayout(new GridLayout(0, 3));
				panel.setName("saturationPanel");
				JLabel label = new JLabel("Saturation");
				label.setForeground(textColor);
				label.setHorizontalAlignment(JLabel.RIGHT);
				JSlider saturationSlider = new JSlider(0, 100, saturationSliders);
				saturationSlider.setBackground(backgroundColor1);
				saturationSlider.addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent e){
						saturationSliders = saturationSlider.getValue();
						saturationChange();
					}
				});
				panel.add(label);
				panel.add(saturationSlider);
				if (buttonContainer.getComponent(i).getName() == "saturation"){
					buttonContainer.add(panel, i+1);
				}else{
					buttonContainer.add(panel, i);
				}
				break;
			}else if(buttonContainer.getComponent(i).getName() == "saturationPanel"){
				buttonContainer.remove(i);
				break;
			}
		}
		buttonContainer.revalidate();
	}
	
	private void storeOffsetValues(String sliderText, int sliderValue){
		if (sliderText == "Red:"){
			offsetSliders[0] = sliderValue;
		}else if (sliderText == "Green:"){
			offsetSliders[1] = sliderValue;
		}else if (sliderText == "Blue:"){
			offsetSliders[2] = sliderValue;
		}
	}
	
	private int getOffsetValues(String sliderText){
		if (sliderText == "Red:"){
			return offsetSliders[0];
		}else if (sliderText == "Green:"){
			return offsetSliders[1];
		}else if (sliderText == "Blue:"){
			return offsetSliders[2];
		}else{
			return 50;
		}
	}
	
	private JPanel createOffsetSlider(String text){
		JPanel offsetPanel = new JPanel();
		offsetPanel.setBackground(backgroundColor1);
		offsetPanel.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/25));
		offsetPanel.setLayout(new GridLayout(0, 3));
		JLabel label = new JLabel(text);
		label.setForeground(textColor);
		label.setHorizontalAlignment(JLabel.RIGHT);
		JSlider slider = new JSlider(0, 100, getOffsetValues(text));
		slider.setBackground(backgroundColor1);
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				storeOffsetValues(label.getText(), slider.getValue());
				offsetChange();
			}
		});
		offsetPanel.add(label);
		offsetPanel.add(slider);
		return offsetPanel;
	}
	
	private void offsetChange(){
		Mat4f transformation = ColorTransformationMaths.offsetMatrix(offsetSliders[0]/50f, offsetSliders[1]/50f, offsetSliders[2]/50f);
		addTransformation("offset", transformation);
		applyTransformations();
		
		if(imageFrame.pixelSelected()){
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
		}
	}
	
	
	private void offsetPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "offset" && buttonContainer.getComponent(i + 1).getName() != "offsetPanel"){
				closePanel();
				JPanel panel = new JPanel();
				panel.setBackground(backgroundColor1);
				panel.setPreferredSize(new Dimension(WIDTH/4, 3 * HEIGHT/24));
				panel.setName("offsetPanel");
				JPanel redPanel = createOffsetSlider("Red:");
				JPanel greenPanel = createOffsetSlider("Green:");
				JPanel bluePanel = createOffsetSlider("Blue:");
				panel.add(redPanel, BorderLayout.CENTER);
				panel.add(greenPanel, BorderLayout.CENTER);
				panel.add(bluePanel, BorderLayout.CENTER);
				if (buttonContainer.getComponent(i).getName() == "offset"){
					buttonContainer.add(panel, i+1);
				}else{
					buttonContainer.add(panel, i);
				}
				break;
			}else if(buttonContainer.getComponent(i).getName() == "offsetPanel"){
				buttonContainer.remove(i);
				break;
			}
		}
		
		buttonContainer.revalidate();
	}
	
	private void rotationChange(){
		Mat4f transformation = ColorTransformationMaths.rotationMatrix(rotationSliders/1f);
		addTransformation("rotation", transformation);
		applyTransformations();
		
		if(imageFrame.pixelSelected()){
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
		}
	}
	
	private void rotationPress(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "rotation" && buttonContainer.getComponent(i + 1).getName() != "rotationPanel"){
				closePanel();
				JPanel panel = new JPanel();
				panel.setBackground(backgroundColor1);
				panel.setPreferredSize(new Dimension(WIDTH/4,HEIGHT/24));
				panel.setLayout(new GridLayout(0, 3));
				panel.setName("rotationPanel");
				JLabel label = new JLabel("Rotation");
				label.setForeground(textColor);
				label.setHorizontalAlignment(JLabel.RIGHT);
				JSlider rotationSlider = new JSlider(0, 360, rotationSliders);
				rotationSlider.setBackground(backgroundColor1);
				rotationSlider.addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent e){
						rotationSliders = rotationSlider.getValue();
						rotationChange();
					}
				});
				panel.add(label);
				panel.add(rotationSlider);
				if (buttonContainer.getComponent(i).getName() == "rotation"){
					buttonContainer.add(panel, i+1);
				}else{
					buttonContainer.add(panel, i);
				}
				break;
			}else if(buttonContainer.getComponent(i).getName() == "rotationPanel"){
				buttonContainer.remove(i);
				break;
			}
		}
		buttonContainer.revalidate();
	}
	
	private void closePanel(){
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			 if(buttonContainer.getComponent(i).getName() == "brightnessPanel"){
				 buttonContainer.remove(i);
			 }
			 else if(buttonContainer.getComponent(i).getName() == "saturationPanel"){
				 buttonContainer.remove(i);
			 }else if(buttonContainer.getComponent(i).getName() == "offsetPanel"){
				 buttonContainer.remove(i);
			 }else if(buttonContainer.getComponent(i).getName() == "rotationPanel"){
				 buttonContainer.remove(i);
			 }
		}
	}
	
	
	private void reset(){
		resetTransformations();
		brightnessSliders[0] = 50;
		brightnessSliders[1] = 50;
		brightnessSliders[2] = 50;
		saturationSliders = 50;
		offsetSliders[0] = 0;
		offsetSliders[1] = 0;
		offsetSliders[2] = 0;
		rotationSliders = 0;
		brightnessChange();
		offsetChange();
		for (int i=0; i<buttonContainer.getComponentCount(); i++){
			if(buttonContainer.getComponent(i).getName() == "brightnessPanel"){
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(1)).getComponent(1)).setValue(50);
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(0)).getComponent(1)).setValue(50);
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(2)).getComponent(1)).setValue(50);
			}
			else if(buttonContainer.getComponent(i).getName() == "saturationPanel"){
				(((JSlider)((JPanel) buttonContainer.getComponent(i)).getComponent(1))).setValue(50);
			}else if(buttonContainer.getComponent(i).getName() == "offsetPanel"){
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(1)).getComponent(1)).setValue(0);
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(0)).getComponent(1)).setValue(0);
				((JSlider)((JPanel)((JPanel) buttonContainer.getComponent(i)).getComponent(2)).getComponent(1)).setValue(0);
			}else if(buttonContainer.getComponent(i).getName() == "rotationPanel"){
				(((JSlider)((JPanel) buttonContainer.getComponent(i)).getComponent(1))).setValue(0);
			}
		}
	}
	
	private void updateColorEditor(){
		Color color = imageFrame.getSelectedColor();
		storeColorEditorValues("Red:", color.getRed() * 100 / 255);
		storeColorEditorValues("Green:", color.getGreen() * 100 / 255);
		storeColorEditorValues("Blue:", color.getBlue() * 100 / 255);
		((JSlider) editRed.getComponent(1)).setValue(getColorEditorValues("Red:"));
		((JSlider) editGreen.getComponent(1)).setValue(getColorEditorValues("Green:"));
		((JSlider) editBlue.getComponent(1)).setValue(getColorEditorValues("Blue:"));
	}
	
	private void storeColorEditorValues(String editorText, int editorValue){
		if (editorText == "Red:"){
			colorEditorSliders[0] = editorValue;
		}else if (editorText == "Green:"){
			colorEditorSliders[1] = editorValue;
		}else if (editorText == "Blue:"){
			colorEditorSliders[2] = editorValue;
		}
	}
	
	private int getColorEditorValues(String editorText){
		if (editorText == "Red:"){
			return colorEditorSliders[0];
		}else if (editorText == "Green:"){
			return colorEditorSliders[1];
		}else if (editorText == "Blue:"){
			return colorEditorSliders[2];
		}else{
			return 0;
		}
	}
	
	private void colorEditorChange(){
		Color color = new Color(getColorEditorValues("Red:") * 255 / 100, getColorEditorValues("Green:") * 255 / 100, getColorEditorValues("Blue:") * 255 / 100);
		imageFrame.setPixel(imageFrame.getSelectedX(), imageFrame.getSelectedY(), color);
		graphFrame.setColor(color);
	}
	
	private JPanel createColorEditorSlider(String text){
		JPanel colorEditorPanel = new JPanel();
		colorEditorPanel.setBackground(backgroundColor1);
		colorEditorPanel.setPreferredSize(new Dimension(WIDTH/4, HEIGHT/25));
		colorEditorPanel.setLayout(new GridLayout(0, 3));
		JLabel label = new JLabel(text);
		label.setForeground(textColor);
		label.setHorizontalAlignment(JLabel.RIGHT);
		JSlider slider = new JSlider(0, 100, getOffsetValues(text));
		slider.setBackground(backgroundColor1);
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				storeColorEditorValues(label.getText(), slider.getValue());
				colorEditorChange();
			}
		});
		colorEditorPanel.add(label);
		colorEditorPanel.add(slider);
		return colorEditorPanel;
	}
	
	private void addTransformation(String transformation, Mat4f transformationMatrix){
		int transformationIndex = getTransformationIndex(transformation);	
		int index = transformationIndeces[transformationIndex];;
		if(index < 0){
			appendTransformation(transformationIndex, transformationMatrix);
		}else{
			appliedTransformations[index] = transformationMatrix;
		}
	}
	
	private int getTransformationIndex(String transformation){
		if(transformation == "brightness"){
			return 0;
		}else if(transformation == "luminance"){
			return 1;
		}else if(transformation == "saturation"){
			return 2;
		}else if(transformation == "offset"){
			return 3;
		}else if(transformation == "rotation"){
			return 4;
		}
		return -1;
	}
	
	private void appendTransformation(int transformationIndex, Mat4f transformationMatrix){
		for(int i = 0; i < appliedTransformations.length; i++){
			if(appliedTransformations[i] == null){
				appliedTransformations[i] = transformationMatrix;
				transformationIndeces[transformationIndex] = i;
				return;
			}
		}
	}
	
	private void applyTransformations(){
		int[] pixelArray = imageFrame.getBasePixels().clone();
		for (int i = 0; i<pixelArray.length; i++){
			Vec4f color = ColorTransformationMaths.loadColorToVec4f(new Color(pixelArray[i]));

			for(Mat4f transformation:appliedTransformations){
				if(transformation == null) 
					continue;
				
				color = Mat4f.mul(transformation, color);
			}
			
			pixelArray[i] = ColorTransformationMaths.vec4fToColor(color).getRGB();
		}
		
		imageFrame.setPixels(pixelArray);
		
		//colorEditorChange()
	}
	
	private void removeTransformation(String transformation){
		int transformationIndex = getTransformationIndex(transformation);
		
		for(int i = 0; i < transformationIndeces.length; i++){
			if(transformationIndeces[i] > transformationIndeces[transformationIndex]){
				transformationIndeces[i]--;
			}
		}
		
		appliedTransformations[transformationIndeces[transformationIndex]] = null;
		transformationIndeces[transformationIndex] = -1;
		
		applyTransformations();
	}
	
	private void resetTransformations(){
		transformationIndeces = new int[]{-1, -1, -1, -1, -1};
		appliedTransformations = new Mat4f[]{null, null, null, null, null};
	}
	
	public void toggleScreen(int nextScreen){
		switch(nextScreen){
		/* To the Image Screen */
		case 0:
			graphFrame.setPreferredSize(new Dimension(0, 0));
			colorEditor.setPreferredSize(new Dimension(0, 0));
			imageFrame.setPreferredSize(new Dimension(3*WIDTH/4, HEIGHT));
			frameContainer.revalidate();
			
			graphFrame.setColor(Color.BLACK);
			
			break;
		/* To the Graph Screen */
		case 1:
			imageFrame.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT / 2));
			colorEditor.setPreferredSize(new Dimension(WIDTH / 4, HEIGHT));
			graphFrame.setPreferredSize(new Dimension(WIDTH / 2, HEIGHT / 2));
			frameContainer.revalidate();
			
			graphFrame.setColor(imageFrame.getSelectedColor());
			updateColorEditor();
			break;
		}
		screen = nextScreen;
	}
}
