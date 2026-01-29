package Servlet;

import db.DBUtil;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/addCar")
public class AddCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        //Only employee can add car
        if (session == null || !"employee".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        String carName = request.getParameter("car_name");
        String brand = request.getParameter("brand");
        String rateStr = request.getParameter("rate");

        try {
            int rate = Integer.parseInt(rateStr);

            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO Car (car_name, brand, rate, available) VALUES (?, ?, ?, TRUE)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, carName);
            ps.setString(2, brand);
            ps.setInt(3, rate);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("addCarSuccess.html");
            } else {
                response.sendRedirect("addCarFailed.html");
            }

        } catch (Exception e) {
            e.printStackTrace();   // 🔥 CHECK THIS IN CONSOLE
            response.sendRedirect("addCarFailed.html");
        }
    }
}
