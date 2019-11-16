package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import model.LiftRide;


public class LiftRideDao {

  public void createLiftRide(LiftRide newLiftRide, HttpServletRequest req) {
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, "
        + "time, liftId, vertical) VALUES (?,?,?,?,?,?,?)";

    DataSource pool = (DataSource) req.getServletContext().getAttribute("my-pool");

    try (Connection conn = pool.getConnection();
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

  public int getDayVertical(int resortId, String seasonId, String dayId,
      int skierId, HttpServletRequest req) {
    int totalVertical = 0;
    String selectCreditCard = "SELECT vertical FROM LiftRides WHERE skierId=? AND resortId=? AND"
        + " seasonId=? AND dayId=?;";
    DataSource pool = (DataSource) req.getServletContext().getAttribute("my-pool");
    try (Connection conn = pool.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(selectCreditCard)) {
      preparedStatement.setInt(1, skierId);
      preparedStatement.setInt(2, resortId);
      preparedStatement.setString(3, seasonId);
      preparedStatement.setString(4, dayId);
      try (ResultSet result = preparedStatement.executeQuery()) {
        while (result.next()) {
          totalVertical += result.getInt("vertical");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return totalVertical;
  }
}