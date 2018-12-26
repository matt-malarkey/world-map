package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static map.world.view.OpcWorldMapView.pixelsPerStrip;

public abstract class WorldMapEffect extends Thread {

  // Dimension constants
  static final int NUM_PIXELS = 471;
  static final int NUM_STRIPS = 8;
  static final int MAP_WIDTH = 53;
  static final int MAP_HEIGHT = 24;

  static final int BLACK_PIXEL = OpcPixel.makePixel(0, 0, 0);
  static final int WHITE_PIXEL = OpcPixel.makePixel(255, 255, 255);

  // To read in configuration files to, addressed by [x][y] from top left
  private final int[][] pixelPositionToNumber = new int[MAP_WIDTH][MAP_HEIGHT];

  // TODO: comment
  private final int[] pixelNumToOpcNum = new int[NUM_PIXELS];

  private final WorldMapView view;
  private final int frameDelay;

  // Pixel list
  private final Integer[] pixels = new Integer[NUM_PIXELS];

  // To enable playing/pausing of effect
  private final Object playBlocker = new Object();
  private boolean shouldPlay = true;

  private volatile boolean isDead = false;
  private int currentFrame = 0;

  public WorldMapEffect(WorldMapView view, int frameDelay) {
    this.view = view;
    this.frameDelay = frameDelay;

    // Set up pixel list
    IntStream.range(0, NUM_PIXELS).forEach(i -> pixels[i] = BLACK_PIXEL);

    // Set up configuration maps for internal and opc pixel num mappings
    readConfigFiles();
  }

  // Set up pixel position to int and pixel num to opc pixel config arrays
  private void readConfigFiles() {
    readPixelPositionConfig();
    readPixelStripConfig();
  }

  private void readPixelPositionConfig() {
    // Initialise the mapping to -1 for all x, y
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        pixelPositionToNumber[x][y] = -2;
      }
    }

    // TODO: move to constants
    String configFile = "/Users/Matt/Projects/ARM Project/world-map/src/main/java/map/world/view/coordinates.txt";
    String delimiter = " ";
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
      while ((line = br.readLine()) != null) {
        String[] info = line.split(delimiter);

        int x = Integer.parseInt(info[0]);
        int y = Integer.parseInt(info[1]);
        int pos = Integer.parseInt(info[2]);
        pixelPositionToNumber[x][y] = pos;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Store a list mapping internal pixel_number (from config file) to physical
  // pixel number, calculated from channel and position_in_channel
  private void readPixelStripConfig() {
    //pixelsPerStrip[i] gives number of pixels in strip i
    // Build up a cumulative list of how many pixels to add for to index a certain strip
    int[] numPixelsBeforeStrip = new int[NUM_STRIPS];
    numPixelsBeforeStrip[0] = 0;
    for (int i = 0; i < NUM_STRIPS - 1; i++) {
      numPixelsBeforeStrip[i + 1] = numPixelsBeforeStrip[i] + pixelsPerStrip[i];
    }

    // TODO: move to constants
    String configFile = "/Users/Matt/Projects/ARM Project/world-map/src/main/java/map/world/view/strip_config.txt";
    String delimiter = " ";
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
      while ((line = br.readLine()) != null) {
        String[] info = line.split(delimiter);

        int strip = Integer.parseInt(info[0]);
        int posInStrip = Integer.parseInt(info[1]);
        int pixelNum = Integer.parseInt(info[2]);

        pixelNumToOpcNum[pixelNum] = numPixelsBeforeStrip[strip] + posInStrip;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Optional<Integer> getPosition(int x, int y) {
    int internalPixelNum = pixelPositionToNumber[x][y];
    if (internalPixelNum < 0 || internalPixelNum >= NUM_PIXELS) {
      return Optional.empty();
    }
    return Optional.of(pixelNumToOpcNum[internalPixelNum]);
  }

  abstract void calculateNextFrame(int frame);

  private void advanceFrame() {
    calculateNextFrame(currentFrame);
    currentFrame++;
  }

  public final void setPixel(int pixelNum, int colour) {
    pixels[pixelNum] = colour;
  }

  public final boolean setPixel(int x, int y, int colour) {
    assert (x < MAP_WIDTH) : "x was greater than the width of the map";
    assert (y < MAP_HEIGHT) : "y was greater than the height of the map";

    Optional<Integer> pixel = getPosition(x, y);
    pixel.ifPresent(position -> setPixel(position, colour));
    return pixel.isPresent();
  }

  public final List<Integer> getPixelList() {
    return Arrays.asList(pixels);
  }

  public final void kill() {
    isDead = true;
  }

  private void frameSleepBetween() {
    try {
      Thread.sleep(frameDelay);
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void blockIfPaused() {
    synchronized (playBlocker) {
      if (!shouldPlay) {
        try {
          // Block until another thread calls notify on playBlocker
          playBlocker.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override public final void run() {
    view.clear();

    // Run this effect until interrupted by controller
    while (!isDead) {
      // Make sure playing of effect is enabled
      blockIfPaused();

      // Update pixels for next frame then update display
      advanceFrame();
      view.update(this);

      // Sleep between frames
      frameSleepBetween();
    }

    view.clear();
  }

  public final void play() {
    if (!shouldPlay) {
      shouldPlay = true;
      synchronized (playBlocker) {
        playBlocker.notify();
      }
    }
  }

  public final void pause() {
    shouldPlay = false;
  }
}
