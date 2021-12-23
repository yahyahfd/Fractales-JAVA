package projet;

public class Polynome {
	int degre;
	int coef;
	Polynome next;
	
	public Polynome(int d,int c, Polynome n) {
		this.degre = d;
		this.coef = c;
		this.next = n;
	}
	
	public String toString() {
		if(this.next == null) {
			if(this.degre == 0) return ""+this.coef;
			if(this.degre == 1) {
				if(this.coef == 1)return "x";
				return this.coef+"x";
			}
			return this.coef+"x^"+this.degre;
		}else {
			if(this.degre == 0) return this.coef+" + "+this.next.toString();
			if(this.degre == 1) {
				if(this.coef == 1)return this.coef+"x"+" + "+this.next.toString();
			}
			return this.coef+"x^"+this.degre+" + "+this.next.toString();
		}
	}
	
}
