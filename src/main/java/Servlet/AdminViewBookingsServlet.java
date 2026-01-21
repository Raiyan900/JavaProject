package Servlet;

import db.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/adminBookings")
public class AdminViewBookingsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h2>All Bookings (Admin)</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>User</th><th>Car</th><th>Start</th><th>End</th><th>Price</th><th>Status</th></tr>");

        try {
            Connection con = DBUtil.getConnection();

            String sql =
                "SELECT u.username, c.car_name, b.start_date, b.end_date, b.total_price, b.status " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN cars c ON b.car_id = c.id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("username") + "</td>");
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
        out.println("<br><a href='adminDashboard.html'>Back</a>");
    }
}
