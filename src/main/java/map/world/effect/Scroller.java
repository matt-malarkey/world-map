package map.world.effect;

import map.world.view.WorldMapView;

public class Scroller extends WorldMapEffect {

  private static final int FRAME_DELAY = 50;

  // Create a buffer pixel grid to contain data about to be displayed
  private final int BUFF_WIDTH = 100;
  private int[][] bufferedGrid = new int[MAP_HEIGHT][BUFF_WIDTH];
  
  private int startPosition;

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
        bufferedGrid[x][y] = {255, 0, c}; // TODO: figure out colours properly
      }
    }

    // Set background buffer
    for (int x = 0; x < 60; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        bufferedGrid[x][y] = {255, 0, 255}; // TODO: figure out colours properly
      }
    }

    // COMPLETE hack but I am way too tired
    for (int x = 85; x < 100; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        bufferedGrid[x][y] = {255, 0, 255}; // TODO: figure out colours properly
      }
    }

    // Set opc_pixel_t grid to all purple
    for (int x = 0; x < MAP_WIDTH; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        setPixel(getPosition(x, y), {255, 0, 255}); // TODO: figure out colours properly
      }
    }
  }

  // TODO: read from config, put in abstract class.
  private int getPosition(int x, int y) {
    return 0;
  }

  @Override void calculateNextFrame(int frame) {
    // Update the pixel list displayed in the view from the fixed buffer grid
    for (int x = startPosition; x < MAP_WIDTH + startPosition; x++) {
      for (int y = 0; y < MAP_HEIGHT; y++) {
        int effectiveX = x % BUFF_WIDTH;
        int pos = getPosition(effectiveX, y);
        if (pos >= 0) {
          setPixel(pos, bufferedGrid[effectiveX][y]);
        }
      }
    }
    startPosition++;
  }
}
