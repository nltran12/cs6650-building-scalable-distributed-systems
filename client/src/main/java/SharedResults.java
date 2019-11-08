import java.util.ArrayList;
import java.util.List;

public class SharedResults {
  private int successfulPosts;
  private int failedPosts;
  private List<String> fileLines;

  public SharedResults() {
    this.successfulPosts = 0;
    this.failedPosts = 0;
    this.fileLines = new ArrayList<>();
  }

  public synchronized void incrementSuccessfulPost(int increment) {
    this.successfulPosts += increment;
  }

  public synchronized void incrementFailedPost(int increment) {
    this.failedPosts -= increment;
  }

  public synchronized void addNewResults(List<String> newResults) {
    this.fileLines.addAll(newResults);
  }

  public int getSuccessfulPosts() {
    return this.successfulPosts;
  }

  public int getFailedPosts() {
    return this.failedPosts;
  }

  public List<String> getFileLines() { return this.fileLines; }
}
