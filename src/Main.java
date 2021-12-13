package projet;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	private static final int MAX_ITER = 1000;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage img = new BufferedImage(201,201, BufferedImage.TYPE_INT_RGB);
//		int r = 64; int g = 224; int b = 208;  //turquoise
//		int col = (r << 16) | (g << 8) | b;
//		img.setRGB(0, 0, col);
//		File f = new File("NewImg.png");
//		try {
//			ImageIO.write(img, "PNG", f);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//c = 0,3 + 0,5 i.
		Complexe c1 = new Complexe(0.3,0.5);
//		Complexe z0 =  new Complexe(-1.0,1.0);
//		//z0.JuliaIteration(c1, 10);
//		System.out.println(z0.divergence(c1));
		
		
//		Double i0 = -1.0;
//		Double j0 = 1.0;
//		Double h = 0.01;
		int r,g,b;
//		for(int i=0; i<201;i++) {
//			for(int j=0;j<201;j++) {
//				Complexe tmp = new Complexe(i0,j0);
//				int ind = tmp.divergence(c1);
//				System.out.println(tmp.toString()+" : "+ind);
//				if(ind < MAX_ITER) {
//					r = 64; g = 224; b = 208;  //turquoise
//					img.setRGB(i, j, r | g | b );
//				}
//				if(ind == MAX_ITER) {
//					r= 0; g=0; b=0;
//					img.setRGB(i, j, r | g | b );
//				}
//				
//				
//				j0-=h;
//			}
//			i0+=h;
//		}
//		File f1 = new File("FirstTry.png");
//		try {
//			ImageIO.write(img, "PNG", f1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		Double h = 0.01;
//		Complexe t = new Complexe(-1.0,1.0);
//		System.out.println(t.toString());
//		t.setRe(t.getR()+h);
//		System.out.println(t.toString());
//		System.out.println(t.divergence(c1));
		int xx = 0;
		
		for(Double i=-1.0;i<1.0;i+=h) {
			int yy = 0;
			for(Double j=1.0;j>-1.0;j-=h) {
				Complexe tmp = new Complexe(i,j);
				int ind = tmp.divergence(c1);
				System.out.println(tmp.toString()+" : "+ind);
				if(ind < MAX_ITER) {
					r = 64; g = 224; b = 208;  //turquoise
					img.setRGB(xx, yy, r | g | b );
				}
				if(ind == MAX_ITER) {
					r= 0; g=0; b=0;
					img.setRGB(xx,yy, r | g | b );
				}
				yy++;
			}
			xx++;
		}
		File f1 = new File("FirstTry.png");
		try {
			ImageIO.write(img, "PNG", f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
