package lincks.maximilian.task2;

import lincks.maximilian.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lincks.maximilian.App.SPEED;

@Getter
@RequiredArgsConstructor
public class InteriorCleaning {
  private final int id;
  @Getter private boolean isFree = true;

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  private  void use(Car car) throws InterruptedException {
    try (ResourceLock ignored = new ResourceLock(lock)) {
      isFree = false;
      long duration = RandomUtil.fromInterval(1, 3) * 5L;
      System.out.printf(
              "Car %s is using InteriorCleaning %s for %s minutes%n", car.getId(), id, duration);
      Thread.sleep(duration * SPEED);
      isFree = true;
      condition.signal();
    }
  }

  public  void tryUse(Car car) throws InterruptedException {
    try (ResourceLock ignored = new ResourceLock(lock)) {
      while (!isFree) {
        try {
          condition.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      use(car);
      System.out.printf("Car %s is leaving InteriorCleaning %s%n", car.getId(), id);
    }
  }
}
