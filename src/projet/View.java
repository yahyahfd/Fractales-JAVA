package projet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

/**
 * View class: used for graphic mode
 * @author HAFID Yahya 71800678
 * @author OUBADIA Tanel 71806010
 */
public class View extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * full: the whole main panel<br/>
	 * paint: the panel that contains the image<br/>
	 * settings: the panel that contains the settings and paramaters
	 */
	private JPanel full, paint, settings;
	/**
	 * poly: the polynomial's panel<br/>
	 * c: the complex's panel<br/>
	 * plane: the plane's panel<br/>
	 * jorm: the set's panel<br/>
	 * iter: the max iteration panel<br/>
	 * btnPane: the panel containing the save and sub buttons
	 */
	private static JPanel poly, c, plane, jorm, iter, btnPane;
	/**
	 * polyPlaceholder: contains the placeholder for the polynomial's panel<br/>
	 * cPlaceholder: contains the placeholder for the complex's panel<br/>
	 * planePlaceholder: contains the placeholder for the plane's panel<br/>
	 * jormPlaceholder: contains the placeholder for the set's panel<br/>
	 * iterPlaceholder: contains the placeholder for the max iteration panel
	 */
	private static String polyPlaceholder, cPlaceholder, planePlaceholder, jormPlaceholder, iterPlaceholder;
	/**
	 * Welcome label
	 */
	private static JLabel welc;
	/**
	 * sub is used to submit the data written in all the text areas<br/>
	 * save is locked by default, and unlocked when an image is generated and is ready to be saved
	 */
	private static JButton sub, save;
	
	/**
	 * @return sub button
	 */
	public static JButton getSub() {
		return sub;
	}
	
	/**
	 * @return save button
	 */
	public static JButton getSave() {
		return save;
	}
	/**
	 * This class represents the graphic mode
	 * @throws NumberFormatException if we try to parse a string into an int/double
	 * @throws Exception all the other exceptions
	 */
	public View() throws NumberFormatException, Exception {
		setTitle("Julia and Mandelbrot sets");
		Toolkit tk = Toolkit.getDefaultToolkit();
		int x = (int) tk.getScreenSize().getWidth(); 
		int y = (int) tk.getScreenSize().getHeight();
		this.fillFrame();
		this.setSize(x,y);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
	
	/**
	 * This class is used to create a JPanel containing a JTextArea, a JScrollPane and JLabel
	 */
	private class FieldPanel extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param n text shown in the JLabel
		 * @param m maximum size of the JTextArea
		 * @param p the placeholder text used inside JTextArea
		 * @param b if true, JTextArea is editable
		 */
		private FieldPanel(String n, int m, String p, boolean b) {
			super();
			this.setLayout(new GridLayout(2,1));
			this.labelTextArea(n, m, p, b);
		}
		
		/**
		 * Methode used to create and configure a JTextArea linked to a JLabel and a JScrollPane
		 * @param n text shown in the JLabel
		 * @param max maximum size of the JTextArea
		 * @param placeholder the placeholder text used inside JTextArea
		 * @param b if true, JTextArea is editable
		 */
		private void labelTextArea(String n, int max, String placeholder, boolean b) {
			JTextArea txt = new JTextArea(placeholder,3,25);
			txt.setDocument(new TextFieldMax(max));
			txt.setForeground(Color.GRAY);
			txt.setLineWrap(true);
			txt.setWrapStyleWord(true);
			txt.setEditable(b);
			txt.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					if(txt.getText().equals(placeholder)) {
						txt.setText("");
						txt.setForeground(Color.BLACK);
					}
				}
				@Override
				public void focusLost(FocusEvent e) {
					if(txt.getText().isEmpty()) {
						txt.setText(placeholder);
						txt.setForeground(Color.GRAY);
					}
				}
			});
			this.add(new JLabel(n));
			JScrollPane ptxt = new JScrollPane(txt);
			ptxt.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(ptxt, BorderLayout.PAGE_START);
		}
	}
	
	/**
	 * This class is used to create a panel that displays a Buffered Image. 
	 * It includes a zooming functionality
	 */
	private class PaintPanel extends JPanel implements MouseWheelListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * The zoom: by default is 1
		 */
		private double zoom = 1;
		/**
		 * The x coordinate to which we zoom
		 */
		private double xM = this.getWidth()/2;
		/**
		 * The y coordinate to which we zoom
		 */
		private double yM = this.getHeight()/2;
		/**
		 * The image shown
		 */
		private BufferedImage img;
		
		/**
		 * Constructor for the paint panel
		 * @param i the image to add to this panel
		 */
		private PaintPanel(BufferedImage i){
			this.img = i;
			addMouseWheelListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			super.paintComponent(g2D);
	        AffineTransform at = g2D.getTransform();
	        at.translate(xM, yM);
	        at.scale(zoom, zoom);
	        at.translate(-xM, -yM);
	        g2D.setTransform(at);
		    g2D.drawImage(img, 0, 0,this.getWidth(),this.getHeight(), this);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			//Zoom in
	        if(e.getWheelRotation()<0){
				xM = e.getX();
	            yM = e.getY();
	            this.setZoom(1.1*this.getZoom());
	            this.repaint();
	        }
	        //Zoom out
	        if(e.getWheelRotation()>0){
	        	this.setZoom(this.getZoom()/1.1);
	        	if(this.getZoom()<1) this.setZoom(1);
	            this.repaint();
	        }
		}
		
		/**
		 * Getter for zoom value
		 * @return zoom value
		 */
		public double getZoom() {
			return zoom;
		}
		
		/**
		 * Setter for zoom size
		 * @param z new zoom value
		 */
		public void setZoom(double z) {
			if(z<zoom) zoom = zoom/1.1;
			else if (z<1) zoom = 1;
			else zoom = z;
		}
	}
	
	/**
	 * This class is used as a size limiter for our textfields inside
	 * {@link PaintPanel}
	 */
	private class TextFieldMax extends PlainDocument{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * The maximum size
		 */
		private int maxsize;
		/**
		 * This method helps creating a text field with a max character count
		 * @param m the max char count we want
		 */
		private TextFieldMax(int m) {
			super();
			this.maxsize = m;
		}
		public void insertString(int offset, String text, AttributeSet attr) throws BadLocationException{
			if(text==null) {
				return;
			}
			if(((getLength() + text.length()) <= maxsize) || maxsize == -1) {
				super.insertString(offset, text, attr);
			}else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}
	
	/**
	 * This method is used to fill the whole frame
	 * @throws NumberFormatException if we try to parse a string to an int/double
	 * @throws Exception all the other exceptions
	 * @see PaintPanel
	 * @see FieldPanel
	 */
	public void fillFrame() throws NumberFormatException, Exception {
		full = new JPanel();//whole window panel
		
		polyPlaceholder = "<html>Enter a polynomial of the form:<br/>ax^n +by^(n-1) +... +cx^0</html>";
		cPlaceholder = "<html>Enter a complex number of the form:<br/> a +bi</html>";
		planePlaceholder = "<html>Enter the coordinates of the plane<br/>following one of these formats:<br/>1) x,pas<br/>2) x,y,pas<br/>3) x1,x2,y1,y2,pas</html>";
		jormPlaceholder = "Enter 'j' for Julia, or 'm' for Mandelbrot";
		iterPlaceholder = "Enter the number of iterations";
		
		poly = new FieldPanel(polyPlaceholder, -1, "1x^2", true);
		getPoly().append("1x^2");
		c = new FieldPanel(cPlaceholder,-1,"0.285 +0.013i", true);
		getC().append("0.285 +0.013i");
		plane = new FieldPanel(planePlaceholder,-1,"1.25,0.001", true);
		getPlane().append("1.25,0.001");
		jorm = new FieldPanel(jormPlaceholder, 1,"j", true);
		getJorm().append("j");
		iter = new FieldPanel(iterPlaceholder, -1, "1000", true);
		getIter().append("1000");
		welc = new JLabel("Welcome");
		sub = new JButton("Submit");
		save = new JButton("Save");
		save.setEnabled(false);
		btnPane = new JPanel(new FlowLayout());
		btnPane.add(sub);
		btnPane.add(save);
		

		settings = new JPanel();

		//tmp borders (remove later)
		poly.setBorder(BorderFactory.createLineBorder(Color.red));
		full.setBorder(BorderFactory.createLineBorder(Color.blue));
		c.setBorder(BorderFactory.createLineBorder(Color.pink));
		plane.setBorder(BorderFactory.createLineBorder(Color.yellow));
		jorm.setBorder(BorderFactory.createLineBorder(Color.gray));
		iter.setBorder(BorderFactory.createLineBorder(Color.pink));
		settings.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//making settings panel
		settings.setLayout(new GridBagLayout());
		GridBagConstraints cons1 = new GridBagConstraints();
		cons1.insets = new Insets(20,0,20,0);
		settings.add(welc, cons1);
		cons1.gridy = 1;
		settings.add(poly, cons1);
		cons1.gridy = 2;
		settings.add(c, cons1);
		cons1.gridy = 3;
		settings.add(plane,cons1);
		cons1.gridy = 4;
		settings.add(jorm,cons1);
		cons1.gridy = 5;
		settings.add(iter,cons1);
		cons1.gridy = 6;
		settings.add(btnPane, cons1);
		
		full.setLayout(new GridBagLayout());
		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.fill = GridBagConstraints.BOTH;
		cons2.weighty = 1; //100% height of window
		cons2.weightx = 0.95; //75% window width
		paint = new JPanel();
		full.add(paint, cons2);
		cons2.weightx = 0.05; //25% window width
		cons2.gridx = 1;
		full.add(settings,cons2);
		this.add(full);
	}
	/**
	 * This method is used to add the image i to our painting panel inside {@link #fillFrame()}
	 * @param i the image to add
	 * @see PaintPanel
	 */
	public void paintIt(BufferedImage i) {
		full.removeAll();
		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.fill = GridBagConstraints.BOTH;
		cons2.weighty = 1; //100% height of window
		cons2.weightx = 0.95; //75% window width
		paint = new PaintPanel(i);
		full.add(paint, cons2);
		cons2.weightx = 0.05; //25% window width
		cons2.gridx = 1;
		full.add(settings,cons2);
		this.revalidate();
		this.repaint();
	}
	/**
	 * Gets a JTextArea from a JScrollPane
	 * @param p panel containing the JScrollPane
	 * @return The JTextArea contained in this panel p
	 */
	private static JTextArea jscrollpaneText (JPanel p) {
		JViewport n = ((JScrollPane) p.getComponent(1)).getViewport();
		JTextArea l = (JTextArea) n.getView();
		return l;
	}
	
	/**
	 * @return the text area containing our complex's data
	 */
	public static JTextArea getC() {
		return jscrollpaneText(c);
	}
	
	/**
	 * @return the text area containing our polynomial's data
	 */
	public static JTextArea getPoly() {
		return jscrollpaneText(poly);
	}
	
	/**
	 * @return the text area containing our plane's data
	 */
	public static JTextArea getPlane() {
		return jscrollpaneText(plane);
	}
	
	/**
	 * @return the text area containing the value 'j' for Julia or 'm' for Mandelbrot
	 */
	public static JTextArea getJorm() {
		return jscrollpaneText(jorm);
	}
	
	/**
	 * @return the text area containing the value of maximum iterations
	 */
	public static JTextArea getIter() {
		return jscrollpaneText(iter);
	}
}
