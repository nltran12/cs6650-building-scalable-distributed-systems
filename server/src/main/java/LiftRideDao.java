import java.sql.*;

public class LiftRideDao {
  private static Connection conn;
  private static PreparedStatement preparedStatement;

  public LiftRideDao() {
    try {
      conn = DBCPDataSource.getConnection();
      preparedStatement = null;
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void createLiftRide(LiftRide newLiftRide) {
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId) " +
        "VALUES (?,?,?,?,?,?)";
    try {
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setInt(3, newLiftRide.getSeasonId());
      preparedStatement.setInt(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftId());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}