import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class Phase3Thread extends Thread {
  private final String BASE_PATH = "http://54.191.150.225:8080/server_war/";
  //private final String BASE_PATH = "http://localhost:8080/server_war_exploded/";
  private Integer resortID;
  private String seasonID;
  private String dayID;
  private Integer startSkierID;
  private Integer endSkierID;
  private Integer startTime;
  private Integer endTime;
  private Integer liftID;
  private CountDownLatch latch;
  private int numPostRequests;
  private SharedResults sharedResults;
  private CountDownLatch overallLatch;

  /**
   * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
   * #Thread(ThreadGroup, Runnable, String) Thread} {@code (null, null, gname)}, where {@code gname} is
   * a newly generated name. Automatically generated names are of the form {@code "Thread-"+}<i>n</i>,
   * where <i>n</i> is an integer.
   */
  public Phase3Thread(Integer resortID, String seasonID, String dayID,
      Integer startSkierID, Integer endSkierID, Integer startTime, Integer endTime,
      Integer liftID, CountDownLatch latch, int numPostRequests, SharedResults sharedResults,
      CountDownLatch overallLatch) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.startSkierID = startSkierID;
    this.endSkierID = endSkierID;
    this.startTime = startTime;
    this.endTime = endTime;
    this.liftID = liftID;
    this.latch = latch;
    this.numPostRequests = numPostRequests;
    this.sharedResults = sharedResults;
    this.overallLatch = overallLatch;
  }

  /**
   * If this thread was constructed using a separate
   * <code>Runnable</code> run object, then that
   * <code>Runnable</code> object's <code>run</code> method is called;
   * otherwise, this method does nothing and returns.
   * <p>
   * Subclasses of <code>Thread</code> should override this method.
   *
   * @see #start()
   * @see #stop()
   * @see #Thread(ThreadGroup, Runnable, String)
   */
  @Override
  public void run() {
    Timestamp startTime;
    Timestamp endTime;
    int responseCode;
    int successfulPosts = 0;
    int failedPosts = 0;
    List<String> fileData = new ArrayList<>();
    SkiersApi apiInstance = new SkiersApi();
    ApiClient client = apiInstance.getApiClient();
    client.setBasePath(BASE_PATH);
    for (int i = 0; i < this.numPostRequests; i++) {
      LiftRide liftRide = new LiftRide();
      liftRide.time(ThreadLocalRandom.current().nextInt(this.endTime - this.startTime) + this.startTime);
      liftRide.liftID(ThreadLocalRandom.current().nextInt(this.liftID) + 1);
      Integer skierID = ThreadLocalRandom.current().nextInt(this.endSkierID - this.startSkierID) + this.startSkierID;
      startTime = new Timestamp(System.currentTimeMillis());
      try {
        apiInstance.writeNewLiftRide(liftRide, this.resortID, this.seasonID, this.dayID, skierID);
        endTime = new Timestamp(System.currentTimeMillis());
        successfulPosts++;
        responseCode = 200;
      } catch (ApiException e) {
        System.err.println("Exception when calling SkierApi#writeNewLiftRide");
        endTime = new Timestamp(System.currentTimeMillis());
        responseCode = e.getCode();
        failedPosts++;
        e.printStackTrace();
      }
      long latency = endTime.getTime() - startTime.getTime();
      String fileLine = startTime.toString() + ",POST," + latency + "," + responseCode + "\n";
      fileData.add(fileLine);

      startTime = new Timestamp(System.currentTimeMillis());
      try {
        apiInstance.getSkierDayVertical(this.resortID, this.seasonID, this.dayID, skierID);
        endTime = new Timestamp(System.currentTimeMillis());
        successfulPosts++;
        responseCode = 200;
      } catch (ApiException e) {
        System.err.println("Exception when calling SkierApi#getSkierDayVertical");
        endTime = new Timestamp(System.currentTimeMillis());
        responseCode = e.getCode();
        failedPosts++;
        e.printStackTrace();
      }
      latency = endTime.getTime() - startTime.getTime();
      fileLine = startTime.toString() + ",GET," + latency + "," + responseCode + "\n";
      fileData.add(fileLine);
    }
    this.sharedResults.incrementSuccessfulPost(successfulPosts);
    this.sharedResults.incrementFailedPost(failedPosts);
    this.sharedResults.addNewResults(fileData);

    try {
      this.latch.countDown();
      this.overallLatch.countDown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
