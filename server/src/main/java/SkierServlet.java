import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  private Gson gson  = new Gson();
  private static final int dayIdMin = 1;
  private static final int dayIdMax = 366;

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    BufferedReader buffIn = null;
    try {
      buffIn = request.getReader();
      StringBuilder reqBody = new StringBuilder();
      String line;
      while ((line = buffIn.readLine()) != null) {
        reqBody.append(line);
      }
      response.getWriter().write(reqBody.toString());
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
      return;
    } finally {
      if (buffIn != null) {
        try {
          buffIn.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }

    if (!isUrlValid(urlParts, request)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("application/json");
    res.setCharacterEncoding("UTF-8");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts, req)) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      res.getWriter().write(gson.toJson(message));
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // Process url params
      if (urlParts[2].equals("vertical")) {
        Resorts resorts = new Resorts(urlParts[1], 10);
        res.getWriter().write(gson.toJson(resorts));
      } else {
        res.getWriter().write("34507");
      }
    }
  }

  private boolean isUrlValid(String[] urlPath, HttpServletRequest req) {
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    if (urlPath.length == 8) {
      try {
        for (int i = 1; i < urlPath.length; i += 2) {
          Integer.parseInt(urlPath[i]);
        }
        return (urlPath[3].length() == 4
            && Integer.valueOf(urlPath[5]) >= dayIdMin
            && Integer.valueOf(urlPath[5]) <= dayIdMax
            && urlPath[2].equals("seasons")
            && urlPath[4].equals("days")
            && urlPath[6].equals("skiers"));
      } catch (Exception e) {
        return false;
      }
    } else if (urlPath.length == 3) {
      try {
        Integer.parseInt(urlPath[1]);
        return (urlPath[2].equals("vertical") && req.getParameter("resort") != null);
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}
