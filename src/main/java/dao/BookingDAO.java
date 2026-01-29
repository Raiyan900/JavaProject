package dao;

import db.DBUtil;
import model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookingDAO {

    public boolean saveBooking(Booking booking) {
        boolean success = false;

        try {
            Connection con = DBUtil.getConnection();

            // Generate rental_id (since no auto increment)
            String rentalId = "R" + System.currentTimeMillis();

            // 1️⃣ Insert into Rental table
            String sql = "INSERT INTO Rental " +
                         "(rental_id, rental_date, cust, rep, car, days, amount) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, rentalId);
            ps.setString(2, booking.getStartDate());  
            ps.setString(3, booking.getCustId());      
            ps.setString(4, booking.getEmpId());      
            ps.setString(5, booking.getCarId());  
            ps.setInt(6, booking.getDays()); 
            ps.setDouble(7, booking.getTotalPrice());

            int rows = ps.executeUpdate();

            if (rows > 0) {

                //Insert into Payment table
                String paySql = "INSERT INTO Payment " +
                                "(payment_id, payment_date, rental, amount, mode) " +
                                "VALUES (?, CURDATE(), ?, ?, ?)";

                PreparedStatement psPay = con.prepareStatement(paySql);

                String paymentId = "P" + System.currentTimeMillis();

                psPay.setString(1, paymentId);
                psPay.setString(2, rentalId);
                psPay.setDouble(3, booking.getTotalPrice());
                psPay.setString(4, "CASH");   // or UPI / Card

                psPay.executeUpdate();

                //Update Car status to Rented
                PreparedStatement psCar = con.prepareStatement(
                        "UPDATE Car SET status = 'Rented' WHERE car_id = ?");

                psCar.setString(1, booking.getCarId());
                psCar.executeUpdate();

                success = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
}
