package laiproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;

/**
 * Servlet implementation class GetRestaurantsNearby
 */
@WebServlet("/GetRestaurantsNearby")
public class GetRestaurantsNearby extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public GetRestaurantsNearby() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // what if I change it to txt
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        String username = "";
        PrintWriter out = response.getWriter();
        if (request.getParameter("username") != null) {
            username = request.getParameter("username");
            out.print("Hello" + username);
        }
        out.flush();
        out.close();

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    private static final DBConnection connection = new DBConnection();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            reader.close();
        } catch (Exception e) { /* report an error */
        }

        try {
            JSONObject input = new JSONObject(jb.toString());
            JSONArray array = null;
            if (input.has("lat") && input.has("lon")) {
                double lat = (Double) input.get("lat");
                double lon = (Double) input.get("lon");
                array = connection.GetRestaurantsNearLoation(lat, lon);
            }
            response.setContentType("application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = response.getWriter();
            out.print(array);
            out.flush();
            out.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
