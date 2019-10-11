import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

public class Main {
  private static Integer numThreads = 64; // max 256
  private static Integer numSkiers = 20000; // max 50000
  private static Integer numLifts = 20; // 5-60
  private static Integer numRuns = 20; // max 20
  private static Integer port = 800;
  private static final double PHASE1_POST_MULTIPLIER = 0.1;
  private static final double PHASE2_POST_MULTIPLIER = 0.8;
  private static final String SEASON_ID = "2019";
  private static final String DAY_ID = "7";

  public static void main(String[] args) {
    SharedResults results = new SharedResults();
    // Calculations for threads and requests
    int numReducedThreads = (int) Math.round(numThreads / 4.0);
    int numPhase1Requests =
        (int) Math.round((numRuns * PHASE1_POST_MULTIPLIER) * (numSkiers / (numReducedThreads + 0.0)));
    int numPhase2Requests = (int) Math.round((numRuns * PHASE2_POST_MULTIPLIER) * (numSkiers / (numThreads + 0.0)));
    int numPhase3Requests = (int) Math.round(numRuns * 0.1);
    int totalThreads = numReducedThreads * 2 + numThreads;
    CountDownLatch overallLatch = new CountDownLatch(totalThreads);

    // Create each phase.
    Phase phase1 = new Phase(numReducedThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 1,
        90, numPhase1Requests, ((int)Math.ceil(numReducedThreads / 10.0)), results, overallLatch);
    Phase phase2 = new Phase(numThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 91, 360,
        numPhase2Requests, ((int)Math.ceil(numThreads / 10.0)), results, overallLatch);
    Phase phase3 = new Phase(numReducedThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 361,
        420, numPhase3Requests, numReducedThreads, results, overallLatch);

    try {
      long wallTime = runPhases(phase1, phase2, phase3, overallLatch);
      System.out.println("Number of successful posts: " + results.getSuccessfulPosts());
      System.out.println("Number of failed posts: " + results.getFailedPosts());
      System.out.println("Wall time: " + wallTime + "ms");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // Given each phase and the count down latch, runs each phase awaiting for all the threads to
  // finish. Returns the wall time of the run.
  private static long runPhases(Phase phase1, Phase phase2, Phase phase3,
      CountDownLatch overallLatch) throws InterruptedException {
    Timestamp startTime = new Timestamp(System.currentTimeMillis());
    phase1.run();
    phase2.run();
    phase3.run();
    overallLatch.await();
    Timestamp endTime = new Timestamp(System.currentTimeMillis());
    return endTime.getTime() - startTime.getTime();
  }
}
