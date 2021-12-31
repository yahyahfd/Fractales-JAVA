package projet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel full, result, paint, settings;
	private static JPanel poly, c, plane;
	private static String polyPlaceholder, cPlaceholder, rPlaceholder, planePlaceholder;
	private static JLabel welc;
	static JButton sub;
	
	public View() throws NumberFormatException, Exception {
		setTitle("Julia");
		Toolkit tk = Toolkit.getDefaultToolkit();
		int x = (int) tk.getScreenSize().getWidth(); 
		int y = (int) tk.getScreenSize().getHeight();
		this.fillFrame();
		this.setSize(x,y);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
	
	private class FieldPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private FieldPanel(String n, int m, String p, boolean b) {
			super();
			this.setLayout(new GridLayout(2,1));
			this.labelTextArea(n, m, p, b);
		}
		
		private void labelTextArea(String n, int max, String placeholder, boolean b) {
			JTextArea txt = new JTextArea(placeholder,5,25);
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
	
	private class PaintPanel extends JPanel implements MouseWheelListener{
		private static final long serialVersionUID = 1L;
		
		private BufferedImage img;
		
		private PaintPanel(BufferedImage i){
			this.img = i;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.drawImage(img, 0, 0,this.getWidth(),this.getHeight(), null);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
//			//Zoom in
//	        if(e.getWheelRotation()<0){
//	            this.setZoomFactor(1.1*this.getZoomFactor());
//	            this.repaint();
//	        }
//	        //Zoom out
//	        if(e.getWheelRotation()>0){
//	            this.setZoomFactor(mydrawer.getZoomFactor()/1.1);
//	            this.repaint();
//	        }
		}
	}
	
	private class TextFieldMax extends PlainDocument{
		private static final long serialVersionUID = 1L;
		private int maxsize;
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
	
	public void fillFrame() throws NumberFormatException, Exception {
		full = new JPanel();//whole window panel
		
		polyPlaceholder = "Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0";
		cPlaceholder = "Enter a complex number of the form: a +bi";
		rPlaceholder = "Result...";
		planePlaceholder = "Enter the coordinates of the plane we are working on using one of the following formats:\n1) x,pas\n2) x,y,pas\n3) x1,x2,y1,y2,pas";
		poly = new FieldPanel("Polynome", -1, polyPlaceholder, true);
		c = new FieldPanel("C",-1,cPlaceholder, true);
		plane = new FieldPanel("Plane",-1,planePlaceholder, true);
		welc = new JLabel("Welcome");
		sub = new JButton("Submit");
		result = new FieldPanel("Result", -1, rPlaceholder, false);

		settings = new JPanel();
		//10x^100 +10x^99 +10x^98 +10x^97 +10x^96 +10x^95 +10x^94 +10x^93 +10x^92 +10x^91 +10x^90 +10x^89 +10x^88 +10x^87 +10x^86 +10x^85 +10x^84 +10x^83 +10x^82 +10x^81 +10x^80
		
		//tmp borders (remove later)
		poly.setBorder(BorderFactory.createLineBorder(Color.red));
		full.setBorder(BorderFactory.createLineBorder(Color.blue));
		c.setBorder(BorderFactory.createLineBorder(Color.pink));
		plane.setBorder(BorderFactory.createLineBorder(Color.yellow));
		result.setBorder(BorderFactory.createLineBorder(Color.orange));
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
		settings.add(sub, cons1);
		cons1.gridy = 5;
		settings.add(result, cons1);
		sub.requestFocus();
		full.setLayout(new GridBagLayout());
		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.fill = GridBagConstraints.BOTH;
		cons2.weighty = 1; //100% height of window
		cons2.weightx = 0.95; //75% window width
		paint = new JPanel();
		paint.setBackground(Color.red);
		full.add(paint, cons2);
		cons2.weightx = 0.05; //25% window width
		cons2.gridx = 1;
		full.add(settings,cons2);
		this.add(full);
	}
	
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
	
	public void changeResult(String n) {
		try {
			jscrollpaneText(result).setText(n);
		}catch(Exception e1) {
			jscrollpaneText(result).setText("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
			e1.printStackTrace();
		}
	}
	
	private static JTextArea jscrollpaneText (JPanel p) {
		JViewport n = ((JScrollPane) p.getComponent(1)).getViewport();
		JTextArea l = (JTextArea) n.getView();
		return l;
	}
	
	public static JTextArea getC() {
		return jscrollpaneText(c);
	}
	
	public static JTextArea getPoly() {
		return jscrollpaneText(poly);
	}
	
	public static JTextArea getPlane() {
		return jscrollpaneText(plane);
	}
}
