package Servlet;

import db.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/myBookings")
public class CustomerBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Customer check
        if (session == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>My Bookings</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>Car</th><th>Start Date</th><th>End Date</th><th>Price</th><th>Status</th></tr>");

        try {
            Connection con = DBUtil.getConnection();

            String sql =
                "SELECT c.car_name, b.start_date, b.end_date, b.total_price, b.status " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.id " +
                "WHERE b.user_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("car_name") + "</td>");
                out.println("<td>" + rs.getDate("start_date") + "</td>");
                out.println("<td>" + rs.getDate("end_date") + "</td>");
                out.println("<td>" + rs.getInt("total_price") + "</td>");
                out.println("<td>" + rs.getString("status") + "</td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("<br><a href='customerDashboard.html'>Back</a>");
    }
}
