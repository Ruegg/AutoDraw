package bugpatch.master;

import java.awt.image.BufferedImage;

public class ImageHData {

	public BufferedImage image;
	public int delay;
	
	public ImageHData(BufferedImage i, int delay){
		this.image = i;
		this.delay = delay;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public int getDelay(){
		return delay;
	}
}
