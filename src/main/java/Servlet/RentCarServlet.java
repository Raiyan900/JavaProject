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

@WebServlet("/rentCar")
public class RentCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int carId = Integer.parseInt(request.getParameter("car_id"));

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE cars SET available=false WHERE id=?"
            );
            ps.setInt(1, carId);
            ps.executeUpdate();

            response.sendRedirect("customerDashboard.html?msg=success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
