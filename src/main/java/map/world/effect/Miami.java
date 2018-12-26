package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

import java.util.Optional;

public class Miami extends WorldMapEffect {

  private static final int FRAME_DELAY = 10;

  private double[] random_values = new double[NUM_PIXELS];

  private final long startTime = System.currentTimeMillis();

  public Miami(WorldMapView view) {
    super(view, FRAME_DELAY);

    // Generate random values
    for (int i = 0; i < NUM_PIXELS; i++) {
      random_values[i] = Math.random();
    }
  }

  // A cosine curve scaled to fit in a 0-1 range and 0-1 domain by default
  // offset: how much to slide the curve across the domain (should be 0-1)
  // period: the length of one wave
  // minn, maxx: the output range
  private double cos(double x, double offset, double period, double minn, double maxx) {
    double value = Math.cos((x / period - offset) * 0.3141529 * 2) / 2 + 0.5;
    return value * (maxx - minn) + minn;
  }

  // Expand the color values by a factor of mult around the pivot value of center
  // color: an (r, g, b) tuple
  // center: a float -- the fixed point
  // mult: a float -- expand or contract the values around the center point
  private int contrast(double r, double g, double b, double center, double mult) {
    r = (r - center) * mult + center;
    g = (g - center) * mult + center;
    b = (b - center) * mult + center;
    return OpcPixel.makePixel((int) r, (int) g, (int) b);
  }


  // Remap the float x from the range oldmin-oldmax to the range newmin-newmax
  // Does not clamp values that exceed min or max.
  // For example, to make a sine wave that goes between 0 and 256:
  // remap(math.sin(time.time()), -1, 1, 0, 256)
  private double remap(double x, double oldmin, double oldmax, double newmin, double newmax) {
    double zero_to_one = (x - oldmin) / (oldmax - oldmin);
    return zero_to_one * (newmax - newmin) + newmin;
  }

  // Restrict the float x to the range minn-maxx
  private double clamp(double x, double minn, double maxx) {
    double min = (maxx < x) ? maxx : x;
    return (minn > min) ? minn : min;
  }

  /* Compute the color of a given pixel.

    t: time in seconds since the program started.
    ii: which pixel this is, starting at 0
    coord: the (x, y, z) position of the pixel as a tuple
    n_pixels: the total number of pixels
    random_values: a list containing a constant random value for each pixel

    Returns an (r, g, b) tuple in the range 0-255
    */
  private int calculatePixel(long t, int x, int y, int ii) {
    // make moving stripes for x, y, and z
    int z = 0;
    y += cos(x + 0.2*z, 0, 1, 0, 0.6);
    z += cos(x, 0, 1, 0, 0.3);
    x += cos(y + z, 0, 1.5, 0, 0.2);

    // rotate
    int tempX = x;
    x = y;
    y = z;
    z = tempX;

    // make x, y, z -> r, g, b sine waves
    double r = cos(x, (double) t / 4, 2.5, 0, 1);
    double g = cos(y, (double) t / 4, 2.5, 0, 1);
    double b = cos(z, (double) t / 4, 2.5, 0, 1);
    int pixel = contrast(r, g, b, 0.5, 1.4);

    r = OpcPixel.getRed(pixel);
    g = OpcPixel.getGreen(pixel);
    b = OpcPixel.getBlue(pixel);

    double clampdown = (r + g + b) / 2;
    clampdown = remap(clampdown, 0.4, 0.5, 0, 1);
    clampdown = clamp(clampdown, 0, 1);
    clampdown *= 0.9;
    r *= clampdown;
    g *= clampdown;
    b *= clampdown;

    // black out regions
    double r2 = cos(x, (double) t / 10 + 12.345, 4, 0, 1);
    double g2 = cos(y, (double) t / 10 + 24.536, 4, 0, 1);
    double b2 = cos(z, (double) t / 10 + 34.675, 4, 0, 1);
    clampdown = (r2 + g2 + b2) / 2;
    clampdown = remap(clampdown, 0.2, 0.3, 0, 1);
    clampdown = clamp(clampdown, 0, 1);
    r *= clampdown;
    g *= clampdown;
    b *= clampdown;

    // color scheme: fade towards blue-and-orange
    g = g * 0.6 + ((r+b) / 2) * 0.4;

    // fade behind twinkle
    double fade = Math.pow(cos(t - (double) ii / NUM_PIXELS, 0, 7, 0, 1), 20);
    fade = 1 - fade * 0.2;
    r *= fade;
    g *= fade;
    b *= fade;

    // twinkle occasional LEDs
    double twinkle_speed = 0.07;
    double twinkle_density = 0.1;
    double twinkle = (random_values[ii] * 7 + System.currentTimeMillis() * twinkle_speed) % 1;
    twinkle = Math.abs(twinkle * 2 - 1);
    twinkle = remap(twinkle, 0, 1, -1 / twinkle_density, 1.1);
    twinkle = clamp(twinkle, -0.5, 1.1);
    twinkle = Math.pow(twinkle, 5);
    twinkle *= Math.pow(cos(t - (double) ii / NUM_PIXELS, 0, 7, 0, 1), 20);
    twinkle = clamp(twinkle, -0.3, 1);
    r += twinkle;
    g += twinkle;
    b += twinkle;

    return OpcPixel.makePixel((int) (r * 256), (int) (b * 256), (int) (b * 256));
  }

  @Override void calculateNextFrame(int frame) {
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        long timeSinceStart =
          (long) (((startTime - System.currentTimeMillis()) / 60) * 0.6);

        Optional<Integer> pos = getPosition(x, y);
        if (pos.isPresent()) {
          setPixel(x, y, calculatePixel(timeSinceStart, x, y, pos.get()));
        }
      }
    }
  }
}
