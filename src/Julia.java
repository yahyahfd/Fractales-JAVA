package projet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Julia {
	
	public static void affichage(BufferedImage img, String dirname) {

		File f1 = new File(dirname+".png");
		try {
			ImageIO.write(img, "PNG", f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static BufferedImage createImage(RectDeTravail rec, Polynome p, Complexe c, int max_iter, char JORM) {
		BufferedImage img = new BufferedImage(rec.NbPixelX()+1,rec.NbPixelY()+1, BufferedImage.TYPE_INT_RGB);
		int ind;
		int max_div = 0;
		int xx = 0;
		
		for(Double i=rec.x1;i<rec.x2;i+=rec.pas) {
			int yy = 0;
			for(Double j=rec.y2;j>rec.y1;j-=rec.pas) {
				Complexe tmp = new Complexe(i,j);
				if(JORM == 'j') {
					ind = tmp.divergence(p,c,max_iter);
				}else  {
					ind = c.divergence(p, tmp, max_iter);
				}
				if(max_div < ind && ind != max_iter)max_div = ind;
				//System.out.println(tmp.toString()+" : "+ind);
				if(ind < max_iter) {
					int shade = ((255*ind)/(max_div+1)|0|0);
					img.setRGB(xx, yy, shade );
				}
				if(ind == max_iter) {
					img.setRGB(xx,yy, (0|0|0) );
				}
				yy++;
			}
			xx++;
		}
		System.out.println(max_div);
		return img;
	}
	
}