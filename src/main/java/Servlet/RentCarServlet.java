package Servlet;

import db.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/rentCar")
public class RentCarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("cust_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String custId = session.getAttribute("cust_id").toString();
        int carId = Integer.parseInt(request.getParameter("car_id"));
        int rate = Integer.parseInt(request.getParameter("rate"));
        int days = Integer.parseInt(request.getParameter("days"));
        int amount = rate * days;

        try (Connection con = DBUtil.getConnection()) {
        	
        	

            // ===== GET EMPLOYEE =====
        	String empId = null;
        	String empSql = "SELECT emp_id FROM employee LIMIT 1";
        	PreparedStatement psEmp = con.prepareStatement(empSql);
        	ResultSet rsEmp = psEmp.executeQuery();

        	if (rsEmp.next()) {                    // 👈 MISSING LINE
        	    empId = rsEmp.getString("emp_id"); // 👈 MISSING LINE
        	}

        	System.out.println("Assigned empId = " + empId);


            // ===== INSERT RENTAL =====
            String rentalSql =
            	    "INSERT INTO rental (cust_id, car_id, emp_id, rental_date, days, amount, status) " +
            	    "VALUES (?, ?, ?, CURDATE(), ?, ?, 'BOOKED')";

            PreparedStatement psRental =
                con.prepareStatement(rentalSql, Statement.RETURN_GENERATED_KEYS);

            psRental.setString(1, custId);
            psRental.setInt(2, carId);
            psRental.setString(3, empId);
            psRental.setInt(4, days);
            psRental.setInt(5, amount);

            psRental.executeUpdate();

            ResultSet rsKeys = psRental.getGeneratedKeys();
            int rentalId = 0;
            if (rsKeys.next()) {
                rentalId = rsKeys.getInt(1);
            }

            // ===== INSERT PAYMENT =====
            String paymentSql =
                "INSERT INTO payment (rental_id, amount, payment_date, payment_status) " +
                "VALUES (?, ?, CURDATE(), 'PAID')";

            PreparedStatement psPayment = con.prepareStatement(paymentSql);
            psPayment.setInt(1, rentalId);
            psPayment.setInt(2, amount);
            psPayment.executeUpdate();

            // ===== UPDATE CAR =====
            String updateCar =
                "UPDATE car SET available = FALSE WHERE car_id = ?";

            PreparedStatement psCar = con.prepareStatement(updateCar);
            psCar.setInt(1, carId);
            psCar.executeUpdate();

            response.sendRedirect("bookingSuccess.html");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("bookingFailed.html");
        }
    }
}

