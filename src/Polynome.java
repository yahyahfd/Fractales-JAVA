package projet;


public class Polynome {
	int degre;
	int coef;
	Polynome next;
	
	public Polynome(int d,int c, Polynome n) throws Exception {
		try {
            if(d<=n.degre) {
            	throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
            }
        } catch (NullPointerException e) {
        	//this means our polynome is finished, we don't do anything
        }
		this.degre = d;
		this.coef = c;
		this.next = n;
	}
	
	public static Polynome parsePoly (String s) throws NumberFormatException, Exception {
		Polynome res = null;
		String[] tmp = s.split(" ");
		for(int i=tmp.length-1;i>=0;i--) {
			String[] tmp2 = tmp[i].split("x");
			if(tmp2.length==2) {
				res = new Polynome(Integer.parseInt(tmp2[1].substring(1)),Integer.parseInt(tmp2[0]),res);
			}else {
				throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
			}
		}
		return res;
	}
	
	public String toString(Polynome n) {
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
	
	
}