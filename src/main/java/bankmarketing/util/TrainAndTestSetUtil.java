package bankmarketing.util;

import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.common.RandomWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility for creating training and test set splits.
 */
public class TrainAndTestSetUtil {

  private final List<Integer> trainingSet;
  private final List<Integer> testSet;
  private final RandomWrapper random;

  public TrainAndTestSetUtil(int size, double trainPercentage) {
    List<Integer> order = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      order.add(i);
    }

    this.random = RandomUtils.getRandom();
    Collections.shuffle(order, random);

    int trainingSetSize = (int) (order.size() * trainPercentage);

    this.trainingSet = order.subList(0, trainingSetSize);
    this.testSet = order.subList(trainingSetSize, size);
  }

  public List<Integer> shuffledTrainingSet() {
    Collections.shuffle(trainingSet, random);
    return trainingSet;
  }

  public List<Integer> getTestSet() {
    return testSet;
  }
}
