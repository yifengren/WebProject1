package laiproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class SetVisitedRestaurants
 */
@WebServlet("/SetVisitedRestaurants")
public class SetVisitedRestaurants extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetVisitedRestaurants() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private static final DBConnection connection = new DBConnection();
	 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
            if (input.has("user_id") && input.has("visited")) {
                String user_id = (String) input.get("user_id");
                JSONArray visited = (JSONArray) input.get("visited");
                List<String> list = new ArrayList<String>();
                for(int i=0; i<visited.length(); i++){
                    String businessId = visited.getString(i);
                    list.add(businessId);
                }
                connection.SetVisitedRestaurants(user_id, list);;
            }
            response.setContentType("application/json");
            response.addHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = response.getWriter();
            out.flush();
            out.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

}
