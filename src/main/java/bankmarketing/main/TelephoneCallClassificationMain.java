package bankmarketing.main;

import bankmarketing.domain.TelephoneCall;
import bankmarketing.util.TrainAndTestSetUtil;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

public class TelephoneCallClassificationMain {

  public static final int NUM_CATEGORIES = 2;
  public static final int NUM_FEATURES = 11;

  public static void main(String[] args) throws Exception {
    List<TelephoneCall> calls = getTelephoneCalls();

    double trainPercentage = 0.10;
    TrainAndTestSetUtil split = new TrainAndTestSetUtil(calls.size(), trainPercentage);
    List<Integer> test = split.getTestSet();

    int[] correct = new int[test.size() + 1];
    for (int run = 0; run < 20; run++) {
      OnlineLogisticRegression lr = new OnlineLogisticRegression(NUM_CATEGORIES, NUM_FEATURES, new L1());
      for (int pass = 0; pass < 15; pass++) {
        List<Integer> train = split.shuffledTrainingSet();
        for (int k : train) {
          TelephoneCall observation = calls.get(k);
          observation.vectorize();
          lr.train(observation.getTarget(), observation.asVector());
        }
      }

      int x = 0;
      int[] count = new int[2];
      for (Integer k : test) {
        TelephoneCall testCall = calls.get(k);
        try {
          double r = lr.classifyScalar(testCall.asVector());
          int result;
          if (r <= 0.5) {
            result = 0;
          } else {
            result = 1;
          }
          count[result]++;
          x += result == testCall.getTarget() ? 1 : 0;
        } catch (NullPointerException e) {
          System.out.println(k);
        }
      }
      System.out.println("Run " + run + " = " + ((double) x / (double) test.size()) * 100 + "% accuracy");
      correct[x]++;
    }
  }

  private static List<TelephoneCall> getTelephoneCalls() throws Exception {
    FlatFileItemReader<TelephoneCall> reader = createCSVReader();
    ExecutionContext context = new ExecutionContext();
    Dictionary targetDictionary = new Dictionary();
    List<TelephoneCall> calls = new ArrayList<>();
    TelephoneCall call;
    reader.open(context);
    while ((call = reader.read()) != null) {
      call.setDictionary(targetDictionary);
      call.vectorize();
      calls.add(call);
    }
    reader.close();
    return calls;
  }

  private static FlatFileItemReader<TelephoneCall> createCSVReader() {
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(";");
    tokenizer.setNames(new String[]{"age","job","marital","education","default","balance","housing","loan","contact","day","month","duration","campaign","pdays","previous","poutcome","y"});

    DefaultLineMapper<TelephoneCall> lineMapper = new DefaultLineMapper<>();
    lineMapper.setFieldSetMapper(new TelephoneCallFieldSetMapper());
    lineMapper.setLineTokenizer(tokenizer);

    FlatFileItemReader<TelephoneCall> reader = new FlatFileItemReader<>();
    reader.setLinesToSkip(1);
    reader.setLineMapper(lineMapper);
    reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy("\""));
    reader.setResource(new ClassPathResource("bank-full.csv"));
    return reader;
  }

  private static class TelephoneCallFieldSetMapper implements FieldSetMapper<TelephoneCall> {

    @Override
    public TelephoneCall mapFieldSet(FieldSet fieldSet) throws BindException {
      TelephoneCall call = new TelephoneCall();
      call.setAge(fieldSet.readInt("age"));
      call.setJob(fieldSet.readString("job"));
      call.setMarital(fieldSet.readString("marital"));
      call.setEducation(fieldSet.readString("education"));
      call.setDefault(fieldSet.readString("default"));
      call.setBalance(fieldSet.readInt("balance"));
      call.setHousing(fieldSet.readString("housing"));
      call.setLoan(fieldSet.readString("loan"));
      call.setContact(fieldSet.readString("contact"));
      call.setDay(fieldSet.readInt("day"));
      call.setMonth(fieldSet.readString("month"));
      call.setDuration(fieldSet.readInt("duration"));
      call.setCampaign(fieldSet.readInt("campaign"));
      call.setPdays(fieldSet.readInt("pdays"));
      call.setPrevious(fieldSet.readInt("previous"));
      call.setPoutcome(fieldSet.readString("poutcome"));
      call.setY(fieldSet.readString("y"));

      return call;
    }
  }
}
