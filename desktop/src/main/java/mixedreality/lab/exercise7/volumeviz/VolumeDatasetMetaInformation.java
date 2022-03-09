/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7.volumeviz;

import java.util.Arrays;
import java.util.List;

/**
 * POJO class for the meta information of a dataset
 */
public class VolumeDatasetMetaInformation {

  /**
   * Available predefinded datasets.
   */
  public enum DatasetId {NEGHIP, ENGINE, FOOT, MONKEY}

  /**
   * Id for the dataset
   */
  private DatasetId id;

  /**
   * File with the volume data
   */
  private String filename;

  /**
   * Resolution of the data.
   */
  private int[] resolution;

  public VolumeDatasetMetaInformation(DatasetId id, String filename, int resX, int rexY, int resZ) {
    this.id = id;
    this.filename = filename;
    this.resolution = new int[]{resX, rexY, resZ};
  }

  public DatasetId getId() {
    return id;
  }

  public int getResolution(int dim) {
    return resolution[dim];
  }

  public String getFilename() {
    return filename;
  }

  /**
   * Predefined datasets.
   */
  static List<VolumeDatasetMetaInformation> datasets = Arrays.asList(
          new VolumeDatasetMetaInformation(DatasetId.NEGHIP, "volumedata/neghip.raw", 64, 64, 64),
          new VolumeDatasetMetaInformation(DatasetId.ENGINE, "volumedata/engine.raw", 256, 256, 256),
          new VolumeDatasetMetaInformation(DatasetId.FOOT, "volumedata/foot.raw", 256, 256, 256),
          new VolumeDatasetMetaInformation(DatasetId.MONKEY, "volumedata/monkey.raw", 256, 256, 62));
}
