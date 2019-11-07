import java.sql.*;

public class LiftRideDao {

  public void createLiftRide(LiftRide newLiftRide) {
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId) " +
        "VALUES (?,?,?,?,?,?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = DBCPDataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setString(3, newLiftRide.getSeasonId());
      preparedStatement.setString(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftId());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}