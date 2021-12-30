package projet;

public class Polynome {
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
            	throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
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
				throw new Exception("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
			}
		}
		return res;
	}
	
	public String toString() {
		if(this.coef == 0) {
			return (this.next==null)?"0"+" + c":"0"+" + "+this.next.toString();
		}else if (this.degre == 0) {
			return (this.next==null)?""+this.coef+" + c":""+this.coef+" + "+this.next.toString();
		}else {
			String pref = this.coef==1? "": this.coef==-1?"-":""+this.coef;
			String suff = this.degre>1?"^"+this.degre:"";
			return (this.next==null)?pref+"x"+suff+" + c":pref+"x"+suff+" + "+this.next.toString();
		}
		
	}
	
}
