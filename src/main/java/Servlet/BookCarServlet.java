package Servlet;

import dao.BookingDAO;
import model.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/bookCar")
public class BookCarServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // customer-only check
        if (session == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int carId = Integer.parseInt(request.getParameter("car_id"));
        int pricePerDay = Integer.parseInt(request.getParameter("price_per_day"));

        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");

        // AUTO CALCULATing PRICE
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int totalPrice = (int) (days * pricePerDay);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setCarId(carId);
        booking.setStartDate(startDateStr);
        booking.setEndDate(endDateStr);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");

        BookingDAO dao = new BookingDAO();
        boolean booked = dao.saveBooking(booking);

        if (booked) {
            response.sendRedirect("customerDashboard.html");
        } else {
            response.sendRedirect("bookingFailed.html");
        }
    }
}
