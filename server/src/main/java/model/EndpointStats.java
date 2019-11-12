package model;

import java.util.List;

public class EndpointStats {
  private List<Stats> endpointStats;


  public EndpointStats(List<Stats> endpointStats) {
    this.endpointStats = endpointStats;
  }

  public List<Stats> getEndpointStats() {
    return endpointStats;
  }
}
