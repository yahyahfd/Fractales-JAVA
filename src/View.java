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
	private JPanel full, paint, settings;
	private static JPanel poly, c, plane, jorm, iter;
	private static String polyPlaceholder, cPlaceholder, planePlaceholder, jormPlaceholder, iterPlaceholder;
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
		settings.add(sub, cons1);
		
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
	
	public static JTextArea getJorm() {
		return jscrollpaneText(jorm);
	}
	
	public static JTextArea getIter() {
		return jscrollpaneText(iter);
	}
}
