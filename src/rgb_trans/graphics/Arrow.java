package rgb_trans.graphics;

import java.awt.Color;

public class Arrow {
	private Mesh2D mesh;
	private Color color;
	
	public Arrow(int length, int width, double angle, double headLengthPercent, double headWidthPercent, Color color){
		
		PixelVertex p1 = rotate(new PixelVertex(0, (int) -(width * (1 - headWidthPercent) / 2)), angle);
		PixelVertex p2 = rotate(new PixelVertex((int) (length * (1 - headLengthPercent)), (int) -(width * (1 - headWidthPercent) / 2)), angle);
		PixelVertex p3 = rotate(new PixelVertex((int) (length * (1 - headLengthPercent)), -width / 2), angle);
		PixelVertex p4 = rotate(new PixelVertex(length, 0), angle);
		PixelVertex p5 = rotate(new PixelVertex((int) (length * (1 - headLengthPercent)), width / 2), angle);
		PixelVertex p6 = rotate(new PixelVertex((int) (length * (1 - headLengthPercent)), (int) (width * (1 - headWidthPercent) / 2)), angle);
		PixelVertex p7 = rotate(new PixelVertex(0, (int) (width * (1 - headWidthPercent) / 2)), angle);
	
		this.mesh = new Mesh2D(p1, p2, p3, p4, p5, p6, p7);
		this.color = color;
	}
	
	private static PixelVertex rotate(PixelVertex vertex, double angle){
		PixelVertex rotatedPoint = new PixelVertex(0, 0);
		rotatedPoint.x = (int) (Math.cos(Math.toRadians(-angle)) * vertex.x - Math.sin(Math.toRadians(-angle)) * vertex.y);
		rotatedPoint.y = (int) (Math.cos(Math.toRadians(-angle)) * vertex.y + Math.sin(Math.toRadians(-angle)) * vertex.x);
		return rotatedPoint;
	}
	
	public void setPos(PixelVertex position){
		mesh.setPos(position);
	}
	
	public PixelVertex getPos(){
		return mesh.getPos();
	}
	
	public Mesh2D getMesh(){
		return mesh;
	}
	
	public Color getColor(){
		return color;
	}
}
