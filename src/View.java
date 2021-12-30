package projet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class View extends JFrame{
	private static final long serialVersionUID = 1L;

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
	
	private class PaintPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private BufferedImage img;
		
		private PaintPanel(BufferedImage i){
			this.img = i;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		    g.drawImage(img, 0, 0, null); //javadoc
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
		JPanel full = new JPanel();//whole window panel
		
		String polyPlaceholder = "Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0";
		String cPlaceholder = "Enter a complex number of the form: a +bi";
		String rPlaceholder = "Result...";
		JPanel poly = new FieldPanel("Polynome", -1, polyPlaceholder, true);
		JPanel c = new FieldPanel("C",-1,cPlaceholder, true);
		JLabel welc = new JLabel("Welcome");
		JButton sub = new JButton("Submit");
		JPanel result = new FieldPanel("Result", -1, rPlaceholder, false);

		JPanel settings = new JPanel();
		//10x^100 +10x^99 +10x^98 +10x^97 +10x^96 +10x^95 +10x^94 +10x^93 +10x^92 +10x^91 +10x^90 +10x^89 +10x^88 +10x^87 +10x^86 +10x^85 +10x^84 +10x^83 +10x^82 +10x^81 +10x^80
		
		//tmp borders (remove later)
		poly.setBorder(BorderFactory.createLineBorder(Color.red));
		full.setBorder(BorderFactory.createLineBorder(Color.blue));
		c.setBorder(BorderFactory.createLineBorder(Color.pink));
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
		settings.add(sub, cons1);
		cons1.gridy = 4;
		settings.add(result, cons1);
		
		sub.addActionListener(e ->{
			try {
				Polynome res = Polynome.parsePoly(jscrollpaneText(poly).getText());
				jscrollpaneText(result).setText(res.toString());
				SwingUtilities.updateComponentTreeUI(this);
			}catch(Exception e1) {
				jscrollpaneText(result).setText("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax. (respect the spaces)");
				e1.printStackTrace();
				SwingUtilities.updateComponentTreeUI(this);
			}
		});

		Complexe c1 = new Complexe(0.3,0.5);

		RectDeTravail r1 = new RectDeTravail(-1.0,1.0,-1.0,1.0,0.01);
		Polynome test2 = Polynome.makePoly(2,1,null);
		JPanel paint = new PaintPanel(Julia.createImage(r1,test2,c1,1000, 'j'));
		full.setLayout(new GridBagLayout());
		GridBagConstraints cons2 = new GridBagConstraints();
		cons2.fill = GridBagConstraints.BOTH;
		cons2.weighty = 1; //100% height of window
		cons2.weightx = 0.95; //75% window width
		full.add(paint, cons2);
		cons2.weightx = 0.05; //25% window width
		cons2.gridx = 1;
		full.add(settings,cons2);
		
		this.add(full);
	}
	
	public static JTextArea jscrollpaneText (JPanel p) {
		JViewport n = ((JScrollPane) p.getComponent(1)).getViewport();
		JTextArea l = (JTextArea) n.getView();
		return l;
	}
	
	
	public static void main(String[] args) {
		//thread safety
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 try {
					new View();
				} catch (Exception e) {
					e.printStackTrace();
				}
	         }
	      });
		
//		JPanel full = new JPanel();
//		JPanel paint = new JPanel();
//		
//		/* frame configuration */
//		mpane.setBackground(Color.pink);
//		paint.setBackground(Color.green);
//		paint.setBorder(BorderFactory.createLineBorder(Color.orange));
//		errors.setBackground(Color.red);
//		
//		full.setLayout(new GridBagLayout());
//		GridBagConstraints cons1 = new GridBagConstraints();
//		cons1.fill = GridBagConstraints.BOTH;
//		cons1.weighty = 1; //100% height of window
//		cons1.weightx = 1.5; //150% row <=> 75% window
//		cons1.gridheight = 2; //covers 100% height of window
//		full.add(paint, cons1);
//		cons1.gridheight = 1;
//		cons1.weightx = 0.5; //50% row <=> 25% window
//		cons1.weighty = 0.60; //60% window
//		cons1.gridx = 1;
//		full.add(mpane,cons1);
//		cons1.gridy = 1;
//		cons1.weightx = 0.5; //50% row <=> 25% window
//		cons1.weighty = 0.40; //40% window height
//		full.add(errors,cons1);
//		this.add(full);
//		//Window Size
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		int x = (int) tk.getScreenSize().getWidth(); 
//		int y = (int) tk.getScreenSize().getHeight();
//		JPanel main = new JPanel();
//		this.add(main);
//		JLabel poly = new JLabel("Polynome");
//		JTextArea ptxt = new JTextArea();
//		ptxt.setDocument(new TextFieldMax(5));
//		main.add(poly);
//		main.add(ptxt);
//		
//		/* mpane configuration */
//		//components
//		JLabel welc = new JLabel("Welcome");
//		JLabel poly = new JLabel("Polynome");
//		JLabel c = new JLabel("C");
//		
//		JTextArea ptxt = new JTextArea("Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0");
//		ptxt.setRows(5);
//		ptxt.setTabSize(20);
//		ptxt.setForeground(Color.GRAY);
//		ptxt.setLineWrap(true);
//		ptxt.setWrapStyleWord(true);
//		ptxt.addFocusListener(new FocusListener() {
//			@Override
//			public void focusGained(FocusEvent e) {
//				if(ptxt.getText().equals("Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0")) {
//					ptxt.setText("");
//					ptxt.setForeground(Color.BLACK);
//					Toolkit.getDefaultToolkit().beep();
//				}
//			}
//			@Override
//			public void focusLost(FocusEvent e) {
//				if(ptxt.getText().isEmpty()) {
//					Toolkit.getDefaultToolkit().beep();
//					ptxt.setText("Enter a polynomial of the form: ax^n +by^(n-1) +... +cx^0");
//					ptxt.setForeground(Color.GRAY);
//				}
//			}
//		});
//			
//		JTextField ctxt = new JTextField();
//		
//		JButton sub = new JButton("Submit");
//		mpane.add(welc, BorderLayout.PAGE_START);
//		mpane.add(sub, BorderLayout.PAGE_END);
		//settings
//		mpane.setLayout(new GridLayout(6,1));
//		mpane.add(welc);
//		mpane.add(poly);
//		mpane.add(ptxt);
//		mpane.add(c);
//		mpane.add(ctxt);
//		mpane.add(sub);
//		
//		cons2.ipady = 30;
//		cons2.anchor = GridBagConstraints.PAGE_START;
//        mpane.add(welc, cons2);
//        cons2.ipady = 0;
//        cons2.gridy = 1;
//        mpane.add(poly,cons2);
//        cons2.gridy = 2;
//        mpane.add(ptxt, cons2);
//        cons2.gridy = 3;
//        mpane.add(c, cons2);
//        cons2.gridy = 4;
//        mpane.add(ctxt, cons2);
//        cons2.gridy = 5;
//        mpane.add(sub, cons2);
          
//        constraints.gridx = 1;
//        constraints.gridy = 2;
//        constraints.ipady = 0;
//        mpane.add(poly, constraints); 
//        
//        constraints.gridx = 1;
//        constraints.gridy = 4;
//        mpane.add(c, constraints); 
//        
//        constraints.gridx = 0;
//        constraints.gridy = 6;
//        constraints.gridwidth = 3;
//        mpane.add(sub, constraints);
//        
//        constraints.gridx = 0;
//        constraints.gridy = 3;
//        mpane.add(ptxt, constraints);
//        
//        constraints.gridx = 0;
//        constraints.gridy = 5;
//        mpane.add(ctxt, constraints);
      
//		
//		//poly+c panel
//		JPanel data = new JPanel();	
//		GroupLayout layout = new GroupLayout(data);
//		data.setLayout(layout);
//		layout.setAutoCreateGaps(true);
//		layout.setAutoCreateContainerGaps(true);
//		//horizontal align
//		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
//		hGroup.addGroup(layout.createParallelGroup().addComponent(poly).addComponent(c));
//		hGroup.addGroup(layout.createParallelGroup().addComponent(ptxt).addComponent(ctxt));
//		layout.setHorizontalGroup(hGroup);
//		//vertical align
//		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(poly).addComponent(ptxt));
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(c).addComponent(ctxt));
//		layout.setVerticalGroup(vGroup);
//		
////		mpane.add(welc);
//		mpane.add(data);
////		mpane.add(sub);
		
		
		
		
//		//we add a main panel and make it align elements by Y axis
//		JPanel pane = new JPanel();
//		frame.add(pane);
//		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
//		
//		//new data panel that groups the text boxes and their labels
//		JPanel data = new JPanel();	
//		GroupLayout layout = new GroupLayout(data);
//		data.setLayout(layout);
//		layout.setAutoCreateGaps(true);
//		layout.setAutoCreateContainerGaps(true);
//		JLabel polynome = new JLabel("Polynome");
//		JLabel c = new JLabel("C");
//		JTextField ctxt = new JTextField();
//		JTextField ptxt = new JTextField();
//		JButton subm = new JButton("Submit");
//		JLabel result = new JLabel("");
//		JLabel empty = new JLabel("");
//		
//		
//		//horizontal align
//		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
//		hGroup.addGroup(layout.createParallelGroup().addComponent(polynome).addComponent(c));
//		hGroup.addGroup(layout.createParallelGroup().addComponent(ptxt).addComponent(ctxt));
//		layout.setHorizontalGroup(hGroup);
//		//vertical align
//		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(polynome).addComponent(ptxt));
//		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(c).addComponent(ctxt));
//		layout.setVerticalGroup(vGroup);
//		
//		//submit button + testLabel result
//		pane.add(data);
//		pane.add(subm);
//		pane.add(result);
//		
//		//button action
//		subm.addActionListener(e ->{
//			try {
//				Polynome res = Polynome.parsePoly(ptxt.getText());
//				result.setText(res.toString());
//				SwingUtilities.updateComponentTreeUI(frame);
//			}catch(Exception e1) {
//				result.setText("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
//				e1.printStackTrace();
//				SwingUtilities.updateComponentTreeUI(frame);
//			}
//		});
////				
//		//initialize frame
//		frame.add(full);
//		//window parameters
//	    frame.setSize(x,y);
//	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame.setVisible(true);
	    

	}
}
