import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {
  private static final double PHASE1_POST_MULTIPLIER = 0.1;
  private static final double PHASE2_POST_MULTIPLIER = 0.8;
  private static final String SEASON_ID = "2019";
  private static final String DAY_ID = "7";

  public static void main(String[] args) {
    // Parse command line for input info.
    CommandLineParser parser = new CommandLineParser();
    if (!parser.parseCommandLine(args)){
      return;
    }
    Integer numThreads = parser.getNumThreads();
    Integer numSkiers = parser.getNumSkiers();
    Integer numLifts = parser.getNumLifts();
    Integer numRuns = parser.getNumRuns();
    Integer port = parser.getPort();

    // Print out input info
    System.out.println("Number of threads: " + numThreads);
    System.out.println("Number of skiers: " + numSkiers);
    System.out.println("Number of lifts: " + numLifts);
    System.out.println("Number of runs: " + numRuns);

    // Calculations for threads and requests
    int numReducedThreads = (int) Math.round(numThreads / 4.0);
    int numPhase1Requests =
        (int) Math.round((numRuns * PHASE1_POST_MULTIPLIER) * (numSkiers / (numReducedThreads + 0.0)));
    int numPhase2Requests = (int) Math.round((numRuns * PHASE2_POST_MULTIPLIER) * (numSkiers / (numThreads + 0.0)));
    int numPhase3Requests = (int) Math.round(numRuns * 0.1);
    int totalThreads = numReducedThreads * 2 + numThreads;

    // Overall latch used to make sure all threads finish. Shared variable contains results.
    CountDownLatch overallLatch = new CountDownLatch(totalThreads);
    SharedResults results = new SharedResults();

    // Create each phase.
    Phase phase1 = new Phase(numReducedThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 1,
        90, numPhase1Requests, ((int)Math.ceil(numReducedThreads / 10.0)), results, overallLatch);
    Phase phase2 = new Phase(numThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 91, 360,
        numPhase2Requests, ((int)Math.ceil(numThreads / 10.0)), results, overallLatch);
    Phase phase3 = new Phase(numReducedThreads, numSkiers, 5, SEASON_ID, DAY_ID, numLifts, 361,
        420, numPhase3Requests, numReducedThreads, results, overallLatch);

    try {
      long wallTime = runPhases(phase1, phase2, phase3, overallLatch);
      printStatistics(results, wallTime);
      // Write results to file
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results.csv"));
      for (String result : results.getFileLines()) {
        bufferedWriter.write(result);
      }
      bufferedWriter.close();

    } catch (Exception e) {
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

  private static List<Integer> getResponseTimes(List<String> results) {
    List<Integer> responseTimes = new ArrayList<>();
    for (String result : results) {
      String[] parsedResults = result.split(",");
      Integer responseTime = Integer.parseInt(parsedResults[2]);
      responseTimes.add(responseTime);
    }
    return responseTimes;
  }

  private static void printStatistics(SharedResults results, long wallTime) {
    DataProcessor dataProcessor = new DataProcessor(getResponseTimes(results.getFileLines()), wallTime);
    System.out.println("Number of successful posts: " + results.getSuccessfulPosts());
    System.out.println("Number of failed posts: " + results.getFailedPosts());
    System.out.println("Wall time: " + (wallTime / 1000) + "secs");
    System.out.println("Average response time: " + dataProcessor.getMean() + " ms");
    System.out.println("Median response time: " + dataProcessor.getMedian() + " ms");
    System.out.println("Throughput: " + dataProcessor.getThroughput() + " requests/wall time");
    System.out.println("99th percentile: " + dataProcessor.getNintyninthPercentile() + " ms");
    System.out.println("Max response time: " + dataProcessor.getMaxResponse() + " ms");
  }
}
