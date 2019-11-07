public class LiftRide {
  private int skierId;
  private int resortId;
  private String seasonId;
  private String dayId;
  private int time;
  private int liftId;

  public LiftRide(int skierId, int resortId, String seasonId, String dayId, int time, int liftId) {
    this.skierId = skierId;
    this.resortId = resortId;
    this.seasonId = seasonId;
    this.dayId = dayId;
    this.time = time;
    this.liftId = liftId;
  }

  public int getSkierId() {
    return skierId;
  }

  public int getResortId() {
    return resortId;
  }

  public String getSeasonId() {
    return seasonId;
  }

  public String getDayId() {
    return dayId;
  }

  public int getTime() {
    return time;
  }

  public int getLiftId() {
    return liftId;
  }
}
