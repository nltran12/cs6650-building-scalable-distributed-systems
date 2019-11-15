package servlet;

import com.google.gson.Gson;
import dal.CacheSource;
import io.lettuce.core.RedisFuture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EndpointStats;
import model.Stats;

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {

  private Gson gson = new Gson();

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try {
      response.getWriter().write(gson.toJson(getStats()));
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (Exception ex) {
      ex.printStackTrace();
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private EndpointStats getStats() throws ExecutionException, InterruptedException {
    RedisFuture<String> getLatency = CacheSource.getConnection().get("GET_SKIER_LATENCY");
    RedisFuture<String> getCount = CacheSource.getConnection().get("GET_SKIER_COUNT");
    RedisFuture<String> getMax = CacheSource.getConnection().get("GET_SKIER_MAX");
    RedisFuture<String> postLatency = CacheSource.getConnection().get("POST_SKIER_LATENCY");
    RedisFuture<String> postCount = CacheSource.getConnection().get("POST_SKIER_COUNT");
    RedisFuture<String> postMax = CacheSource.getConnection().get("POST_SKIER_MAX");

    List<Stats> stats = new ArrayList<>();
    String count = postCount.get();
    if (count != null) {
      stats.add(new Stats("/skier", "GET",
          Integer.parseInt(getLatency.get()) / Integer.parseInt(getCount.get()),
          Integer.parseInt(getMax.get())));
      stats.add(new Stats("/skier", "POST",
          Integer.parseInt(postLatency.get()) / Integer.parseInt(count),
          Integer.parseInt(postMax.get())));
    }
    return new EndpointStats(stats);
  }
}
