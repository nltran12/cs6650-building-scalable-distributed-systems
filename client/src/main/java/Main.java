import java.util.concurrent.Executor;

public class Main {
  private static Integer numThreads = 64; // max 256
  private static Integer numSkiers = 1000; // max 50000
  private static Integer numLifts = 40; // 5-60
  private static Integer numRuns = 10; // max 20
  private static Integer port = 800;

  public static void main(String[] args) {
    int numReducedThreads = (int) Math.round(numThreads / 4.0);
    int numPhase1Requests =
        (int) Math.round((numRuns * 0.1) * (numSkiers / (numReducedThreads + 0.0)));
    int numPhase2Requests = (int) Math.round((numRuns * 0.8) * (numSkiers / (numThreads + 0.0)));
    int numPhase3Requests = (int) Math.round(numRuns * 0.1);
    Phase phase1 = new Phase(numReducedThreads, numSkiers, 5, "2019", "7", numLifts, 1,
        90, numPhase1Requests, ((int)Math.ceil(numReducedThreads / 10.0)));
    Phase phase2 = new Phase(numThreads, numSkiers, 5, "2019", "7", numLifts, 91, 360,
        numPhase2Requests, ((int)Math.ceil(numThreads / 10.0)));
    Phase phase3 = new Phase(numReducedThreads, numSkiers, 5, "2019", "7", numLifts, 361,
        420, numPhase3Requests, numReducedThreads);
    try {
      phase1.run();
      phase2.run();
      phase3.run();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
