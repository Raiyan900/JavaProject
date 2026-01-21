package dao;

import db.DBUtil;
import model.Booking;

import java.sql.*;

public class BookingDAO {

    public boolean saveBooking(Booking booking) {
        boolean success = false;

        try {
            Connection con = DBUtil.getConnection();

            // 1️ Insert booking
            String sql = "INSERT INTO bookings (user_id, car_id, start_date, end_date, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getCarId());
            ps.setString(3, booking.getStartDate());
            ps.setString(4, booking.getEndDate());
            ps.setInt(5, booking.getTotalPrice());
            ps.setString(6, booking.getStatus());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    int bookingId = rs.getInt(1);

                    // 2️ Insert payment
                    String paySql = "INSERT INTO payments (booking_id, amount, payment_status, payment_date) VALUES (?, ?, ?, NOW())";
                    PreparedStatement psPay = con.prepareStatement(paySql);
                    psPay.setInt(1, bookingId);
                    psPay.setInt(2, booking.getTotalPrice());
                    psPay.setString(3, "PAID");
                    psPay.executeUpdate();

                    // 3️ Mark car unavailable
                    PreparedStatement psCar = con.prepareStatement(
                            "UPDATE cars SET available = false WHERE id = ?");
                    psCar.setInt(1, booking.getCarId());
                    psCar.executeUpdate();

                    success = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }
}
