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

        out.println("<html><body>");
        out.println("<h2>Available Cars</h2>");

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM cars WHERE available=true"
            );
            ResultSet rs = ps.executeQuery();

            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Brand</th><th>Price</th><th>Action</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("car_name") + "</td>");
                out.println("<td>" + rs.getString("brand") + "</td>");
                out.println("<td>" + rs.getInt("price_per_day") + "</td>");
                out.println("<td>");
                out.println("<form action='rentCar' method='post'>");
                out.println("<input type='hidden' name='car_id' value='" + rs.getInt("id") + "'>");
                out.println("<button type='submit'>Rent</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</body></html>");
    }
}
