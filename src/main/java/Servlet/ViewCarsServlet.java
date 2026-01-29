package Servlet;

import db.DBUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewCars")
public class ViewCarsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Available Cars</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 80%; margin:auto; }");
        out.println("th, td { border: 1px solid black; padding: 8px; text-align: center; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("form { margin:0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2 style='text-align:center;'>Available Cars</h2>");

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Car Name</th>");
        out.println("<th>Brand</th>");
        out.println("<th>Rate / Day</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        try (Connection con = DBUtil.getConnection()) {

            String sql = "SELECT car_id, car_name, brand, rate FROM Car WHERE available = TRUE";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int carId = rs.getInt("car_id");
                String carName = rs.getString("car_name");
                String brand = rs.getString("brand");
                int rate = rs.getInt("rate");

                out.println("<tr>");

                out.println("<td>" + carName + "</td>");
                out.println("<td>" + brand + "</td>");
                out.println("<td>" + rate + "</td>");

                out.println("<td>");
                out.println("<form action='rentPage' method='get'>");

                out.println("<input type='hidden' name='carId' value='" + carId + "'>");
                out.println("<input type='hidden' name='carName' value='" + carName + "'>");
                out.println("<input type='hidden' name='brand' value='" + brand + "'>");
                out.println("<input type='hidden' name='rate' value='" + rate + "'>");

                out.println("<input type='submit' value='Rent'>");

                out.println("</form>");
                out.println("</td>");

                out.println("</tr>");
            }

        } catch (Exception e) {
            out.println("<tr><td colspan='4'>ERROR: " + e.getMessage() + "</td></tr>");
            e.printStackTrace();
        }

        out.println("</table>");

        out.println("<br><div style='text-align:center;'>");
        out.println("<a href='customerDashboard.html'>Back</a>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}
