package rgb_trans.test;

import java.awt.Toolkit;

import rgb_trans.graphics.Display;

public class TestMainCathbad {

	public static void main(String... args){
		Display display = new Display(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		display.drawMain();
	}
}
