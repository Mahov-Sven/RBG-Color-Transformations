package rgb_trans.util.math;

import java.awt.Color;

public class ColorTransformationMaths {

	public Vector loadColorToVector4D(Color color){
		return new Vector(color.getRed(), color.getGreen(), color.getBlue(), 1);
	}
	
	public Matrix brightnessMatrix(Fraction redScale, Fraction greenScale, Fraction blueScale){
		return new Matrix(4, 4, 
				redScale, Fraction.ZERO(), Fraction.ZERO(), Fraction.ZERO(),
				Fraction.ZERO(), greenScale, Fraction.ZERO(), Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ZERO(), blueScale, Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ZERO(), Fraction.ZERO(), Fraction.ONE()
				);
	}
	
	public Matrix luminanceMatrix(){
		Fraction redWeight = new Fraction(3086/10000);
		Fraction greenWeight = new Fraction(6094/10000);
		Fraction blueWeight = new Fraction(820/10000);
		return new Matrix(4, 4, 
				redWeight, redWeight, redWeight, Fraction.ZERO(),
				greenWeight, greenWeight, greenWeight, Fraction.ZERO(),
				blueWeight, blueWeight, blueWeight, Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ZERO(), Fraction.ZERO(), Fraction.ONE()
				);
	}
	
	public Matrix saturationMatrix(Fraction saturation){
		Fraction redWeight = new Fraction(3086/10000);
		Fraction greenWeight = new Fraction(6094/10000);
		Fraction blueWeight = new Fraction(820/10000);
		
		Fraction satRed = Fraction.ONE().copy().subtract(saturation).mul(redWeight);
		Fraction satGeen = Fraction.ONE().copy().subtract(saturation).mul(greenWeight);
		Fraction satBlue = Fraction.ONE().copy().subtract(saturation).mul(blueWeight);
		
		return new Matrix(4, 4, 
				satRed.add(saturation), satRed, satRed, Fraction.ZERO(),
				satGeen, satGeen.add(saturation), satGeen, Fraction.ZERO(),
				satBlue, satBlue, satBlue.add(saturation), Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ZERO(), Fraction.ZERO(), Fraction.ONE()
				);
	}
	
	public Matrix offsetMatrix(Fraction redOffset, Fraction greenOffset, Fraction blueOffset){
		return new Matrix(4, 4, 
				Fraction.ONE(), Fraction.ZERO(), Fraction.ZERO(), Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ONE(), Fraction.ZERO(), Fraction.ZERO(),
				Fraction.ZERO(), Fraction.ZERO(), Fraction.ONE(), Fraction.ZERO(),
				redOffset, greenOffset, blueOffset, Fraction.ONE()
				);
	}
	
	public Matrix rotationMatrix(){
		return null;
	}
}
