package map.world.view;

public class OpcPixel {

  // Do not allow instantiation - all methods are static
  private OpcPixel() {}

  // Package RGB values into a single integer
  public static int makePixel(int red, int green, int blue) {
    assert red >=0 && red <= 255;
    assert green >=0 && green <= 255;
    assert blue >=0 && blue <= 255;
    
    int r = red & 0x000000FF;
    int g = green & 0x000000FF;
    int b = blue & 0x000000FF;
    return (r << 16) | (g << 8) | (b) ;
  }

  // Make a greyscale pixel with brightness given
  public static int makeWhite(int white) {
    return makePixel(white, white, white);
  }

  // Extract the red component from a colour integer
  public static int getRed(int colour) {
    return (colour >> 16) & 0x000000FF;
  }

  // Extract the green component from a colour integer
  public static int getGreen(int colour) {
    return (colour >> 8) & 0x000000FF;
  }

  // Extract the blue component from a colour integer
  public static int getBlue(int colour) {
    return colour & 0x000000FF;
  }

  // Return a colour that has been faded by the given brightness
  public static int fadePixel(int pixel, int brightness) {
    int r = getRed(pixel);
    int g = getGreen(pixel);
    int b = getBlue(pixel);

    // Fade the values and return new faded colour
    r = (r * brightness / 256);
    g = (g * brightness / 256);
    b = (b * brightness / 256);
    return makePixel(r, g, b);
  }
  
}
