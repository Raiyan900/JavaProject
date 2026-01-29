package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/rentPage")
public class RentPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carId = request.getParameter("carId");
        String carName = request.getParameter("carName");
        String brand = request.getParameter("brand");
        String rate = request.getParameter("rate");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Rent Car</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; background:#f2f2f2; }");
        out.println(".box { width:400px; margin:50px auto; background:#fff; padding:20px; border-radius:6px; }");
        out.println("input, button { width:100%; padding:8px; margin:6px 0; }");
        out.println("button { background:#4CAF50; color:white; border:none; font-size:16px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='box'>");
        out.println("<h2>Rent Car</h2>");

        out.println("<form action='rentCar' method='post'>");

        // Hidden values
        out.println("<input type='hidden' name='car_id' value='" + carId + "'>");
        out.println("<input type='hidden' name='rate' value='" + rate + "'>");

        out.println("Car Name:");
        out.println("<input type='text' value='" + carName + "' readonly>");

        out.println("Brand:");
        out.println("<input type='text' value='" + brand + "' readonly>");

        out.println("Price Per Day:");
        out.println("<input type='text' value='" + rate + "' readonly>");

        out.println("Rental Date:");
        out.println("<input type='date' name='rental_date' required>");

        out.println("Number of Days:");
        out.println("<input type='number' name='days' min='1' required>");

        out.println("<button type='submit'>Confirm Rent</button>");
        out.println("</form>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
