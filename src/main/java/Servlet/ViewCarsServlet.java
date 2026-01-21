package Servlet;

import db.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/viewCars")
public class ViewCarsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>Available Cars</h2>");

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM cars WHERE available = true");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int carId = rs.getInt("id");
                String carName = rs.getString("car_name");
                int price = rs.getInt("price_per_day");

                out.println("<form action='bookCar' method='post'>");

                out.println("Car: " + carName + " | Price per day: " + price + " ");

                out.println("<input type='hidden' name='car_id' value='" + carId + "'>");
                out.println("<input type='hidden' name='price_per_day' value='" + price + "'>");

                out.println("Start Date: <input type='date' name='start_date' required>");
                out.println("End Date: <input type='date' name='end_date' required>");

                out.println("<button type='submit'>Rent</button>");
                out.println("</form><hr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
