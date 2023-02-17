/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7;

import com.google.common.base.Preconditions;

public class Index8Bit {

  private short[] digits = {0, 0, 0, 0, 0, 0, 0, 0};

  public Index8Bit() {
  }

  public Index8Bit(short... flags) {
    Preconditions.checkNotNull(flags);
    Preconditions.checkArgument(flags.length == 8);
    for (int i = 0; i < flags.length; i++) {
      digits[i] = flags[i];
    }
  }

  public int toInt() {
    int factor = 1;
    int value = 0;
    for (int i = 0; i < 8; i++) {
      value += factor * digits[i];
      factor *= 2;
    }
    return value;
  }

  public short get(int index) {
    return digits[index];
  }

  public String bitString() {
    String s = "";
    for (int i = 0; i < digits.length; i++) {
      s += digits[i];
    }
    return s;
  }

  /**
   * Convert a binary 8bit number into a String for print-out.
   */
  public String toString() {
    String s = bitString();
    s += " (" + toInt() + ")";
    return s;
  }

  public void set(int index, short value) {
    digits[index] = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Index8Bit index8Bit = (Index8Bit) o;
    return toInt() == index8Bit.toInt();
  }

  @Override
  public int hashCode() {
    return toInt();
  }

  public void fromString(String text) {
    text = text.trim();
    Preconditions.checkArgument(text.length() == 8);
    if (text.length() != 8) {
      return;
    }
    for (int i = 0; i < 8; i++) {
      if (text.charAt(i) == '0') {
        set(i, (short) 0);
      } else if (text.charAt(i) == '1') {
        set(i, (short) 1);
      } else {
        System.out.println("Failed to parse text");
        return;
      }
    }
  }

  public void fromInt(int indexAsInt) {
    Preconditions.checkArgument(indexAsInt >= 0 && indexAsInt <= 255);
    if (indexAsInt < 0 || indexAsInt > 255) {
      throw new IllegalArgumentException("Invalid input number.");
    }
    for (int i = 7; i >= 0; i--) {
      int factor = (int) Math.pow(2, i);
      if (factor <= indexAsInt) {
        digits[i] = 1;
        indexAsInt -= factor;
      } else {
        digits[i] = 0;
      }
    }
  }

  public Index8Bit mapWith(int[] map) {
    return new Index8Bit(
            digits[map[0]],
            digits[map[1]],
            digits[map[2]],
            digits[map[3]],
            digits[map[4]],
            digits[map[5]],
            digits[map[6]],
            digits[map[7]]);
  }
}
