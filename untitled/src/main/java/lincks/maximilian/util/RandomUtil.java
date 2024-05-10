package lincks.maximilian.util;

import java.util.Random;

public class RandomUtil {
  private static final Random random = new Random();

  /**
   * Random number between upper and lower
   *
   * @param lower inclusive
   * @param upper inclusive
   * @return random number
   */
  public static int fromInterval(int lower, int upper) {
    return random.nextInt(1 + upper - lower) + lower;
  }
}
