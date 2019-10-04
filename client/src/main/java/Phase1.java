import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;

public class Phase1 {
  private int numThreads;
  private int numSkiers;
  private Integer resortID;
  private String seasonID;
  private String dayID;
  private Integer numLifts;
  private int numRuns;
  private int startTime;
  private int endTime;

  public Phase1(int numThreads, int numSkiers, Integer resortID, String seasonID,
      String dayID, Integer numLifts, int numRuns, int startTime, int endTime) {
    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.numLifts = numLifts;
    this.numRuns = numRuns;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public void run() throws InterruptedException {
    int numPhase1Threads = (int) Math.round(numThreads / 4.0);
    int numPostRequests =
        (int) Math.round((this.numRuns * 0.1) * (this.numSkiers / numPhase1Threads));
    CountDownLatch latch = new CountDownLatch(((int)Math.ceil(numPhase1Threads / 10.0)));
    for (int i = 0; i < numPhase1Threads; i++) {
      int startSkiers = 1 + (i * (numSkiers / numPhase1Threads));
      int endSkiers = (i + 1) * (numSkiers / numPhase1Threads);
      System.out.println("start skiers: " + startSkiers + " end skiers: " + endSkiers);
      Thread thread = new SkierThread(this.resortID, this.seasonID, this.dayID, startSkiers,
          endSkiers, this.startTime, this.endTime, numLifts, latch, numPostRequests);
      thread.start();
    }
    latch.await();
    System.out.println("done!");
  }
}
