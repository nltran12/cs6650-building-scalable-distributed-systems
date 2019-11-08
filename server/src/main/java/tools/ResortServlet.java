package tools;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Message;
import model.ResortInfo;
import model.ResortSeasons;
import model.Resorts;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {

  private Gson gson = new Gson();
  private static final String SEASONS_PARAMETER = "seasons";

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
      return;
    }

    String[] urlParts = urlPath.split("/");
    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_OK);
      ResortInfo resortInfo = new ResortInfo("string", 0);
      response.getWriter().write(gson.toJson(new Resorts(resortInfo)));
      return;
    }

    String[] urlParts = urlPath.split("/");
    if (!isUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      String[] resortSeasons = new String[]{"string"};
      response.getWriter().write(gson.toJson(new ResortSeasons(resortSeasons)));
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    if (urlPath.length == 1) {
      return true;
    }
    if (urlPath.length == 3) {
      try {
        Integer.parseInt(urlPath[1]);
        return urlPath[2].equals(SEASONS_PARAMETER);
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}
