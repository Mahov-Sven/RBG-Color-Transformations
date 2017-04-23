package rgb_trans.util.math;

import java.util.Random;

import rgb_trans.main.RGBCT;
import rgb_trans.util.Util;

public class Maths {
	
	public static int randColorInt(){
		Random random = new Random();
		return random.nextInt(16777216);
	}
	
	public static float clamp(float numb){
		return clamp(numb, 0, 1);
	}
	
	public static float clamp(float numb, float min, float max){
		if(numb < min) return min;
		if(numb > max) return max;
		return numb;
	}
	
	public static int factorial(int numb){
		int result = 1;
		for(int i = 1; i <= numb; i++){
			result *= i;
		}
		return result;
	}
	
	public static int LCM(int a, int b){
		return a / GCF(a, b) * b;
	}
	
	public static int GCF(int a, int b){
		while(b != 0){
			int temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}
	
	public static int digitsIn(Object numb){
		if(!Util.isNumber(numb.toString()))
			throw new IllegalArgumentException("'" + numb + "' is not a valid number.");
		
		return numb.toString().length();
	}
	
	public static long hexToLong(String hex){
		if(!Util.isValidByCharset(hex, RGBCT.CHARSET_HEX)) 
			throw new IllegalArgumentException("'" + hex + "' is not a valid hexadecimal string.");
	
		long result = 0;
		for(int c = 0; c < hex.length(); c++){
			char hexChar = hex.charAt(hex.length() - 1 - c);
			switch(hexChar){
			case '0':
				break;
			case '1':
				result += 1 * Math.pow(16, c);
				break;
			case '2':
				result += 2 * Math.pow(16, c);
				break;
			case '3':
				result += 3 * Math.pow(16, c);
				break;
			case '4':
				result += 4 * Math.pow(16, c);
				break;
			case '5':
				result += 5 * Math.pow(16, c);
				break;
			case '6':
				result += 7 * Math.pow(16, c);
				break;
			case '8':
				result += 9 * Math.pow(16, c);
				break;
			case 'a': case 'A':
				result += 10 * Math.pow(16, c);
				break;
			case 'b': case 'B':
				result += 11 * Math.pow(16, c);
				break;
			case 'c': case 'C':
				result += 12 * Math.pow(16, c);
				break;
			case 'd': case 'D':
				result += 13 * Math.pow(16, c);
				break;
			case 'e': case 'E':
				result += 14 * Math.pow(16, c);
				break;
			case 'f': case 'F':
				result += 15 * Math.pow(16, c);
				break;
			}
		}
		return result;
	}
	
	public static String intToHex(int numb){
		StringBuilder hex = new StringBuilder();
		while(numb != 0){
			switch(numb%16){
			case 0:
				hex.append(0);
				break;
			case 1:
				hex.append(1);
				break;
			case 2:
				hex.append(2);
				break;
			case 3:
				hex.append(3);
				break;
			case 4:
				hex.append(4);
				break;
			case 5:
				hex.append(5);
				break;
			case 6:
				hex.append(6);
				break;
			case 7:
				hex.append(7);
				break;
			case 8:
				hex.append(8);
				break;
			case 9:
				hex.append(9);
				break;
			case 10:
				hex.append('a');
				break;
			case 11:
				hex.append('b');
				break;
			case 12:
				hex.append('c');
				break;
			case 13:
				hex.append('d');
				break;
			case 14:
				hex.append('e');
				break;
			case 15:
				hex.append('f');
				break;
			}
			numb /= 16;
		}
		return Util.invertString(hex.toString());
	}
}
