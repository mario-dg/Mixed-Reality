/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.base.mesh;

import com.jme3.math.ColorRGBA;

/**
 * Represents an OBJ-file material.
 */
public class ObjMaterial {
  /**
   * Name of the material.
   */
  private String name;

  /**
   * Name of the texture file (if available)
   */
  private String textureFilename;

  /**
   * Color.
   */
  private ColorRGBA color;

  public ObjMaterial(String name) {
    this.name = name;
    this.textureFilename = null;
    color = new ColorRGBA(1, 1, 1, 1);
  }

  @Override
  public String toString() {
    return name + ": " + color + ", " + textureFilename;
  }

  // +++ GETTER/SETTER +++++++++++++++++++++++++

  public String getName() {
    return name;
  }

  public ColorRGBA getColor() {
    return color;
  }

  public void setColor(ColorRGBA color) {
    this.color = color;
  }

  public String getTextureFilename() {
    return textureFilename;
  }

  public void setTextureFilename(String filename) {
    this.textureFilename = filename;
  }
}
