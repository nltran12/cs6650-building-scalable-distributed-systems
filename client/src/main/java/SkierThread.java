import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class SkierThread extends Thread {
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

  /**
   * Allocates a new {@code Thread} object. This constructor has the same effect as {@linkplain
   * #Thread(ThreadGroup, Runnable, String) Thread} {@code (null, null, gname)}, where {@code gname} is
   * a newly generated name. Automatically generated names are of the form {@code "Thread-"+}<i>n</i>,
   * where <i>n</i> is an integer.
   */
  public SkierThread(Integer resortID, String seasonID, String dayID,
      Integer startSkierID, Integer endSkierID, Integer startTime, Integer endTime,
      Integer liftID, CountDownLatch latch, int numPostRequests) {
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
    String basePath = "http://localhost:8080/server_war_exploded/";
    SkiersApi apiInstance = new SkiersApi();
    ApiClient client = apiInstance.getApiClient();
    client.setBasePath(basePath);
    for (int i = 0; i < this.numPostRequests; i++) {
      LiftRide liftRide = new LiftRide();
      liftRide.time(ThreadLocalRandom.current().nextInt(this.endTime - this.startTime) + this.startTime);
      liftRide.liftID(ThreadLocalRandom.current().nextInt(this.liftID) + 1);
      Integer skierID = ThreadLocalRandom.current().nextInt(this.endSkierID - this.startSkierID) + this.startSkierID;
      try {
        apiInstance.writeNewLiftRide(liftRide, this.resortID, this.seasonID, this.dayID, skierID);
      } catch (ApiException e) {
        System.err.println("Exception when calling SkierApi#writeNewLiftRide");
        e.printStackTrace();
      }
    }
    try {
      latch.countDown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
