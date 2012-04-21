package gb;

import com.jme.image.Texture;
import com.jme.util.TextureManager;

public class TextureUtil {

	public static Texture simpleLoadTexture(String name) {
		Texture t = TextureManager.loadTexture(
                TextureUtil.class.getResource(name),
                Texture.MinificationFilter.BilinearNoMipMaps,
                Texture.MagnificationFilter.Bilinear,
                1.0f,
                true);
		return t;
	}
    public static float getUForPixel(Texture t,int xPixel) {
        return (float) xPixel / t.getImage().getWidth();
    }

    public static float getVForPixel(Texture t, int yPixel) {
        return 1f - (float) yPixel / t.getImage().getHeight();
    }
}
