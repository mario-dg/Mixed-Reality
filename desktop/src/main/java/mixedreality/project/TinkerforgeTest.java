package mixedreality.project;

import com.tinkerforge.*;

import java.io.IOException;

public class TinkerforgeTest {

  private static final String HOST = "localhost";
  private static final int PORT = 4223;

  // Change XYZ to the UID of your IMU Bricklet 3.0
  //private static final String UID = "XYZ";
  private static final String UID = "Sbf";

  public static void main(String[] args) {
    IPConnection ipcon = new IPConnection(); // Create IP connection
    BrickletIMUV3 imu = new BrickletIMUV3(UID, ipcon); // Create device object

    try {
      ipcon.connect(HOST, PORT); // Connect to brickd

      // Add all data listener
      imu.addAllDataListener(new BrickletIMUV3.AllDataListener() {
        public void allData(int[] acceleration, int[] magneticField,
                            int[] angularVelocity, int[] eulerAngle, int[] quaternion,
                            int[] linearAcceleration, int[] gravityVector,
                            int temperature, int calibrationStatus) {
          System.out.println("Acceleration [X]: " + acceleration[0] / 100.0 + " m/s²");
          System.out.println("Acceleration [Y]: " + acceleration[1] / 100.0 + " m/s²");
          System.out.println("Acceleration [Z]: " + acceleration[2] / 100.0 + " m/s²");
          System.out.println("Magnetic Field [X]: " + magneticField[0] / 16.0 + " µT");
          System.out.println("Magnetic Field [Y]: " + magneticField[1] / 16.0 + " µT");
          System.out.println("Magnetic Field [Z]: " + magneticField[2] / 16.0 + " µT");
          System.out.println("Angular Velocity [X]: " + angularVelocity[0] / 16.0 + " °/s");
          System.out.println("Angular Velocity [Y]: " + angularVelocity[1] / 16.0 + " °/s");
          System.out.println("Angular Velocity [Z]: " + angularVelocity[2] / 16.0 + " °/s");
          System.out.println("Euler Angle [Heading]: " + eulerAngle[0] / 16.0 + " °");
          System.out.println("Euler Angle [Roll]: " + eulerAngle[1] / 16.0 + " °");
          System.out.println("Euler Angle [Pitch]: " + eulerAngle[2] / 16.0 + " °");
          System.out.println("Quaternion [W]: " + quaternion[0] / 16383.0);
          System.out.println("Quaternion [X]: " + quaternion[1] / 16383.0);
          System.out.println("Quaternion [Y]: " + quaternion[2] / 16383.0);
          System.out.println("Quaternion [Z]: " + quaternion[3] / 16383.0);
          System.out.println("Linear Acceleration [X]: " + linearAcceleration[0] / 100.0 + " m/s²");
          System.out.println("Linear Acceleration [Y]: " + linearAcceleration[1] / 100.0 + " m/s²");
          System.out.println("Linear Acceleration [Z]: " + linearAcceleration[2] / 100.0 + " m/s²");
          System.out.println("Gravity Vector [X]: " + gravityVector[0] / 100.0 + " m/s²");
          System.out.println("Gravity Vector [Y]: " + gravityVector[1] / 100.0 + " m/s²");
          System.out.println("Gravity Vector [Z]: " + gravityVector[2] / 100.0 + " m/s²");
          System.out.println("Temperature: " + temperature + " °C");
          System.out.println("Calibration Status: " + Integer.toBinaryString(calibrationStatus));
          System.out.println("");
        }
      });

      // Set period for all data callback to 0.1s (100ms)
      imu.setAllDataCallbackConfiguration(100, false);

      System.out.println("Press key to exit");
      System.in.read();
      ipcon.disconnect();

    } catch (NetworkException e) {
      e.printStackTrace();
    } catch (AlreadyConnectedException e) {
      e.printStackTrace();
    } catch (NotConnectedException e) {
      e.printStackTrace();
    } catch (TinkerforgeException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

