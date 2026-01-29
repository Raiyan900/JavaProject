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
import javax.servlet.http.HttpSession;

@WebServlet("/myBookings")
public class MyBookingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        //Check login
        if (session == null || session.getAttribute("cust_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String custId = session.getAttribute("cust_id").toString();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // ================= HTML HEADER =================
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>My Bookings</title>");
        out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/myBookings.css'>");

        out.println("</head>");
        out.println("<body>");

        out.println("<div class='box'>");
        out.println("<h2>My Bookings - Customer: " + custId + "</h2>");

        // ================= TABLE HEADER =================
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>Rental ID</th>");
        out.println("<th>Date</th>");
        out.println("<th>Car</th>");
        out.println("<th>Days</th>");
        out.println("<th>Amount</th>");
        out.println("</tr>");

        // ================= DATABASE =================
        try (Connection con = DBUtil.getConnection()) {

            String sql =
                "SELECT r.rental_id, r.rental_date, c.car_name, r.days, r.amount " +
                "FROM rental r " +
                "JOIN car c ON r.car_id = c.car_id " +
                "WHERE r.cust_id = ? " +
                "ORDER BY r.rental_id DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, custId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("rental_id") + "</td>");
                out.println("<td>" + rs.getDate("rental_date") + "</td>");
                out.println("<td>" + rs.getString("car_name") + "</td>");
                out.println("<td>" + rs.getInt("days") + "</td>");
                out.println("<td>" + rs.getInt("amount") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr>");
                out.println("<td colspan='5'>No bookings found</td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr>");
            out.println("<td colspan='5'>Error loading bookings</td>");
            out.println("</tr>");
        }

        // ================= FOOTER =================
        out.println("</table>");
        out.println("<br>");
        out.println("<a href='customerDashboard.html'>← Back to Dashboard</a>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}
