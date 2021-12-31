package projet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Model {
	Polynome p;
	Complexe c;
	RectDeTravail r;
	
	public Model(String p, String c, String x) throws NumberFormatException, Exception {
		this.p = Polynome.parsePoly(p);
		this.c = Complexe.parseComplexe(c);
		String[] r1 = x.split(",");
		double[] r2 = new double[r1.length];
		for(int i=0; i<r1.length;i++) {
			r2[i] = Double.parseDouble(r1[i]);
		}
		if(r2.length == 2) this.r = RectDeTravail.carreDT(r2[0],r2[1]);
		else if(r2.length == 3) this.r = RectDeTravail.rectDT(r2[0],r2[1],r2[2]);
		else if(r2.length == 5) this.r = RectDeTravail.custDT(r2[0],r2[1],r2[2],r2[3],r2[4]);
		else throw new Exception ("Wrong Syntax for the plane's coordinates: \n1) x,pas\n2) x,y,pas\n3)x1,x2,y1,y2,pas");
	}

	/**
	* Creates a png file of the image specified.
	* If the dirname already exists, deletes it and creates a new one.
	* @param  img  the image to save (BufferedImage)
	* @param  dirname the location where to save the image
	*/
	public static void affichage(BufferedImage img, String dirname) {
		File f1 = new File(dirname+".png");
		try {
			ImageIO.write(img, "PNG", f1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a BufferedImage representation of a Julia/Mandelbrot set
	 * @param rec  the complex plane where we are working
	 * @param p  the polynom we are working with
	 * @param c  the complex we are working with
	 * @param max_iter max iterations until convergence
	 * @param JORM if j, it's a Julia set, else it's a Mandelbrot one
	 * @return the buffered image made out of these parameters
	 */
	public static BufferedImage createImage(RectDeTravail rec, Polynome p, Complexe c, int max_iter, char JORM) {
		BufferedImage img = new BufferedImage(rec.NbPixelX()+1,rec.NbPixelY()+1, BufferedImage.TYPE_INT_RGB);
		int ind;
		int max_div = 0;
		int xx = 0;
		for(Double i=rec.x1;i<rec.x2;i+=rec.pas) {
			int yy = 0;
			for(Double j=rec.y2;j>rec.y1;j-=rec.pas) {
				Complexe tmp = new Complexe(i,j);
				if(JORM == 'j') {
					ind = tmp.divergence(p,c,max_iter);
				}else  {
					ind = c.divergence(p, tmp, max_iter);
				}
				if(max_div < ind && ind != max_iter)max_div = ind;
				//System.out.println(tmp.toString()+" : "+ind);
				if(ind < max_iter) {
					int shade = ((255*ind)/(max_div+1)|0|0);
					img.setRGB(xx, yy, shade );
				}
				if(ind == max_iter) {
					img.setRGB(xx,yy, (0|0|0) );
				}
				yy++;
			}
			xx++;
		}
		return img;
	}
	
	private static class Complexe{
		private double r, i;
		
		public Complexe(double r, double i) {
			this.r=r;
			this.i=i;
		}
		
		public static Complexe parseComplexe(String s) throws Exception {
			String[] tmp = s.split(" ");
			if(tmp.length==2) {
				return new Complexe(Double.parseDouble(tmp[0]),Double.parseDouble(tmp[1].substring(0, tmp[1].length()-1)));
			}else {
				throw new Exception("Wrong complex syntax: Please respect the \"x +yi\" syntax. (respect the spaces)");
			}
		}
		
		public String toString() {
			if(this.i>0) return this.r + " +" + this.i + "i";
			return this.r + " " + this.i + "i";
		}
		
		public Complexe somme(Complexe c) {
			return new Complexe(this.r += c.r, this.i += c.i);
		}
		
		public Complexe soustraction(Complexe c) {
			return new Complexe(this.r -= c.r,this.i -= c.i);
		}
		
		public Complexe multiplication(Complexe c) {
			return new Complexe(this.r*c.r - this.i*c.i,this.r*c.i + this.i*c.r);
		}
		
		public Complexe division(Complexe c) {
            double reelle = (this.r*c.r+this.i*c.i)/Math.pow(2,c.r) + Math.pow(2,c.i);
            double imaginaire = (c.r*this.i - this.r*c.i)/Math.pow(2,c.r) + Math.pow(2,c.i);
            return new Complexe(reelle,imaginaire);
		}
		
	    public boolean equals(Complexe o) {
			return this.r == o.r && this.i == o.i;
	    }
	    
	    public Complexe conj() {
            return new Complexe(this.r,-this.i);
	    }
	    
	    public double module() {
            return Math.sqrt(Math.pow(2, this.r) + Math.pow(2, this.i));
	    }
	    
	    public double argument() {
            if (this.module() == 0) {
                    return 0;
            }else if(this.i>= 0) {
                    return Math.acos(this.r/this.module());
            }else {
                    return -Math.acos(this.r/this.module());
            }
	    }
	    
	    public Complexe unPoly(Polynome p) {
	    	Complexe tmp = this;
	    	for(int i=1;i<p.degre;i++) {
				tmp = tmp.multiplication(this);
			}
			Complexe tmp2 = tmp;
			for(int j=1;j<p.coef;j++) {
				tmp = tmp.somme(tmp2);
			}
			return tmp;
	    }
	    
	    public Complexe PolyRecu(Polynome p,Complexe c) {
	    	if(p.next == null) {
	    		return unPoly(p).somme(c);
	    	}else {
	    		return unPoly(p).somme(PolyRecu(p.next,c));
	    	}
	    }
	    
		public void JuliaIteration(Polynome p,Complexe c,int nb) {
			Complexe tmp = this;
			for(int i = 0; i < nb ; i++) {
				tmp = tmp.PolyRecu(p,c);
				System.out.println(tmp.toString());		
			}
		}
		
		public int divergence(Polynome p,Complexe c, int max_ite) {
			int ite = 0;
			Complexe zn = this;
			int radius = 2;
			while(ite < max_ite && zn.module() <= radius) {
				zn = zn.PolyRecu(p,c);
				ite++;
			}
			return ite;
		}
	}
	
	private static class RectDeTravail {
		double x1,x2,y1,y2,pas;
		
		private RectDeTravail(double x1,double x2,double y1,double y2,double pas) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.pas = pas;
		}
		
		public static RectDeTravail carreDT(double x, double pas) {
			return new RectDeTravail(-x,x,-x,x,pas);
		}
		
		public static RectDeTravail rectDT(double x, double y, double pas) {
			return new RectDeTravail(-x,x,-y,y,pas);
		}
		
		public static RectDeTravail custDT(double x1,double x2,double y1,double y2,double pas) {
			return new RectDeTravail(x1,x2,y1,y2,pas);
		}
		
		public int NbPixelX() {
			return (int) ((this.x2-this.x1)/this.pas);
		}
		public int NbPixelY() {
			return (int) ((this.y2-this.y1)/this.pas);
		}
	}
	
	private static class Polynome {
		int degre;
		int coef;
		Polynome next;
		
		private Polynome(int d, int c, Polynome n) {
			this.degre=d;
			this.coef=c;
			this.next=n;
		}
		
		public static Polynome makePoly(int d,int c, Polynome n) throws Exception {
			try {
	            if(d<=n.degre) {
	            	throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
	            }
	        } catch (NullPointerException e) {
	        	//this means our polynome is finished, we don't do anything
	        }
	    	return new Polynome(d,c,n);
		}
		
		public static Polynome parsePoly (String s) throws NumberFormatException, Exception {
			Polynome res = null;
			String[] tmp = s.split(" ");
			for(int i=tmp.length-1;i>=0;i--) {
				String[] tmp2 = tmp[i].split("x");
				if(tmp2.length==2) {
					res = makePoly(Integer.parseInt(tmp2[1].substring(1)),Integer.parseInt(tmp2[0]),res);
				}else {
					throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
				}
			}
			return res;
		}
		
		private String toString(Polynome n) {
			String rescoef;
			if(n==null) {
				rescoef = ""+this.coef;
			}else {
				if(this.coef>0) {
					rescoef= " +"+this.coef;
				}else {
					rescoef= " "+this.coef;
				}
			}
			if(this.next == null) {
				if(this.degre == 0) return rescoef;
				if(this.degre == 1) return rescoef+"x";
				return rescoef+"x^"+this.degre;
			}else { 
				if(this.degre == 0) return rescoef+this.next.toString(this);
				if(this.degre == 1) return rescoef+"x"+this.next.toString(this);
				return rescoef+"x^"+this.degre+this.next.toString(this);
			}
		}

		public String toString() {
			return this.toString(null);
		}
	}

}
