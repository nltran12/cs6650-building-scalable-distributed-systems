public class Main {
  private static Integer numThreads = 64; // max 256
  private static Integer numSkiers = 1000; // max 50000
  private static Integer numLifts = 40; // 5-60
  private static Integer numRuns = 10; // max 20
  private static Integer port = 800;

  public static void main(String[] args) {
    Phase1 phase1 = new Phase1(numThreads, numSkiers, 5, "2019", "7", numLifts, numRuns, 1,
        90);
    try {
      phase1.run();
      System.out.println("phase two here");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
