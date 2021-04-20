package mixedreality.lab.base.sprites;

import mixedreality.lab.base.ui.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to support the import of an animates sprite from a sprite sheet.
 */
public class SpriteAnimationImporter {

  /**
   * A 3D index.
   */
  public static class Idx {
    /**
     * Koordinates.
     */
    int i, j;

    public Idx(int i, int j) {
      this.i = i;
      this.j = j;
    }
  }

  /**
   * Orientation type.
   */
  public enum Orientation {HORIZONTAL, VERTICAL}

  /**
   * POJO for the sprite sheet import parameters.
   */
  public static class ImportParams {

    /**
     * Filename of the image
     */
    public String filename;
    /**
     * Unique ID for the animation.
     */
    public String animationId;
    /**
     * Width of a single sprite image.
     */
    public int spriteWidth;
    /**
     * Height of a single sprite image.
     */
    public int spriteHeight;
    /**
     * "2D Index of the first sprite image (usually 0,0)
     */
    public Idx startIdx;
    /**
     * Orientation of the images in the sheet.
     */
    public Orientation orientation;
    /**
     * Number of frames in the animation.
     */
    public int numFrames;
    /**
     * This flag is used to flip all sprite images.
     */
    public boolean flip;

    public ImportParams(String filename,
                        String animationId,
                        int spriteWidth,
                        int spriteHeight,
                        Idx startIdx,
                        Orientation orientation,
                        int numFrames,
                        boolean flip) {
      this.filename = filename;
      this.animationId = animationId;
      this.spriteWidth = spriteWidth;
      this.spriteHeight = spriteHeight;
      this.startIdx = startIdx;
      this.orientation = orientation;
      this.numFrames = numFrames;
      this.flip = flip;
    }

    public ImportParams(String filename,
                        String animationId,
                        int spriteWidth,
                        int spriteHeight,
                        Idx startIdx,
                        Orientation orientation,
                        int numFrames) {
      this(filename, animationId, spriteWidth,
              spriteHeight, startIdx, orientation,
              numFrames, false);
    }
  }

  /**
   * Import animations.
   */
  public static AnimatedSprite importAnimatedSprite(ImportParams... importParams) {
    float aspect = importParams[0].spriteWidth / (float) importParams[0].spriteHeight;
    AnimatedSprite sprite = new AnimatedSprite(aspect);
    for (ImportParams params : importParams) {
      String path = "src/main/resources/" + params.filename;
      try {
        BufferedImage image = ImageIO.read(new File(path));
        int x = params.startIdx.i * params.spriteWidth;
        int y = params.startIdx.j * params.spriteHeight;
        List<BufferedImage> frameImages = new ArrayList<>();
        for (int frame = 0; frame < params.numFrames; frame++) {
          BufferedImage frameImage = params.flip ?
                  flip(image.getSubimage(x, y,
                          Math.min(params.spriteWidth, image.getWidth() - x), params.spriteHeight)) :
                  image.getSubimage(x, y,
                          Math.min(params.spriteWidth, image.getWidth() - x), params.spriteHeight);
          frameImages.add(frameImage);
          switch (params.orientation) {
            case HORIZONTAL:
              x += params.spriteWidth;
              break;
            case VERTICAL:
              y += params.spriteHeight;
          }
        }
        sprite.add(params.animationId, frameImages);
      } catch (IOException e) {
        Logger.getInstance().error("Failed to load sprites from file " + path);
        e.printStackTrace();
      }
    }
    return sprite;
  }

  /**
   * Flip an image.
   */
  private static BufferedImage flip(BufferedImage image) {
    AffineTransform at = new AffineTransform();
    at.concatenate(AffineTransform.getScaleInstance(-1, 1));
    at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
    return createTransformed(image, at);
  }

  /**
   * Create a transformed version of an image.
   */
  private static BufferedImage createTransformed(
          BufferedImage image, AffineTransform at) {
    BufferedImage newImage = new BufferedImage(
            image.getWidth(), image.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = newImage.createGraphics();
    g.transform(at);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }
}
