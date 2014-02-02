package bankmarketing.domain;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.ContinuousValueEncoder;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class TelephoneCall {

  private RandomAccessSparseVector vector;

  public Dictionary dictionary;

  private String y;
  private int age;
  private String job;
  private String marital;
  private String education;
  private String aDefault;
  private int balance;
  private String contact;
  private String loan;
  private int day;
  private String month;
  private int duration;
  private int campaign;
  private int pdays;
  private int previous;
  private String poutcome;
  private String housing;

  public void vectorize() {
    vector = new RandomAccessSparseVector(11);

    ConstantValueEncoder interceptEncoder = new ConstantValueEncoder("intercept");
    interceptEncoder.addToVector("1", vector);

    StaticWordValueEncoder jobEncoder = new StaticWordValueEncoder("job");
    jobEncoder.addToVector(job, vector);

    ContinuousValueEncoder ageEncoder = new ContinuousValueEncoder("age");
    ageEncoder.addToVector(String.valueOf(age), vector);

    StaticWordValueEncoder maritalEncoder = new StaticWordValueEncoder("marital");
    maritalEncoder.addToVector(marital, vector);

    StaticWordValueEncoder educationEncoder = new StaticWordValueEncoder("education");
    educationEncoder.addToVector(education, vector);

    StaticWordValueEncoder defaultEncoder = new StaticWordValueEncoder("aDefault");
    defaultEncoder.addToVector(aDefault, vector);

    ContinuousValueEncoder campaignEncoder = new ContinuousValueEncoder("campaign");
    campaignEncoder.addToVector(String.valueOf(campaign), vector);

    ContinuousValueEncoder pDaysEncoder = new ContinuousValueEncoder("pDays");
    pDaysEncoder.addToVector(String.valueOf(pdays), vector);

    ContinuousValueEncoder previousEncoder = new ContinuousValueEncoder("previous");
    previousEncoder.addToVector(String.valueOf(previous), vector);

    StaticWordValueEncoder pOutcomeEncoder = new StaticWordValueEncoder("poutcome");
    pOutcomeEncoder.addToVector(poutcome, vector);

    StaticWordValueEncoder housingEncoder = new StaticWordValueEncoder("housing");
    housingEncoder.addToVector(housing, vector);
  }

  public Vector asVector() {
    if (vector == null) {
      vectorize();
    }
    return vector;
  }

  public int getTarget() {
    return dictionary.intern(y);
  }

  //======================================= Getters & Setters ==========================================================


  public void setAge(int age) {
    this.age = age;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public void setMarital(String marital) {
    this.marital = marital;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public void setDefault(String aDefault) {
    this.aDefault = aDefault;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public int getBalance() {
    return balance;
  }

  public void setHousing(String housing) {
    this.housing = housing;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getContact() {
    return contact;
  }

  public void setLoan(String loan) {
    this.loan = loan;
  }

  public String getLoan() {
    return loan;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getDay() {
    return day;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getMonth() {
    return month;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getDuration() {
    return duration;
  }

  public void setCampaign(int campaign) {
    this.campaign = campaign;
  }

  public void setPdays(int pdays) {
    this.pdays = pdays;
  }

  public void setPrevious(int previous) {
    this.previous = previous;
  }

  public void setPoutcome(String poutcome) {
    this.poutcome = poutcome;
  }

  public void setY(String y) {
    this.y = y;
  }

  public void setDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
  }
}
