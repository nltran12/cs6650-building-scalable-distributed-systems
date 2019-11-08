public class CommandLineParser {
  private final int COMMAND_LINE_LENGTH = 5;
  private final int MAX_THREADS = 256;
  private final int MAX_SKIERS = 50000;
  private final int MIN_LIFTS = 5;
  private final int MAX_LIFTS = 60;
  private final int MAX_RUNS = 20;
  private Integer numThreads; // max 256
  private Integer numSkiers; // max 50000
  private Integer numLifts; // 5-60
  private Integer numRuns; // max 20
  private Integer port;

  public CommandLineParser() {
    this.numThreads = 0;
    this.numSkiers = 0;
    this.numLifts = 0;
    this.numRuns = 0;
    this.port = 800;
  }

  public boolean parseCommandLine(String[] args) {
    if (args.length != COMMAND_LINE_LENGTH) {
      System.out.println("Not enough arguments. Command line should be in the following order:"
          + " <filename> <num threads> <num skiers> <num lifts> <num runs> <port number>.");
      return false;
    }
    try {
      this.numThreads = Integer.parseInt(args[0]);
      if (this.numThreads > MAX_THREADS) {
        System.out.println("Threads num too high");
        return false;
      }
      this.numSkiers = Integer.parseInt(args[1]);
      if (this.numSkiers > MAX_SKIERS) {
        System.out.println("Skiers num too high");
        return false;
      }
      this.numLifts = Integer.parseInt(args[2]);
      if (this.numLifts < MIN_LIFTS || this.numLifts > MAX_LIFTS) {
        System.out.println("Lifts num too high or low");
        return false;
      }
      this.numRuns = Integer.parseInt(args[3]);
      if (this.numRuns > MAX_RUNS) {
        System.out.println("Runs num too high");
        return false;
      }
      this.port = Integer.parseInt(args[4]);
      return true;
    } catch (Exception ex) {
      System.out.println("Please enter numbers for threads, skiers, lifts, run, and port number."
          + "Command line should be in the following order: <filename> <num threads> <num skiers> "
          + "<num lifts> <num runs> <port number>.");
      return false;
    }
  }

  public Integer getNumThreads() {
    return numThreads;
  }

  public Integer getNumSkiers() {
    return numSkiers;
  }

  public Integer getNumLifts() {
    return numLifts;
  }

  public Integer getNumRuns() {
    return numRuns;
  }

  public Integer getPort() {
    return port;
  }
}
