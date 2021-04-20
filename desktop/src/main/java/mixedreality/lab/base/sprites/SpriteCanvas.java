package mixedreality.lab.base.sprites;

import com.jme3.math.Vector2f;

import javax.swing.*;
import java.awt.*;

/**
 * Drawing canvas for an animated sprite.
 */
public class SpriteCanvas extends JPanel {

  /**
   * Sprite used.
   */
  private AnimatedSprite sprite;

  /**
   * Current moving direction (left or right)
   */
  private boolean direction;

  public SpriteCanvas(int width, int height) {
    setPreferredSize(new Dimension(width, height));
    setSize(width, height);
    this.sprite = null;
    this.direction = true;

    // Load dragon sprite
    sprite = loadDragon();

    setToolTipText("Here be dragons.");
    setOpaque(true);
    setBackground(new Color(0, 0, 0, 0));
  }

  private AnimatedSprite loadDragon() {
    SpriteAnimationImporter.ImportParams[] dragonParams =
            new SpriteAnimationImporter.ImportParams[8];
    for (int i = 0; i < 8; i++) {
      dragonParams[i] =
              new SpriteAnimationImporter.ImportParams("Sprites/reddragonfly.png",
                      Constants.WALK_ANIMATION_IDS[i],
                      205,
                      161,
                      new SpriteAnimationImporter.Idx(0, 0),
                      SpriteAnimationImporter.Orientation.HORIZONTAL,
                      8);
    }

    // West
    dragonParams[2] =
            new SpriteAnimationImporter.ImportParams("Sprites/reddragonfly.png",
                    Constants.WALK_ANIMATION_IDS[2],
                    205,
                    161,
                    new SpriteAnimationImporter.Idx(0, 0),
                    SpriteAnimationImporter.Orientation.HORIZONTAL,
                    8, true);

    // East
    dragonParams[6] = new SpriteAnimationImporter.ImportParams("Sprites/reddragonfly.png",
            Constants.WALK_ANIMATION_IDS[6],
            205,
            161,
            new SpriteAnimationImporter.Idx(0, 0),
            SpriteAnimationImporter.Orientation.HORIZONTAL,
            8, false);

    return SpriteAnimationImporter.importAnimatedSprite(dragonParams);
  }

  /**
   * Load an animated sprite from a sprite sheet (default structure with all animations)
   */
  private AnimatedSprite loadSprite(String imageFilename, int spriteWidth, int spriteHeight) {
    return SpriteAnimationImporter.importAnimatedSprite(
            new SpriteAnimationImporter.ImportParams(imageFilename,
                    "WALK_N",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(0, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_NE",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(1, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_E",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(2, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_SE",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(3, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_S",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(4, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_NW",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(1, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5,
                    true),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_W",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(2, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5,
                    true),
            new SpriteAnimationImporter.ImportParams(
                    imageFilename,
                    "WALK_SW",
                    spriteWidth,
                    spriteHeight,
                    new SpriteAnimationImporter.Idx(3, 0),
                    SpriteAnimationImporter.Orientation.VERTICAL,
                    5,
                    true));
  }

  @Override
  public void paint(Graphics g) {
    g.clearRect(0, 0, getWidth(), getHeight());
    sprite.draw(g);
  }

  public void loop() {
    int minX = Constants.renderWidth / 2;
    int maxX = getWidth() - Constants.renderWidth / 2;
    if (sprite.getPos().x < minX) {
      sprite.getPos().x = minX;
      direction = true;
      sprite.setAnimationId("WALK_E");
    } else if (sprite.getPos().x > maxX) {
      sprite.getPos().x = maxX;
      direction = false;
      sprite.setAnimationId("WALK_W");
    }
    Vector2f dir = new Vector2f(direction ? 1 : -1, 0);
    sprite.setPos(sprite.getPos().add(dir.mult(Constants.SPEED)));
    sprite.getPos().y = getHeight() / 2;
  }
}
