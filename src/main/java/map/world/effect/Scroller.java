package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

public class Scroller extends WorldMapEffect {

  private static final int FRAME_DELAY = 50;

  // Create a buffer pixel grid to contain data about to be displayed
  private final int BUFF_WIDTH = 100;
  private int startPosition = 0;
  private int[][] bufferedGrid = new int[BUFF_WIDTH][MAP_HEIGHT];

  public Scroller(WorldMapView view) {
    super(view, FRAME_DELAY);

    // Initialise the scroller grid
    initBufferedGrid();
  }

  private void initBufferedGrid() {
    // Set a shaded buffer
    for (int x = 60; x < BUFF_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        int c = (x * (255 / (BUFF_WIDTH - 60)));
        bufferedGrid[x][y] = OpcPixel.makePixel(255, 0, c);
      }
    }

    // Set background buffer
    for (int x = 0; x < 60; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        bufferedGrid[x][y] = OpcPixel.makePixel(255, 0, 255);
      }
    }

    // COMPLETE hack but I am way too tired
    for (int x = 85; x < 100; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        bufferedGrid[x][y] = OpcPixel.makePixel(255, 0, 255);
      }
    }

    // Set opc_pixel_t grid to all purple
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        setPixel(x, y, OpcPixel.makePixel(255, 0, 255));
      }
    }
  }

  @Override void calculateNextFrame(int frame) {
    // Update the pixel list displayed in the view from the fixed buffer grid
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        int effectiveX = (x + startPosition) % BUFF_WIDTH;
        setPixel(x, y, bufferedGrid[effectiveX][y]);
      }
    }

    // Make sure position never overflows by resetting when it reaches width
    startPosition++;
    if (startPosition >= BUFF_WIDTH) {
      startPosition = 0;
    }
  }
}
