package Servlet;

import db.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DBUtil.getConnection();

            //fetch id + role
            String sql = "SELECT id, role FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                //Store in session
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("role", role);

                //Role-based redirect
                if ("admin".equals(role)) {
                    response.sendRedirect("adminDashboard.html");
                } else if ("customer".equals(role)) {
                    response.sendRedirect("customerDashboard.html");
                } else {
                    response.sendRedirect("login.html");
                }

            } else {
                //Invalid login
                response.sendRedirect("login.html");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
