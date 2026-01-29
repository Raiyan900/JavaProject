package Servlet;

import db.DBUtil;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        HttpSession session = request.getSession();

        try (Connection con = DBUtil.getConnection()) {

            //Check Employee/Admin
            String sqlEmp = "SELECT emp_id FROM employee WHERE username=? AND password=?";
            PreparedStatement psEmp = con.prepareStatement(sqlEmp);
            psEmp.setString(1, username);
            psEmp.setString(2, password);
            ResultSet rsEmp = psEmp.executeQuery();

            if (rsEmp.next()) {
                session.setAttribute("role", "employee");
                session.setAttribute("emp_id", rsEmp.getString("emp_id"));
                response.sendRedirect("adminDashboard.html");
                return;
            }

            //Check Customer
            String sqlCust = "SELECT cust_id FROM customer WHERE username=? AND password=?";
            PreparedStatement psCust = con.prepareStatement(sqlCust);
            psCust.setString(1, username);
            psCust.setString(2, password);
            ResultSet rsCust = psCust.executeQuery();

            if (rsCust.next()) {
                session.setAttribute("role", "customer");
                session.setAttribute("cust_id", rsCust.getString("cust_id"));
                response.sendRedirect("customerDashboard.html");
                return;
            }

            //Login failed
            response.sendRedirect("loginFailed.html");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("loginFailed.html");
        }
    }
}
