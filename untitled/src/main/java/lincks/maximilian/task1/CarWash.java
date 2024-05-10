package lincks.maximilian.task1;

import java.util.List;
import lincks.maximilian.util.RandomUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CarWash {

  private final List<CleaningStreet> cleaningStreets =
      List.of(new CleaningStreet(1), new CleaningStreet(2), new CleaningStreet(3));
  private final List<InteriorCleaning> interiorCleanings =
      List.of(new InteriorCleaning(1), new InteriorCleaning(2));

  public void useCleaningStreet(Car car) throws InterruptedException {
    System.out.printf("Car %s wants to use cleaning street%n", car.getId());
    // check if any is free
    cleaningStreets.stream()
        .filter(CleaningStreet::isFree)
        .findAny()
        // if none is free, wait for random one
        .orElse(cleaningStreets.get(RandomUtil.fromInterval(0, cleaningStreets.size() - 1)))
        .tryUse(car);
  }

  public void useInteriorCleaning(Car car) throws InterruptedException {
    System.out.printf("Car %s wants to use interior cleaning%n", car.getId());
    // check if any is free
    interiorCleanings.stream()
        .filter(InteriorCleaning::isFree)
        .findAny()
        // if none is free, wait for random one
        .orElse(interiorCleanings.get(RandomUtil.fromInterval(0, interiorCleanings.size() - 1)))
        .tryUse(car);
  }
}
