package bankmarketing.main;

import bankmarketing.domain.TelephoneCall;
import bankmarketing.domain.TelephoneCallParser;
import com.google.common.collect.Lists;
import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;

import java.util.Collections;
import java.util.List;

public class TelephoneCallClassificationMain {

  public static final int NUM_CATEGORIES = 2;

  public static void main(String[] args) throws Exception {
    List<TelephoneCall> calls = Lists.newArrayList(new TelephoneCallParser("bank-full.csv"));

    double heldOutPercentage = 0.10;


    for (int run = 0; run < 20; run++) {
      Collections.shuffle(calls);
      int cutoff = (int) (heldOutPercentage * calls.size());
      List<TelephoneCall> test = calls.subList(0, cutoff);
      List<TelephoneCall> train = calls.subList(cutoff, calls.size());

      OnlineLogisticRegression lr = new OnlineLogisticRegression(NUM_CATEGORIES, TelephoneCall.FEATURES, new L1())
        .learningRate(1)
        .alpha(1)
        .lambda(0.000001)
        .stepOffset(10000)
        .decayExponent(0.2);
      for (int pass = 0; pass < 20; pass++) {
        for (TelephoneCall observation : train) {
          lr.train(observation.getTarget(), observation.asVector());
        }
        if (pass % 5 == 0) {
          Auc eval = new Auc(0.5);
          for (TelephoneCall testCall : test) {
            eval.add(testCall.getTarget(), lr.classifyScalar(testCall.asVector()));
          }
          System.out.printf("%d, %.4f, %.4f\n", pass, lr.currentLearningRate(), eval.auc());
        }
      }

    }
  }
}
