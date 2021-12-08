
public class Complexe {
	private double r;
    private double i;

    public Complexe(double r,double i) {
            this.r=r;
            this.i=i;
    }
    public static final Complexe zero = new Complexe(0,0);
    public static final Complexe un = new Complexe(1,0);
    public static final Complexe compi = new Complexe(0,1);

    public double getR() {
            return this.r;
    }
    public double getI() {
            return this.i;
    }

    public void setRe(double re) {
            this.r = re;
    }
    public void setIm(double im) {
            this.i = im;
    }

    public String toString() {
            return this.r + " + i * " + this.i;
    }

    public Complexe somme(Complexe c) {
            return new Complexe(this.r += c.getR(),this.i += c.getI());
    }

    public Complexe soustraction(Complexe c) {
            return new Complexe(this.r -= c.getR(),this.i -= c.getI());

    }

    public Complexe multiplication(Complexe c) {
            return new Complexe(this.r*c.getR() - this.i*c.getI(),this.r*c.getI() + this.i*c.getR());
    }

    public Complexe division(Complexe c) {
            double reelle = (this.r*c.getR()+this.i*c.getI())/Math.pow(2,c.getR()) + Math.pow(2,c.getI());
            double imaginaire = (c.getR()*this.i - this.r*c.getI())/Math.pow(2,c.getR()) + Math.pow(2,c.getI());
            return new Complexe(reelle,imaginaire);
    }

    public boolean equals(Object other) {
            if(other instanceof Complexe) {
                    Complexe o = (Complexe) other;
                    return this.r == o.getR() && this.i == o.getI();
            }else {
                    return false;
            }

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



    public static Complexe fromPolarCoordinates(double rho, double theta) {
            return new Complexe(rho*Math.cos(theta),rho*Math.sin(theta));
    }



}

