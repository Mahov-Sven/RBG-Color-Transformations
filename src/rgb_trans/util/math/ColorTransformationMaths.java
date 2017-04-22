package rgb_trans.util.math;

import java.awt.Color;

public class ColorTransformationMaths {
	
	public static Color vec4fToColor(Vec4f color){
		return new Color(Maths.clamp(color.x), Maths.clamp(color.y), Maths.clamp(color.z));
	}

	public static Vec4f loadColorToVec4f(Color color){
		return new Vec4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
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
		return new Mat4f().rotate(rotation, new Vec3f(1, 1, 1));
	}
}
