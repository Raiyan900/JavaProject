package Servlet;

import db.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/addMaintenance")
public class AddMaintenanceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        int carId = Integer.parseInt(request.getParameter("car_id"));
        String description = request.getParameter("description");
        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        try {
            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO car_maintenance (car_id, description, start_date, end_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setString(2, description);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE cars SET available = false WHERE id = ?");
            ps2.setInt(1, carId);
            ps2.executeUpdate();

            response.sendRedirect("adminDashboard.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
