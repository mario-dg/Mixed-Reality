/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7.volumeviz;

import com.google.common.base.Preconditions;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import misc.AssetPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mixedreality.lab.exercise7.volumeviz.VolumeDatasetMetaInformation.datasets;

/**
 * Container for the data in a volume dataset
 */
public class VolumeData {

  /**
   * There exist three versions of slice stacks - one for each orientation
   */
  public enum Orientation {
    X_NEG, X_POS, Y_NEG, Y_POS, Z_NEG, Z_POS
  }

  /**
   * Volume data as byte-array.
   */
  private byte[] data = null;

  /**
   * Tmp data structure
   */
  private final byte[] colorBuffer = new byte[4];

  /**
   * The texture stacks are precomputed at load time.
   */
  private final List<List<Texture>> textureStack;

  /**
   * Meta information for the currently loaded dataset
   */
  private VolumeDatasetMetaInformation metaInformation;

  public VolumeData() {
    metaInformation = null;
    textureStack = new ArrayList<>();
  }

  /**
   * Returns the texture stack in back-to-front order for the given orientation.
   */
  public List<Texture> getTextureStack(Orientation orientation) {
    return textureStack.get(orientation.ordinal());
  }

  /**
   * Load a volume dataset from a raw uncompressed data file.
   */
  public void loadVoxFromFile(VolumeDatasetMetaInformation.DatasetId id) {
    Preconditions.checkNotNull(id, "Invalid dataset id");
    for (VolumeDatasetMetaInformation volumeDatasetMetaInformation : datasets) {
      if (volumeDatasetMetaInformation.getId().equals(id)) {
        this.metaInformation = volumeDatasetMetaInformation;
        break;
      }
    }

    Path path = Paths.get(AssetPath.getInstance().getPathToAsset(this.metaInformation.getFilename()));
    try {
      data = Files.readAllBytes(path);
    } catch (IOException e) {
      System.out.println("Failed to read byte volume data!");
      return;
    }

    makeTextures();

    if (data.length != metaInformation.getResolution(0) * metaInformation.getResolution(1) * metaInformation.getResolution(2)) {
      System.out.println("Data size and resolution to not match!");
    } else {
      System.out.println("Successfully read dataset " + this.metaInformation.getId() + " of size " +
              metaInformation.getResolution(0) + "x" +
              metaInformation.getResolution(1) + "x" +
              metaInformation.getResolution(2));
    }
  }

  /**
   * Retreive a byte from the dataset
   */
  public byte getData(int x, int y, int z) {
    int index = z * metaInformation.getResolution(0) * metaInformation.getResolution(1) + y * metaInformation.getResolution(0) + x;
    return data[index];
  }

  /**
   * This simple transfer function computes a RGBA value for a data byte value.
   */
  private byte[] transferFunction(byte value) {
    colorBuffer[0] = value;
    colorBuffer[1] = value;
    colorBuffer[2] = 0;
    colorBuffer[3] = value;
    return colorBuffer;
  }

  /**
   * Generate a stack of textures for a given direction
   */
  private void makeTextures() {
    textureStack.clear();
    for (Orientation orientation : Orientation.values()) {
      if (orientation == Orientation.X_POS || orientation == Orientation.X_NEG) {
        var sliceStack = makeSliceStackX(orientation != Orientation.X_NEG);
        textureStack.add(sliceStack);
      } else if (orientation == Orientation.Y_POS || orientation == Orientation.Y_NEG) {
        var sliceStack = makeSliceStackY(orientation != Orientation.Y_NEG);
        textureStack.add(sliceStack);
      } else if (orientation == Orientation.Z_POS || orientation == Orientation.Z_NEG) {
        var sliceStack = makeSliceStackZ(orientation != Orientation.Z_POS);
        textureStack.add(sliceStack);
      }
    }
  }

  /**
   * Generate the texture stack along the x-direction.
   */
  private List<Texture> makeSliceStackX(boolean reverse) {
    var textures = new ArrayList<Texture>();
    int numSlices = metaInformation.getResolution(0);
    int resU = metaInformation.getResolution(1);
    int resV = metaInformation.getResolution(2);
    for (int sliceIndex = 0; sliceIndex < numSlices; sliceIndex++) {
      var byteData = new byte[resU * resV * 4];
      for (int v = 0; v < resV; v++) {
        for (int u = 0; u < resU; u++) {
          byte data = getData(sliceIndex, u, v);
          int index = v * resU + u;
          writeBytes(byteData, index, data);
        }
      }
      var buffer = BufferUtils.createByteBuffer(byteData);
      var image = new Image(Image.Format.RGBA8, resU, resV, buffer, ColorSpace.Linear);
      var tex = new Texture2D(image);
      textures.add(tex);
    }
    if (reverse) {
      Collections.reverse(textures);
    }
    return textures;
  }

  /**
   * Generate the texture stack along the y-direction.
   */
  private List<Texture> makeSliceStackY(boolean reverse) {
    var textures = new ArrayList<Texture>();
    int numSlices = metaInformation.getResolution(1);
    int resU = metaInformation.getResolution(0);
    int resV = metaInformation.getResolution(2);
    for (int sliceIndex = 0; sliceIndex < numSlices; sliceIndex++) {
      var byteData = new byte[resU * resV * 4];
      for (int v = 0; v < resV; v++) {
        for (int u = 0; u < resU; u++) {
          byte data = getData(u, sliceIndex, v);
          int index = v * resU + u;
          writeBytes(byteData, index, data);
        }
      }
      var buffer = BufferUtils.createByteBuffer(byteData);
      var image = new Image(Image.Format.RGBA8, resU, resV, buffer, ColorSpace.Linear);
      var tex = new Texture2D(image);
      textures.add(tex);
    }
    if (reverse) {
      Collections.reverse(textures);
    }
    return textures;
  }

  /**
   * Generate the texture stack along the z-direction.
   */
  private List<Texture> makeSliceStackZ(boolean reverse) {
    var textures = new ArrayList<Texture>();
    int numSlices = metaInformation.getResolution(2);
    int resU = metaInformation.getResolution(0);
    int resV = metaInformation.getResolution(1);
    for (int sliceIndex = 0; sliceIndex < numSlices; sliceIndex++) {
      var byteData = new byte[resU * resV * 4];
      for (int v = 0; v < resV; v++) {
        for (int u = 0; u < resU; u++) {
          byte data = getData(u, v, numSlices - sliceIndex - 1);
          int index = v * resU + u;
          writeBytes(byteData, index, data);
        }
      }
      var buffer = BufferUtils.createByteBuffer(byteData);
      var image = new Image(Image.Format.RGBA8, resU, resV, buffer, ColorSpace.Linear);
      var tex = new Texture2D(image);
      textures.add(tex);
    }
    if (reverse) {
      Collections.reverse(textures);
    }
    return textures;
  }

  /**
   * Write the density via transfer function to byte array.
   */
  private void writeBytes(byte[] byteData, int index, byte data) {
    byte[] f = transferFunction(data);
    byte r = f[0];
    byte g = f[1];
    byte b = f[2];
    byte a = f[3];
    byteData[index * 4] = r;
    byteData[index * 4 + 1] = g;
    byteData[index * 4 + 2] = b;
    byteData[index * 4 + 3] = a;
  }
}
