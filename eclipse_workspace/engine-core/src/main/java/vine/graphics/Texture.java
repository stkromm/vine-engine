package vine.graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * A texture represents an image object that can be bound to the render
 * pipeline.
 * 
 * @author Steffen
 *
 */
public abstract class Texture {

    /**
     * 
     */
    protected int width;
    /**
     * 
     */
    protected int height;
    /**
     * Stores the id of the texture in open gl.
     */
    protected int textureId;

    /**
     * @param path
     *            The file path of the texture
     * @return the id of the texture in the graphics provider pipeline.
     */
    protected int load(final String path) {
        int[] pixels = null;
        try (FileInputStream stream = new FileInputStream(path)) {
            final BufferedImage image = ImageIO.read(stream);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            pixels = new int[0];
            Logger.getGlobal().log(Level.SEVERE, "Failed to read texture source file", e);
        }
        final int[] data = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            final int alpha = (pixels[i] & 0xff000000) >> 24;
            final int red = (pixels[i] & 0xff0000) >> 16;
            final int green = (pixels[i] & 0xff00) >> 8;
            final int blue = pixels[i] & 0xff;
            data[i] = alpha << 24 | blue << 16 | green << 8 | red;
        }
        return registrateTexture(data);
    }

    /**
     * @param data
     *            the texture data
     * @return the id of the texture that is used by the graphics provider
     */
    protected abstract int registrateTexture(int[] data);

    /**
     * 
     */
    public abstract void bind();

    /**
     * 
     */
    public abstract void unbind();

    /**
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return
     */
    public int getHeight() {
        return height;
    }
}