public class LiftRide {
  private int skierId;
  private int resortId;
  private int seasonId;
  private int dayId;
  private int time;
  private int liftId;

  public LiftRide(int skierId, int resortId, int seasonId, int dayId, int time, int liftId) {
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

  public int getSeasonId() {
    return seasonId;
  }

  public int getDayId() {
    return dayId;
  }

  public int getTime() {
    return time;
  }

  public int getLiftId() {
    return liftId;
  }
}
