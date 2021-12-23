package projet;

import java.awt.*;
import javax.swing.*;

public class View {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("PolyFX");
	    Container pane = frame.getContentPane();
	    pane.setLayout(new BorderLayout());


	    JTextField fn = new JTextField();
	    JButton button = new JButton("Click!");
	    pane.add(fn, BorderLayout.NORTH);
	    pane.add(button, BorderLayout.SOUTH);
		frame.pack();
		JLabel label = new JLabel("");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    button.addActionListener(e ->{
	      	try {
					Polynome res = Polynome.parsePoly(fn.getText());
					label.setText(res.toString());
					frame.add(label);
					frame.pack();
					SwingUtilities.updateComponentTreeUI(frame);
				} catch (Exception e1) {
					label.setText("Wrong polynome syntax: Please respect the \"ax^n +by^(n-1) +... +cx^0\" syntax.");
					e1.printStackTrace();
					frame.add(label);
					frame.pack();
					SwingUtilities.updateComponentTreeUI(frame);
				}
	      });
	}
}
