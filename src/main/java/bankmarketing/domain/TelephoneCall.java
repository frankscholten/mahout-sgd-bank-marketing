package bankmarketing.domain;

import com.google.common.collect.Maps;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import java.util.Iterator;
import java.util.Map;

public class TelephoneCall {
  public static final int FEATURES = 100;
  private static final ConstantValueEncoder interceptEncoder = new ConstantValueEncoder("intercept");
  private static final FeatureVectorEncoder featureEncoder = new StaticWordValueEncoder("feature");

  private RandomAccessSparseVector vector;

  private Map<String, String> fields = Maps.newLinkedHashMap();

  public TelephoneCall(Iterable<String> fieldNames, Iterable<String> values) {
    vector = new RandomAccessSparseVector(FEATURES);
    Iterator<String> value = values.iterator();
    interceptEncoder.addToVector("1", vector);
    for (String name : fieldNames) {
      String fieldValue = value.next();
      fields.put(name, fieldValue);

      switch (name) {
        case "age":
          double v = Double.parseDouble(fieldValue);
          featureEncoder.addToVector(name, Math.log(v), vector);
          break;

        case "balance":
          v = Double.parseDouble(fieldValue);
          if (v < -2000) {
            v = -2000;
          }
          featureEncoder.addToVector(name, Math.log(v + 2001) - 8, vector);
          break;

        case "duration":
          v = Double.parseDouble(fieldValue);
          featureEncoder.addToVector(name, Math.log(v + 1) - 5, vector);
          break;

        case "pdays":
          v = Double.parseDouble(fieldValue);
          featureEncoder.addToVector(name, Math.log(v + 2), vector);
          break;

        case "job":
        case "marital":
        case "education":
        case "default":
        case "housing":
        case "loan":
        case "contact":
        case "campaign":
        case "previous":
        case "poutcome":
          featureEncoder.addToVector(name + ":" + fieldValue, 1, vector);
          break;

        case "day":
        case "month":
        case "y":
          // ignore these for vectorizing
          break;

        default:
          throw new IllegalArgumentException(String.format("Bad field name: %s", name));
      }
    }
  }

  public Vector asVector() {
    return vector;
  }

  public int getTarget() {
    return fields.get("y").equals("no") ? 0 : 1;
  }
}
