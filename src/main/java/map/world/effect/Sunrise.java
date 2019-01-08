package map.world.effect;

import map.world.view.OpcPixel;
import map.world.view.WorldMapView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

public class Sunrise extends WorldMapEffect {

  private static final int FRAME_DELAY = 40;

  private LocalTime internalTime = LocalTime.of(0, 0, 0);
  private final Duration advanceTime = Duration.ofMinutes(20);

  private final HashMap<Integer, SunTiming> pixelSunTimings = new HashMap<>();

  public Sunrise(WorldMapView view) {
    super(view, FRAME_DELAY, true);

    readPixelSunriseConfig();
  }

  private void readPixelSunriseConfig() {
    // TODO: move to constants
    String configFile =
      "/Users/Matt/Projects/ARM Project/world-map/src/main/java/map/world/effect/sun_data.txt";
    String lineDelimiter = " ";
    String timeDelimiter = ":";
    String line;

    try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
      while ((line = br.readLine()) != null) {
        String[] info = line.split(lineDelimiter);

        int pixel = Integer.parseInt(info[0]);

        // Sunrise
        String[] sunriseDigits = info[1].split(timeDelimiter);
        int riseHour = Integer.parseInt(sunriseDigits[0]);
        int riseMin = Integer.parseInt(sunriseDigits[1]);
        int riseSecond = Integer.parseInt(sunriseDigits[2]);
        LocalTime sunrise = LocalTime.of(riseHour, riseMin, riseSecond);

        // Sunset
        String[] sunsetDigits = info[2].split(timeDelimiter);
        int setHour = Integer.parseInt(sunsetDigits[0]);
        int setMin = Integer.parseInt(sunsetDigits[1]);
        int setSec = Integer.parseInt(sunsetDigits[2]);
        LocalTime sunset = LocalTime.of(setHour, setMin, setSec);

        pixelSunTimings.put(pixel, new SunTiming(sunrise, sunset));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Based on rules in C version - TODO: simplify in aux.
  private int calculateSunlight(int pixel) {
    SunTiming sunTiming = pixelSunTimings.get(pixel);
    LocalTime sunrise = sunTiming.sunrise;
    LocalTime sunset = sunTiming.sunset;

    long daylight = Duration.between(sunset, sunrise).toMinutes() * 60;

    // If sunset/sunrise time is the same, pixel is always off
    if (daylight == 0) {
    //if (sunrise.equals(sunset)) {
      return BLACK_PIXEL;
    }

    // Sunrise time is before sunset
    else if (daylight > 0) {
    //if (sunrise.isBefore(sunset)) {
      long beforeSunrise = Duration.between(sunrise, internalTime).toMinutes() * 60;
      if (beforeSunrise < 0) {
      //if (internalTime.isAfter(sunrise)) {
        long beforeSunset = Duration.between(sunset, internalTime).toMinutes() * 60;
        if (beforeSunset > 0) {
        //if (internalTime.isBefore(sunset)) {
          int col = (int) ((beforeSunset / (daylight / 2.0)) * 255);
          col = (col > 255) ? 255 - col : col;
          return OpcPixel.makeWhite(col);
        }
      }
    }

    // Sunset time is before sunrise
    else {
      long beforeSunset = Duration.between(sunset, internalTime).toMinutes() * 60;
      if (beforeSunset > 0) {
        daylight += (3600 * 24);
        int col = (int) ((beforeSunset / (daylight / 2.0)) * 255);
        col = (col > 255) ? 255 - col : col;
        return OpcPixel.makeWhite(col);

      } else {
        long beforeSunrise = Duration.between(sunrise, internalTime).toMinutes() * 60;
        if (beforeSunrise < 0) {
          // Current is after sunrise
          daylight += (3600 * 24);
          int col = (int) ((-beforeSunrise / (daylight / 2.0)) * 255);
          col = (col > 255) ? 255 - col : col;
          return OpcPixel.makeWhite(col);
        }
      }
    }

    return BLACK_PIXEL;
  }

  private int calculateSunlightAux(int pixel) {
    SunTiming sunTiming = pixelSunTimings.get(pixel);

    // If sunset/sunrise time is the same, pixel is always on
    if (sunTiming.sunrise.equals(sunTiming.sunset)) {
      return WHITE_PIXEL;
    }

    // If pixel has sun up at internal world time, turn on
    if (internalTime.isAfter(sunTiming.sunrise)
        && internalTime.isBefore(sunTiming.sunset)) {
      return WHITE_PIXEL;
    }

    // TODO: smooth edges to sunrise
    return BLACK_PIXEL;
  }

  /* How it works - internal time is mapped to by frame number, pixels are
     turned on if internal time is within the sun up time for that pixel */
  @Override void calculateNextFrame(int frame) {
    // Advance internal world time by a fixed amount each frame to model daytime
    internalTime = internalTime.plus(advanceTime);

    // Update pixel lists based on new internal world time
    for (int pixel = 0; pixel < NUM_PIXELS; pixel++) {
      setPixel(pixel, calculateSunlight(pixel));
    }
  }

  private class SunTiming {
    private final LocalTime sunrise;
    private final LocalTime sunset;

    private SunTiming(LocalTime sunrise, LocalTime sunset) {
      this.sunrise = sunrise;
      this.sunset = sunset;
    }
  }
}
