package dao;

import java.sql.*;
import model.LiftRide;

public class LiftRideDao {

  public void createLiftRide(LiftRide newLiftRide) {
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, "
        + "time, liftId, vertical) VALUES (?,?,?,?,?,?,?)";

    try (Connection conn = DBCPDataSource.getConnection();
    PreparedStatement preparedStatement = conn.prepareStatement(insertQueryStatement)) {
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setString(3, newLiftRide.getSeasonId());
      preparedStatement.setString(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftId());
      preparedStatement.setInt(7, newLiftRide.getLiftId() * 10);

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}