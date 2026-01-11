package Servlet;

import db.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addCar")
public class AddCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carName = request.getParameter("car_name");
        String brand = request.getParameter("brand");
        int price = Integer.parseInt(request.getParameter("price_per_day"));

        try {
            Connection con = DBUtil.getConnection();
            String sql = "INSERT INTO cars (car_name, brand, price_per_day, available) VALUES (?, ?, ?, true)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, carName);
            ps.setString(2, brand);
            ps.setInt(3, price);

            ps.executeUpdate();
            response.sendRedirect("adminDashboard.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
