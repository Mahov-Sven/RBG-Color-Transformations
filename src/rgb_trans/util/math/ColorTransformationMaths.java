package rgb_trans.util.math;

import java.awt.Color;

public class ColorTransformationMaths {
	
	public static Color vec4fToColor(Vec4f color){
		return new Color(color.x, color.y, color.z);
	}

	public static Vec4f loadColorToVec4f(Color color){
		return new Vec4f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 1);
	}
	
	public static Mat4f brightnessMatrix(float redScale, float greenScale, float blueScale){
		return new Mat4f(
				redScale, 	0, 				0, 				0,
				0, 			greenScale, 	0, 				0,
				0, 			0, 				blueScale, 		0,
				0, 			0, 				0, 				1
				);
	}
	
	public static Mat4f luminanceMatrix(){
		float redWeight = 0.3086f;
		float greenWeight = 0.6094f;
		float blueWeight = 0.0820f;
		return new Mat4f(
				redWeight, 		redWeight, 		redWeight, 		0,
				greenWeight, 	greenWeight, 	greenWeight, 	0,
				blueWeight, 	blueWeight, 	blueWeight, 	0,
				0, 				0, 				0, 				1
				);
	}
	
	public static Mat4f saturationMatrix(float saturation){
		float redWeight = 0.3086f;
		float greenWeight = 0.6094f;
		float blueWeight = 0.0820f;
		
		float satRed = (1 - saturation) * redWeight;
		float satGeen = (1 - saturation) * greenWeight;
		float satBlue = (1 - saturation) * blueWeight;
		
		return new Mat4f(
				satRed + saturation, 	satRed, 				satRed, 				0,
				satGeen, 				satGeen + saturation, 	satGeen, 				0,
				satBlue, 				satBlue, 				satBlue + saturation, 	0,
				0, 						0, 						0, 						1
				);
	}
	
	public static Mat4f offsetMatrix(float redOffset, float greenOffset, float blueOffset){
		return new Mat4f(
				1, 0, 0, redOffset,
				0, 1, 0, greenOffset,
				0, 0, 1, blueOffset,
				0, 0, 0, 1
				);
	}
	
	public static Mat4f rotationMatrix(float rotation){
		float redWeight = 0.3086f;
		float greenWeight = 0.6094f;
		float blueWeight = 0.0820f;
		
		float sqrt2 = (float) Math.sqrt(2);
		float sqrt3 = (float) Math.sqrt(3);
		
		Mat4f mat = new Mat4f();
		
		/* Rotate grey vector to positive Z*/
		mat = Mat4f.mul(mat, rotateX(1 / sqrt2, 1 / sqrt2));
		mat = Mat4f.mul(mat, rotateY(-1 / sqrt3, sqrt2 / sqrt3));
		
		
		/* To conserve Hue we need to shear before rotating Z */
		float tx = redWeight * mat.s00 + greenWeight * mat.s10 + blueWeight * mat.s20 + mat.s30;
		float ty = redWeight * mat.s01 + greenWeight * mat.s11 + blueWeight * mat.s21 + mat.s31;
		float tz = redWeight * mat.s02 + greenWeight * mat.s12 + blueWeight * mat.s22 + mat.s32;
		float dx = tx/tz;
		float dy = ty/tz;
		mat = Mat4f.mul(mat, shearZ(dx, dy));
		
		/* Rotate Z */
		float rc = (float) Math.cos(Math.toRadians(rotation));
		float rs = (float) Math.sin(Math.toRadians(rotation));
		mat = Mat4f.mul(mat, rotateZ(rc, rs));
		
		/* We need to unshear */
		mat = Mat4f.mul(mat, shearZ(dx, dy));
		
		/* Rotate grey vector back */
		mat = Mat4f.mul(mat, rotateY(1 / sqrt3, sqrt2 / sqrt3));
		mat = Mat4f.mul(mat, rotateX(-1 / sqrt2, 1 / sqrt2));
		
		return mat;
	}
	
	private static Mat4f rotateX(float cos, float sin){
		Mat4f mat = new Mat4f();
		mat.s11 = cos;
		mat.s12 = sin;
		mat.s21 = -sin;
		mat.s22 = cos;
		return mat;
	}
	
	private static Mat4f rotateY(float cos, float sin){
		Mat4f mat = new Mat4f();
		mat.s00 = cos;
		mat.s02 = -sin;
		mat.s20 = sin;
		mat.s22 = cos;
		return mat;
	}
	
	private static Mat4f rotateZ(float cos, float sin){
		Mat4f mat = new Mat4f();
		mat.s00 = cos;
		mat.s01 = sin;
		mat.s10 = -sin;
		mat.s11 = cos;
		return mat;
	}
	
	private static Mat4f shearZ(float dx, float dy){
		Mat4f mat = new Mat4f();
		mat.s02 = dx;
		mat.s12 = dy;
		return mat;
	}
}
