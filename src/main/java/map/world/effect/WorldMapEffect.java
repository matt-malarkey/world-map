package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class WorldMapEffect extends Thread {

  // Dimension constants
  static final int NUM_PIXELS = 471;
  static final int MAP_WIDTH = 53;
  static final int MAP_HEIGHT = 24;

  static final int BLACK_PIXEL = OpcPixel.makePixel(0, 0, 0);
  static final int WHITE_PIXEL = OpcPixel.makePixel(255, 255, 255);

  // To read in configuration files to
  private int[][] pixelPositionToNumber = new int[MAP_WIDTH][MAP_HEIGHT];

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

    // Set up pixel position to int config array
    readPixelPositionConfig();
  }

  private void readPixelPositionConfig() {
    // Initialise the mapping to -1 for all x, y
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        pixelPositionToNumber[x][y] = -1;
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

  private int getPosition(int x, int y) {
    return pixelPositionToNumber[x][y];
  }

  abstract void calculateNextFrame(int frame);

  private void advanceFrame() {
    calculateNextFrame(currentFrame);
    currentFrame++;
  }

  public final void setPixel(int pixelNum, int colour) {
    pixels[pixelNum] = colour;
  }

  public final void setPixel(int x, int y, int colour) {
    assert (x < MAP_WIDTH) : "x was greater than the width of the map";
    assert (y < MAP_HEIGHT) : "y was greater than the height of the map";

    int position = getPosition(x, y);
    if (position >= 0 && position < NUM_PIXELS) {
      setPixel(position, colour);
    }
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
