package lincks.maximilian.task2;

import lincks.maximilian.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Task2 {
  public static void run() {
    CarWash carWash = new CarWash();
    ArrayList<Thread> queue = new ArrayList<>();
    Counter counter = new Counter();

    // hour 1
    // each 5 min: 1-3 Cars, clean % 2
    System.out.println("Run hour 1");
    runHour(counter, 3, 2, queue, carWash);

    // hour 2
    // each 5 min: 3 -5 Cars, clean % 3
    System.out.println("Run hour 2");
    runHour(counter, 5, 3, queue, carWash);

    // hour 3
    // each 5 min: 1 - 2 Cars clean % 1
    System.out.println("Run hour 3");
    runHour(counter, 2, 1, queue, carWash);

    // hour 4
    // each 5 min: 1 - 2 Cars clean % 1
    System.out.println("Run hour 4");
    runHour(counter, 2, 1, queue, carWash);

    // wait for all Threads/Cars to finish
    queue.forEach(
        t -> {
          try {
            t.join();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /**
   * @param counter used to generate Car Ids
   * @param max the maximum amount of cars in a 5min slot
   * @param clean every clean-th car needs interior cleaning
   * @param queue queue to await all threads
   * @param carWash used to work on the cars
   */
  private static void runHour(
      Counter counter, int max, int clean, List<Thread> queue, CarWash carWash) {
    IntStream.range(0, 12)
        .sequential()
        .mapToObj(
            slot ->
                IntStream.range(1, 1 + RandomUtil.fromInterval(1, max)).map(nr -> counter.getId()))
        .map(
            batch ->
                batch
                    .parallel()
                    .mapToObj(
                        id ->
                            new Thread(
                                () -> {
                                  Car car = new Car(id);
                                  try {
                                    carWash.useCleaningStreet(car);
                                    if (id % clean == 0) {
                                      carWash.useInteriorCleaning(car);
                                    }
                                  } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                  }
                                })))
        .forEach(
            batch -> {
              System.out.println("Start slot");
              batch
                  .parallel()
                  .forEach(
                      t -> {
                        t.start();
                        queue.add(t);
                      });
              try {
                // Wait 5 min for next
                Thread.sleep(5 * 1000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });
  }

  public static class Counter {
    private int count;

    public synchronized int getId() {
      return ++count;
    }
  }
}
