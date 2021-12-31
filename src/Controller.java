package projet;

import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Controller {
	private static View view;
	private static Model model;
	static Scanner scan = new Scanner(System.in);	
	public synchronized static void startProgram() throws NumberFormatException, Exception {
		System.out.println("Type 1 for graphic mode, and 2 for terminal mode:");
		try {
			int value = scan.nextInt();
			if(value==1) {
				view = new View();
				View.sub.addActionListener(e ->{
					try {
						model = new Model(View.getPoly().getText(), View.getC().getText(), View.getPlane().getText());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					view.paintIt(Model.createImage(model.r,model.p,model.c,1000,View.getJorm().getText().charAt(0)));
				});
			}else if (value==2) {
				System.out.println("Enter the coordinates of the plane we are working on using one of the following formats:\n1) x,pas\n2) x,y,pas\n3) x1,x2,y1,y2,pas");
				scan.nextLine();
				String r = scan.nextLine();
				System.out.println("Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0");
				String p = scan.nextLine();
				System.out.println("Enter a complex number of the form: a +bi");
				String c = scan.nextLine();
				model = new Model(p, c, r);
				System.out.println("Choose a name for your rendered image (press enter for using polynomial as name)");
				String n = scan.nextLine();
				System.out.println("Choose the number of iterations (1000 is default if you don't enter an integer)");
				String iter = scan.nextLine();
				int res = 1000;
				try {
					res = Integer.parseInt(iter);
				}catch(Exception e) {
					res = 1000;
				}
				if(n==""||n==" ") {
					System.out.println("Saving your rendered image under the name:"+p+c+".png");
					Model.affichage(Model.createImage(model.r,model.p,model.c,res,'j'),(p+"+("+c+")").replaceAll("\\s+",""));
				}else {
					System.out.println("Saving your rendered image under the name:"+n+".png");
					Model.affichage(Model.createImage(model.r,model.p,model.c,res,'j'),n);
				}
			}else {
				System.out.println("Wrong input!");
				startProgram();
			}
		}catch(InputMismatchException e) {
			System.out.println("Expected an integer here: Program ending.");
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		scan.close();
	}
	public static void main(String[] args) {
		//thread safety
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 try {
					startProgram();
				} catch (Exception e) {
					e.printStackTrace();
				}
	         }
		});
	}
}

//10x^100 +10x^99 +10x^98 +10x^97 +10x^96 +10x^95 +10x^94 +10x^93 +10x^92 +10x^91 +10x^90 +10x^89 +10x^88 +10x^87 +10x^86 +10x^85 +10x^84 +10x^83 +10x^82 +10x^81 +10x^80

//Complexe c1 = new Complexe();
//Polynome testa = Polynome.makePoly(0,1,null);
//Polynome test = Polynome.makePoly(2,3,testa);
//
//RectDeTravail r1 = RectDeTravail.carreDT(1.0,0.01);
//
//
//System.out.println(test.toString());
//
//Polynome test1 = Polynome.makePoly(2,5,Polynome.makePoly(1,2,Polynome.makePoly(0,7,null)));
//Polynome test2 = Polynome.parsePoly("");
//System.out.println(test1.toString());
//System.out.println(test2.toString());
