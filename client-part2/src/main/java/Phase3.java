import java.util.concurrent.CountDownLatch;

public class Phase3 {
  private int numThreads;
  private int numSkiers;
  private Integer resortID;
  private String seasonID;
  private String dayID;
  private Integer numLifts;
  private int startTime;
  private int endTime;
  private int numPostRequests;
  private int numWaitThreads;
  private SharedResults results;
  private CountDownLatch overallLatch;

  public Phase3(int numThreads, int numSkiers, Integer resortID, String seasonID,
      String dayID, Integer numLifts, int startTime, int endTime, int numPostRequests,
      int numWaitThreads, SharedResults results, CountDownLatch overallLatch) {
    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.numLifts = numLifts;
    this.startTime = startTime;
    this.endTime = endTime;
    this.numPostRequests = numPostRequests;
    this.numWaitThreads = numWaitThreads;
    this.results = results;
    this.overallLatch = overallLatch;
  }

  public void run() throws InterruptedException {
    for (int i = 0; i < this.numThreads; i++) {
      int startSkiers = 1 + (i * (this.numSkiers / this.numThreads));
      int endSkiers = (i + 1) * (this.numSkiers / this.numThreads);
      Thread thread = new Phase3Thread(this.resortID, this.seasonID, this.dayID, startSkiers,
          endSkiers, this.startTime, this.endTime, numLifts, numPostRequests, this.results,
          this.overallLatch);
      thread.start();
    }
  }
}
