package rgb_trans.graphics;

public class Mesh2D {
	
	private PixelVertex[] verteces;
	private PixelVertex position;
	
	public Mesh2D(PixelVertex position, PixelVertex... verteces){
		this.verteces = new PixelVertex[verteces.length];
		this.position = position;
		
		for(int i = 0; i < verteces.length; i++){
			this.verteces[i] = verteces[i];
		}
	}
	
	public PixelVertex getPos(){
		return position;
	}
	
	public void setPos(PixelVertex position){
		this.position = position;
	}
	
	public void setPos(int x, int y){
		this.position.x = x;
		this.position.y = y;
	}
	
	public int length(){
		return verteces.length;
	}
	
	public int[] getXVerteces(){
		int[] xVerteces = new int[verteces.length];
		for(int i = 0; i < verteces.length; i++){
			xVerteces[i] = verteces[i].x + position.x;
		}
		return xVerteces;
	}
	
	public int[] getYVerteces(){
		int[] xVerteces = new int[verteces.length];
		for(int i = 0; i < verteces.length; i++){
			xVerteces[i] = verteces[i].y + position.y;
		}
		return xVerteces;
	}
}
