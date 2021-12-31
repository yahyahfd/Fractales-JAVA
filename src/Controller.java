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
					view.changeResult(View.getPoly().getText()+" + ("+View.getC().getText()+")");
					try {
						model = new Model(View.getPoly().getText(), View.getC().getText(), "1, 0.01");//change so we can specify
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					view.paintIt(Model.createImage(model.r,model.p,model.c,1000,'j'));
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
				if(n==""||n==" ") {
					System.out.println("Saving your rendered image under the name:"+p+c+".png");
					Model.affichage(Model.createImage(model.r,model.p,model.c,1000,'j'),(p+"+("+c+")").replaceAll("\\s+",""));
				}else {
					System.out.println("Saving your rendered image under the name:"+n+".png");
					Model.affichage(Model.createImage(model.r,model.p,model.c,1000,'j'),n);
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


//Complexe c1 = new Complexe(0.3,0.5);
//Polynome testa = Polynome.makePoly(0,1,null);
//Polynome test = Polynome.makePoly(2,3,testa);
//
//RectDeTravail r1 = RectDeTravail.carreDT(1.0,0.01);
//
//
//System.out.println(test.toString());
//
//Polynome test1 = Polynome.makePoly(2,5,Polynome.makePoly(1,2,Polynome.makePoly(0,7,null)));
//Polynome test2 = Polynome.parsePoly("5x^2 -2x^1 +7x^0");
//System.out.println(test1.toString());
//System.out.println(test2.toString());
