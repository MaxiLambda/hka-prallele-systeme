package lincks.maximilian.task1;

import lincks.maximilian.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lincks.maximilian.App.SPEED;

@Getter
@RequiredArgsConstructor
public class InteriorCleaning {
  private final int id;
  @Getter private boolean isFree = true;

  private synchronized void use(Car car) throws InterruptedException {
    isFree = false;
    long duration = RandomUtil.fromInterval(1, 3) * 5L;
    System.out.printf(
        "Car %s is using InteriorCleaning %s for %s minutes%n", car.getId(), id, duration);
    Thread.sleep(duration * SPEED);
    isFree = true;
    notify();
  }

  public synchronized void tryUse(Car car) throws InterruptedException {
    while (!isFree) {
      try {
        wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    use(car);
    System.out.printf("Car %s is leaving InteriorCleaning %s%n", car.getId(), id);
  }
}
