package projet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Julia {
	
	public static void affichage(RectDeTravail rec,Polynome p,Complexe c,int max_iter) {
		BufferedImage img = new BufferedImage(rec.NbPixelX(),rec.NbPixelY(), BufferedImage.TYPE_INT_RGB);
		int r,g,b;
		
		int xx = 0;
		
		for(Double i=-1.0;i<1.0;i+=rec.pas) {
			int yy = 0;
			for(Double j=1.0;j>-1.0;j-=rec.pas) {
				Complexe tmp = new Complexe(i,j);
				int ind = tmp.divergence(p,c,max_iter);
				//System.out.println(tmp.toString()+" : "+ind);
				if(ind < max_iter) {
					r = 64; g = 224; b = 208;  //turquoise
					img.setRGB(xx, yy, r | g | b );
				}
				if(ind == max_iter) {
					r= 0; g=0; b=0;
					img.setRGB(xx,yy, r | g | b );
				}
				yy++;
			}
			xx++;
		}
		File f1 = new File("TryAgain.png");
		try {
			ImageIO.write(img, "PNG", f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
}
