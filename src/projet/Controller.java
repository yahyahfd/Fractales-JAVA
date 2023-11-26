package projet;


import java.awt.image.BufferedImage;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller class: contains the main and starts the program
 * @author HAFID Yahya 71800678
 * @author OUBADIA Tanel 71806010
 */
public class Controller {
	/**
	 * The view: graphic mode
	 */
	private static View view;
	/**
	 * The model: all the methods and terminal mode
	 */
	private static Model model;
	/**
	 * The scanner used to ask the user for information
	 */
	private static Scanner scan = new Scanner(System.in);
	/**
	 * r: the plane<br/>
	 * p: the polynomial<br/>
	 * c: the complex<br/>
	 * n: the name<br/>
	 * iter: the maximum iteration<br/>
	 * jorm: "j" for Julia and "m" for Mandelbrot<br/>
	 */
	private static String r, p, c, n, iter, jorm;
	/**
	 * The final buffered image
	 */
	private static BufferedImage img;
	
	/**
	 * This method starts the program and gives the user the choice between graphic mode or terminal mode. 
	 * The terminal mode keeps asking the user for input and saves the final image accordingly
	 * The graphic mode is user friendly and gives the user the choice to save the image
	 * @throws NumberFormatException if we try to parse a string to an int/double 
	 * @throws Exception all the other exceptions
	 */
	public synchronized static void startProgram() throws NumberFormatException, Exception {
		System.out.println("Type 1 for graphic mode, and 2 for terminal mode:");
		try {
			int value = scan.nextInt();
			if(value==1) {
				view = new View();
				View.getSub().addActionListener(e ->{
					try {
						p = View.getPoly().getText();
						c = View.getC().getText();
						r = View.getPlane().getText();
						model = new Model(p, c, r);
						img = Model.createImage(model.getR(),model.getP(),model.getC(),Integer.parseInt(View.getIter().getText()),View.getJorm().getText().charAt(0));
						view.paintIt(img);
						View.getSave().setEnabled(true);
					} catch (Exception e1) {
						View.getSave().setEnabled(false);
						e1.printStackTrace();
					}
				});
				View.getSave().addActionListener(e ->{
					String m = JOptionPane.showInputDialog("Choose a path to save the rendered image to: (default is "+(p+"+("+c+")_"+r).replaceAll("\\s+","")+".png)");
					try {
						if(m.isBlank()) m = (p+"+("+c+")_"+r).replaceAll("\\s+","");
						Model.affichage(img, m);
				        System.out.println("Saved to "+m+".png");
					}catch (NullPointerException e1){
						System.out.println("Did not save because user canceled the operation.");
					}
				});
			}else if (value==2) {
				System.out.println("Enter the coordinates of the plane we are working on using one of the following formats:\n1) x,step\n2) x,y,step\n3) x1,x2,y1,y2,step");
				scan.nextLine();
				r = scan.nextLine();
				System.out.println("Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0");
				p = scan.nextLine();
				System.out.println("Enter a complex number of the form: a +bi");
				c = scan.nextLine();
				model = new Model(p, c, r);
				System.out.println("Choose a name for your rendered image (press enter for using polynomial as name)");
				n = scan.nextLine();
				System.out.println("Choose the number of iterations (1000 is default if you don't enter an integer)");
				iter = scan.nextLine();
				System.out.println("Type 'j' if you want a Julia sets, 'm' if you want Mandelbrot sets (default is Julia)");
				jorm = scan.nextLine();
				if(jorm != "j" || jorm != "m") jorm = "j";
				int res = 1000;
				try {
					res = Integer.parseInt(iter);
				}catch(Exception e) {
					res = 1000;
				}
				img = Model.createImage(model.getR(),model.getP(),model.getC(),res,jorm.charAt(0));
				if(n==""||n==" ") {
					System.out.println("Saving your rendered image under the name:"+(p+"+("+c+")_"+r).replaceAll("\\s+","")+".png");
					Model.affichage(img,(p+"+("+c+")_"+r).replaceAll("\\s+",""));
				}else {
					System.out.println("Saving your rendered image under the name:"+n+".png");
					Model.affichage(img,n);
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
	/**
	 * The main method that calls the program using a safe thread
	 * @param args this is left empty
	 */
	public static void main(String[] args) {
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
