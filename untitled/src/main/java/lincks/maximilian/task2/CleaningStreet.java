package lincks.maximilian.task2;

import lincks.maximilian.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;

import static lincks.maximilian.App.SPEED;

@RequiredArgsConstructor
public class CleaningStreet {
  private final int id;
  @Getter private boolean isFree = true;

  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  private void use(Car car) throws InterruptedException {
    try (ResourceLock ignored = new ResourceLock(lock)) {
      isFree = false;
      long duration = RandomUtil.fromInterval(5, 12);
      System.out.printf(
              "Car %s is using CleaningStreet %s for %s minutes%n", car.getId(), id, duration);
      Thread.sleep(duration * SPEED);
      isFree = true;
      condition.signal();
    }
  }

  public void tryUse(Car car) throws InterruptedException {
    try (ResourceLock ignored = new ResourceLock(lock)) {
      Stream.generate(this::isFree)
              .map(free -> {
                if(!free){
                  try {
                    condition.await();
                    return this.isFree();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                  }
                }
                return true;
              })
              .filter(t -> t).findAny();

//      while (!isFree) {
//
//      }
      use(car);
      System.out.printf("Car %s is leaving CleaningStreet %s%n", car.getId(), id);

    }
  }
}
