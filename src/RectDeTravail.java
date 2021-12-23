package projet;


public class RectDeTravail {
	Double x1,x2,y1,y2,pas;
	
	
	public RectDeTravail(Double x1,Double x2,Double y1,Double y2,Double pas) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.pas = pas;
	}
	
	public int NbPixelX() {
		return (int) ((this.x2-this.x1)/this.pas);
	}
	public int NbPixelY() {
		return (int) ((this.y2-this.y1)/this.pas);
	}
}