package model;

public class SkierVertical {
  private String seasonID;
  private int vertical;

  public SkierVertical(String seasonID, int vertical) {
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
