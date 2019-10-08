public class PostResults {
  private int successfulPosts;
  private int failedPosts;

  public PostResults() {
    this.successfulPosts = 0;
    this.failedPosts = 0;
  }

  public synchronized void increment_successful_post() {
    this.successfulPosts++;
  }

  public synchronized void increment_failed_post() {
    this.failedPosts++;
  }

  public int getSuccessfulPosts() {
    return successfulPosts;
  }

  public int getFailedPosts() {
    return failedPosts;
  }
}
