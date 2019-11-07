public class PostBody {
  private int time;
  private int liftID;

  public PostBody(int time, int liftID) {
    this.time = time;
    this.liftID = liftID;
  }

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }
}
