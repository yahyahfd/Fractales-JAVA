package projet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Model class: contains all the methods and is used for terminal mode
 * @author HAFID Yahya 71800678
 * @author OUBADIA Tanel 71806010
 */
public class Model {
	/**
	 * A polynomial used in the {@link Controller}
	 */
	private Polynom p;
	/**
	 * A complex used in the {@link Controller}
	 */
	private Complex c;
	/**
	 * A plane used in the {@link Controller}
	 */
	private Plane r;
	
	/**
	 * Model class constructor: contains all the basic methods
	 * @param p	polynomial in string form: ax^n +bx^n-1 ... +zx^0
	 * @param c complex in string for: a +bi
	 * @param x plane in string form: (x,step) or (x,y,step) or (x1,x2,y1,y2,step) without the brackets
	 * @throws NumberFormatException if we try to parse a string to an int/double
	 * @throws Exception we throw an exception if the format is not respected
	 */
	public Model(String p, String c, String x) throws NumberFormatException, Exception {
		this.p = Polynom.parsePoly(p);
		this.c = Complex.parseComplex(c);
		String[] r1 = x.split(",");
		double[] r2 = new double[r1.length];
		for(int i=0; i<r1.length;i++) {
			r2[i] = Double.parseDouble(r1[i]);
		}
		if(r2.length == 2) this.r = Plane.carreDT(r2[0],r2[1]);
		else if(r2.length == 3) this.r = Plane.rectDT(r2[0],r2[1],r2[2]);
		else if(r2.length == 5) this.r = Plane.custDT(r2[0],r2[1],r2[2],r2[3],r2[4]);
		else throw new Exception ("Wrong Syntax for the plane's coordinates: \n1) x,step\n2) x,y,step\n3)x1,x2,y1,y2,step");
	}
	
	/**
	 * Getter for plane r
	 * @return r
	 */
	public Plane getR() {
		return this.r;
	}
	
	/**
	 * Getter for complex c
	 * @return c
	 */
	public Complex getC() {
		return this.c;
	}
	
	/**
	 * Getter for polynomial p
	 * @return p
	 */
	public Polynom getP() {
		return this.p;
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
	 * This method is used to calculate the maximum iterations while using
	 * all the points of rec as reference. max_iter creates confusion
	 * in some cases since it doesn't include all the points.
	 * This is only used for the fading colors in our result image.
	 * @param rec  the complex plane where we are working
	 * @param p  the polynom we are working with
	 * @param c  the complex we are working with
	 * @param max_iter max iterations until convergence
	 * @param JORM if j, it's a Julia set, else it's a Mandelbrot one
	 * @return the maximum iterations using all the points in rec
	 * as reference
	 */
	public static int max_div(Plane rec, Polynom p, Complex c, int max_iter,char JORM) {
		int ind,max_div=0;
		for(double i=rec.x1;i<rec.x2;i+=rec.step) {
			for(double j=rec.y2;j>rec.y1;j-=rec.step) {
                Complex tmp = new Complex(i,j);
                if(JORM == 'j') {
                        ind = tmp.divergence(p,c,max_iter);
                }else  {
                        ind = c.divergence(p, tmp, max_iter);
                }
                if(max_div < ind && ind != max_iter)max_div = ind;
			}
		}
        return max_div;
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
	public static BufferedImage createImage(Plane rec, Polynom p, Complex c, int max_iter, char JORM) {
		BufferedImage img = new BufferedImage(rec.NbPixelX()+1,rec.NbPixelY()+1, BufferedImage.TYPE_INT_RGB);
		int ind;
		int max_div = max_div(rec,p,c,max_iter,JORM);
		int xx = 0;
		for(double i=rec.x1;xx<(rec.NbPixelX()+1);i+=rec.step) {
			int yy = 0;
			for(double j=rec.y2;yy<(rec.NbPixelY()+1);j-=rec.step) {
				Complex tmp = new Complex(i,j);
				if(JORM == 'j') {
					ind = tmp.divergence(p,c,max_iter);
				}else  {
					ind = c.divergence(p, tmp, max_iter);
				}
				if(ind < max_iter) {
					
					int degrade = (255*(max_div-ind))/(max_div);				
					int shade = new Color(degrade,degrade,255).getRGB();
					img.setRGB(xx, yy, shade );
					
				}
				if(ind == max_iter){
					
					img.setRGB(xx,yy, new Color(1, 152, 117).getRGB());
				}
				yy++;
			}
			xx++;
		}
		return img;
	}
	
	/**
	 * Class for complexes
	 */
	private static class Complex{
		/**
		 * r: real part <br/>
		 * i: imaginary part
		 */
		private double r, i;
		
		/**
		 * Private constructor for complex
		 * @param r real part
		 * @param i imaginary part
		 */
		private Complex(double r, double i) {
			this.r=r;
			this.i=i;
		}
		
		/**
		 * This static factory is used to parse a complex passed in arguments as a string into a complex
		 * @param s	String form of complex: a +bi
		 * @return new Complex(double r,double i)
		 * @throws Exception we throw an exception if the format is not respected
		 */
		public static Complex parseComplex(String s) throws Exception {
			String[] tmp = s.split(" ");
			if(tmp.length==2) {
				return new Complex(Double.parseDouble(tmp[0]),Double.parseDouble(tmp[1].substring(0, tmp[1].length()-1)));
			}else {
				throw new Exception("Wrong complex syntax: Please respect the \"x +yi\" syntax. (respect the spaces)");
			}
		}
		
		public String toString() {
			if(this.i>0) return this.r + " +" + this.i + "i";
			return this.r + " " + this.i + "i";
		}
		
		/**
		 * Sums up this with the complex c
		 * @param c a complex
		 * @return the result of the addition
		 */
		public Complex addition(Complex c) {
			return new Complex(this.r += c.r, this.i += c.i);
		}
		
		/**
		 * Multiplies this by the complex c
		 * @param c a complex
		 * @return the result of the multiplication
		 */
		public Complex multiplication(Complex c) {
			return new Complex(this.r*c.r - this.i*c.i,this.r*c.i + this.i*c.r);
		}
	    
		/**
		 * Calculates the modulus of this complex
		 * @return the result of the calculation
		 */
	    public double modulus() {
            return Math.sqrt(Math.pow(2, this.r) + Math.pow(2, this.i));
	    }
	    
	    /**
	     * Calculates a monomial by replacing the x in p with this
	     * @param p a polynomial
	     * @return the result of the calculation
	     */
	    public Complex monomPoly(Polynom p) {
	    	Complex tmp = this;
	    	for(int i=1;i<p.degre;i++) {
				tmp = tmp.multiplication(this);
			}
			Complex tmp2 = tmp;
			for(int j=1;j<p.coef;j++) {
				tmp = tmp.addition(tmp2);
			}
			return tmp;
	    }
	    
	    /**
	     * This method calls  {@link #monomPoly(Polynom p)} on all the "sub-polynoms" of p
	     * @param p a polynomial
	     * @param c a complex
	     * @return the addition between all the results obtained
	     * @see #monomPoly(Polynom p)
	     */
	    public Complex PolyRecu(Polynom p,Complex c) {
	    	if(p.next == null) {
	    		return monomPoly(p).addition(c);
	    	}else {
	    		return monomPoly(p).addition(PolyRecu(p.next,c));
	    	}
	    }
		
	    /**
	     * Calculates the divergence index of a specific point
	     * @param p Polynom
	     * @param c Complex
	     * @param max_ite the maximum iterations: 1000 is recommanded
	     * @return the divergence index
	     */
		public int divergence(Polynom p,Complex c, int max_ite) {
			int ite = 0;
			Complex zn = this;
			int radius = 2;
			while(ite < max_ite && zn.modulus() <= radius) {
				zn = zn.PolyRecu(p,c);
				ite++;
			}
			return ite;
		}
	}
	
	/**
	 * Class for the plane
	 */
	private static class Plane {
		/**
		 * x1,x2: coordinates for the X axis: x1 is lower than x2<br/>
		 * y1,y2: coordinates for the Y axis: y1 is lower than y2<br/>
		 * step: the step between each two consecutive points: the pixels between each
		 */
		double x1,x2,y1,y2,step;
		
		/**
		 * Private constructor for plane
		 * @param x1 shorter side's left coordinate
		 * @param x2 shorter side's right coordinate
		 * @param y1 longer side's left coordinate
		 * @param y2 longer side's right coordinate
		 * @param step step between two consecutive points
		 */
		private Plane(double x1,double x2,double y1,double y2,double step) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.step = step;
		}
		
		/**
		 * Static factory used to create a square plane
		 * @param x side's coordinate (negative for left)
		 * @param step the difference between a pixel and the next
		 * @return a new square plane
		 */
		public static Plane carreDT(double x, double step) {
			return new Plane(-x,x,-x,x,step);
		}
		
		/**
		 * Static factory used to create a rectangle plane
		 * @param x shorter side's coordinate (negative for left)
		 * @param y longer side's coordinate (negative for left)
		 * @param step the difference between a pixel and the next
		 * @return a new rectangle plane
		 */
		public static Plane rectDT(double x, double y, double step) {
			return new Plane(-x,x,-y,y,step);
		}
		
		/**
		 * Static factory used to create a custom plane:
		 * x1 is lower than x2 and y1 lower than y2
		 * @param x1 shorter side's left coordinate
		 * @param x2 shorter side's right coordinate
		 * @param y1 longer side's left coordinate
		 * @param y2 longer side's right coordinate
		 * @param step step between two points
		 * @return a new custom plane
		 */
		public static Plane custDT(double x1,double x2,double y1,double y2,double step) {
			return new Plane(x1,x2,y1,y2,step);
		}
		
		/**
		 * @return the number of pixels on the X axis
		 */
		public int NbPixelX() {
			return (int) ((this.x2-this.x1)/this.step);
		}
		
		/**
		 * @return the number of pixels on the Y axis
		 */
		public int NbPixelY() {
			return (int) ((this.y2-this.y1)/this.step);
		}
	}
	
	/**
	 * Class for the polynomials
	 */
	private static class Polynom {
		/**
		 * Degree
		 */
		int degre;
		/**
		 * Coefficient
		 */
		int coef;
		/**
		 * Next polynomial
		 */
		Polynom next;
		
		/**
		 * Private Polynom constructor
		 * @param d degree
		 * @param c complex
		 * @param n next polynomial
		 */
		private Polynom(int d, int c, Polynom n) {
			this.degre=d;
			this.coef=c;
			this.next=n;
		}
		
		/**
		 * Static fabric used to create a polynomial
		 * @param d degree
		 * @param c coefficient
		 * @param n the next polynomial: needs to have a lower degree than this
		 * @return a new polynomial formed by this and n
		 * @throws Exception we throw an exception if the format is not respected
		 */
		public static Polynom makePoly(int d,int c, Polynom n) throws Exception {
			try {
	            if(d<=n.degre) {
	            	throw new Exception("Wrong Polynom syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
	            }
	        } catch (NullPointerException e) {
	        	//this means our polynomial is finished, we don't do anything
	        }
	    	return new Polynom(d,c,n);
		}
		
		/**
		 * Fabric static used to parse a polynomial from a string
		 * @param s string form of polynomial ax^n +bx^n-1 ... +zx^0
		 * @return a new polynomial based on s
		 * @throws NumberFormatException if we try to parse a string to an int/double
		 * @throws Exception we throw an exception if the format is not respected
		 */
		public static Polynom parsePoly (String s) throws NumberFormatException, Exception {
			Polynom res = null;
			String[] tmp = s.split(" ");
			for(int i=tmp.length-1;i>=0;i--) {
				String[] tmp2 = tmp[i].split("x");
				if(tmp2.length==2) {
					res = makePoly(Integer.parseInt(tmp2[1].substring(1)),Integer.parseInt(tmp2[0]),res);
				}else {
					throw new Exception("Wrong Polynom syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
				}
			}
			return res;
		}
		
		/**
		 * This toString method is used to avoid putting a space before the first polynomial
		 * @param n null at first, contains the last polynomial each time it is called
		 * @return a string based on a polynomial
		 */
		private String toString(Polynom n) {
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
		
		/**
		 * Final toString method that calls {@link #toString(Polynom n)} with a null argument
		 */
		public String toString() {
			return this.toString(null);
		}
	}

}
