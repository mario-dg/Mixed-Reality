package mixedreality.project;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

/**
 * This class is used to test to OpenCV-Bindings
 * <p>
 * Installation: https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html
 * <p>
 * Beispiel: https://www.baeldung.com/java-opencv
 */
public class OpenCVTest {

  /**
   * Load an image.
   */
  public static Mat loadImage(String imagePath) {
    Imgcodecs imageCodecs = new Imgcodecs();
    return imageCodecs.imread(imagePath);
  }

  /**
   * Save an image.
   */
  public static void saveImage(Mat imageMatrix, String targetPath) {
    Imgcodecs imgcodecs = new Imgcodecs();
    imgcodecs.imwrite(targetPath, imageMatrix);
  }

  public static void main(String[] args) {
    // Load shared OpenCV libraries - must be done first
    System.loadLibrary("opencv_java452");

    // Minimal test
    Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
    System.out.println("mat = " + mat.dump());

    // Advanced test: detect face in image, save result to other image
    final String inputImage = "src/main/resources/opencv/faceDetectionInput.jpg";
    final String outputImage = "src/main/resources/opencv/faceDetectionOutput.jpg";
    final String classifiedFilename = "src/main/resources/opencv/haarcascade_frontalface_alt.xml";
    Mat loadedImage = loadImage(inputImage);
    MatOfRect facesDetected = new MatOfRect();
    CascadeClassifier cascadeClassifier = new CascadeClassifier();
    int minFaceSize = Math.round(loadedImage.rows() * 0.1f);
    cascadeClassifier.load(classifiedFilename);
    cascadeClassifier.detectMultiScale(loadedImage,
            facesDetected,
            1.1,
            3,
            Objdetect.CASCADE_SCALE_IMAGE,
            new Size(minFaceSize, minFaceSize),
            new Size()
    );
    Rect[] facesArray = facesDetected.toArray();
    for (Rect face : facesArray) {
      Imgproc.rectangle(loadedImage, face.tl(), face.br(), new Scalar(0, 0, 255), 3);
    }
    saveImage(loadedImage, outputImage);
  }

}
