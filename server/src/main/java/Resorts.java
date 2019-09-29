public class Resorts {
  private String seasonID;
  private int vertical;

  public Resorts(String seasonID, int vertical) {
    this.seasonID = seasonID;
    this.vertical = vertical;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public int getVertical() {
    return vertical;
  }
}
