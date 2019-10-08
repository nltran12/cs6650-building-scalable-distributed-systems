import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class Main {
  private static Integer numThreads = 8; // max 256
  private static Integer numSkiers = 20; // max 50000
  private static Integer numLifts = 20; // 5-60
  private static Integer numRuns = 20; // max 20
  private static Integer port = 800;
  private static final double PHASE1_POST_MULTIPLIER = 0.1;
  private static final double PHASE2_POST_MULTIPLIER = 0.8;

  public static void main(String[] args) {
    PostResults results = new PostResults();
    int numReducedThreads = (int) Math.round(numThreads / 4.0);
    int numPhase1Requests =
        (int) Math.round((numRuns * PHASE1_POST_MULTIPLIER) * (numSkiers / (numReducedThreads + 0.0)));
    int numPhase2Requests = (int) Math.round((numRuns * PHASE2_POST_MULTIPLIER) * (numSkiers / (numThreads + 0.0)));
    int numPhase3Requests = (int) Math.round(numRuns * 0.1);
    int totalThreads = numReducedThreads * 2 + numThreads;
    CountDownLatch overallLatch = new CountDownLatch(totalThreads);
    Phase phase1 = new Phase(numReducedThreads, numSkiers, 5, "2019", "7", numLifts, 1,
        90, numPhase1Requests, ((int)Math.ceil(numReducedThreads / 10.0)), results, overallLatch);
    Phase phase2 = new Phase(numThreads, numSkiers, 5, "2019", "7", numLifts, 91, 360,
        numPhase2Requests, ((int)Math.ceil(numThreads / 10.0)), results, overallLatch);
    Phase phase3 = new Phase(numReducedThreads, numSkiers, 5, "2019", "7", numLifts, 361,
        420, numPhase3Requests, numReducedThreads, results, overallLatch);
    try {
      Timestamp startTime = new Timestamp(System.currentTimeMillis());
      phase1.run();
      phase2.run();
      phase3.run();
      overallLatch.await();
      Timestamp endTime = new Timestamp(System.currentTimeMillis());
      System.out.println("Number of successful posts: " + results.getSuccessfulPosts());
      System.out.println("Number of failed posts: " + results.getFailedPosts());
      System.out.println("Wall time: " + (endTime.getTime() - startTime.getTime()) + "ms");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
