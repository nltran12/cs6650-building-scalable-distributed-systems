package servlet;

import com.google.gson.Gson;
import dao.LiftRideDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.LiftRide;
import model.Message;
import model.PostBody;
import model.SkierResorts;
import model.SkierVertical;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  private Gson gson  = new Gson();
  private static final int DAY_ID_MIN = 1;
  private static final int DAY_ID_MAX = 366;
  private static final String SEASONS_PARAMETER = "seasons";
  private static final String DAYS_PARAMETER = "days";
  private static final String SKIERS_PARAMETER = "skiers";
  private static final String VERTICAL_PARAMETER = "vertical";
  private static final String RESORTS_PARAMETER = "resorts";

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
    if (!isUrlValid(urlParts, request)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      Message message = new Message("string");
      response.getWriter().write(gson.toJson(message));
    } else {
        PostBody postBody = gson.fromJson(request.getReader(), PostBody.class);
        LiftRideDao liftRideDao = new LiftRideDao();
        liftRideDao.createLiftRide(new LiftRide(Integer.parseInt(urlParts[7]),
            Integer.parseInt(urlParts[1]), urlParts[3], urlParts[5],
            postBody.getTime(), postBody.getLiftID()));
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

      // Process url params
      if (urlParts[2].equals("vertical")) {
        SkierVertical skierVertical = new SkierVertical(urlParts[1], 10);
        res.getWriter().write(gson.toJson(new SkierResorts(skierVertical)));
      } else {
        LiftRideDao liftRideDao = new LiftRideDao();
        res.getWriter().write(String.valueOf(liftRideDao.getDayVertical(Integer.parseInt(urlParts[1]), urlParts[3],
            urlParts[5], Integer.parseInt(urlParts[7]))));
      }
      res.setStatus(HttpServletResponse.SC_OK);
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
            && Integer.valueOf(urlPath[5]) >= DAY_ID_MIN
            && Integer.valueOf(urlPath[5]) <= DAY_ID_MAX
            && urlPath[2].equals(SEASONS_PARAMETER)
            && urlPath[4].equals(DAYS_PARAMETER)
            && urlPath[6].equals(SKIERS_PARAMETER));
      } catch (Exception e) {
        return false;
      }
    } else if (urlPath.length == 3) {
      try {
        Integer.parseInt(urlPath[1]);
        return (urlPath[2].equals(VERTICAL_PARAMETER) && req.getParameter(RESORTS_PARAMETER) != null);
      } catch (Exception e) {
        return false;
      }
    }
    return false;
  }
}
