package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Message;

@WebServlet(name = "StatisticsServlet")
public class StatisticsServlet extends HttpServlet {

  private Gson gson = new Gson();

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    String[] urlParts = urlPath.split("/");
    if (isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_OK);
      
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    if (urlPath.length == 1) {
      return true;
    }
    return false;
  }
}
