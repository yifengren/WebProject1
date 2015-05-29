package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBImport {
    public static void main(String[] args) {
        try {
            // Step 1: Initialize the driver and connect to a SQL server
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection conn = null;
            String line = null;

            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?" + "user=root&password=root");
            } catch (SQLException e) {
                System.out.println("SQLException " + e.getMessage());
                System.out.println("SQLState " + e.getSQLState());
                System.out.println("VendorError " + e.getErrorCode());
            }
            if (conn == null) {
                return;
            }

            // Step 2: drop tables in order of user_visit_history, users,
            // restaurants
            Statement stmt = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS USER_VISIT_HISTORY";
            stmt.executeUpdate(sql);
            sql = "DROP TABLE IF EXISTS USERS";
            stmt.executeUpdate(sql);
            sql = "DROP TABLE IF EXISTS RESTAURANTS";
            stmt.executeUpdate(sql);

            // Step 3: create tables in order of users, restaurants,
            // user_visit_history
            sql = "CREATE TABLE USERS " + "(user_id VARCHAR(255) NOT NULL, "
                    + " first_name VARCHAR(255), last_name VARCHAR(255), " + " PRIMARY KEY ( user_id ))";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE RESTAURANTS " + "(business_id VARCHAR(255) NOT NULL, " + " name VARCHAR(255), "
                    + "categories VARCHAR(255), " + "city VARCHAR(255), " + "state VARCHAR(255), " + "stars FLOAT,"
                    + "full_address VARCHAR(255), " + "latitude FLOAT, " + " longitude FLOAT, "
                    + " PRIMARY KEY ( business_id ))";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE USER_VISIT_HISTORY (visit_history_id bigint(20) unsigned NOT NULL AUTO_INCREMENT, "
                    + "user_id VARCHAR(255) NOT NULL , business_id VARCHAR(255) NOT NULL, last_visited_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY (visit_history_id), FOREIGN KEY (business_id) REFERENCES RESTAURANTS(business_id),FOREIGN KEY (user_id) REFERENCES users(user_id))";
            stmt.executeUpdate(sql);
            // replace with your real physical path .
            BufferedReader reader = new BufferedReader(new FileReader(
                    "/Users/yifengr/Downloads/yelp_dataset-master/business_small.json"));
            // For windows, it is like
            // "C:\\your_user_name\\yelp_dataset-master\\business_small.json";
            while ((line = reader.readLine()) != null) {
                JSONObject restaurant = new JSONObject(line);
                String business_id = restaurant.getString("business_id");
                String name = parseString(restaurant.getString("name"));
                String categories = parseString(jsonArrayToString(restaurant.getJSONArray("categories")));
                String city = parseString(restaurant.getString("city"));
                String state = restaurant.getString("state");
                String full_address = parseString(restaurant.getString("full_address"));
                double stars = restaurant.getDouble("stars");
                double latitude = restaurant.getDouble("latitude");
                double longitude = restaurant.getDouble("longitude");
                sql = "INSERT INTO RESTAURANTS " + "VALUES ('" + business_id + "', \"" + name + "\", \"" + categories
                        + "\", '" + city + "', '" + state + "', " + stars + ", \"" + full_address + "\", " + latitude
                        + "," + longitude + ")";
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }
            
            sql="INSERT INTO USERS VALUES(\"1111\", \"John\", \"Smith\")";
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done Importing");
    }

    /**
     * Replace \ by \", and / by or.
     */
    public static String parseString(String str) {
        return str.replace("\"", "\\\"").replace("/", " or ");
    }

    /**
     * Convert a JSONArray to string with ,
     */
    public static String jsonArrayToString(JSONArray array) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < array.length(); i++) {
                String obj = (String) array.get(i);
                sb.append(obj);
                if (i != array.length() - 1) {
                    sb.append(",");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Convert a String to JSONArray
     */
    public static JSONArray stringToJSONArray(String str) {
        try {
            return new JSONArray("[" + str + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
