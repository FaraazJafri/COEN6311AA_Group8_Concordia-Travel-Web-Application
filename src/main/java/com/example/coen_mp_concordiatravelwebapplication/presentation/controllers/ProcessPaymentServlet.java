package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Cart;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "ProcessPaymentServlet", value = "/ProcessPaymentServlet")
public class ProcessPaymentServlet extends HttpServlet {


    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    private NotificationDAO notificationDAO;


    private CartDAO cartDAO;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
        notificationDAO = new NotificationDAOImpl();
        paymentDAO = new PaymentDAOImpl();
        cartDAO = new CartDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {


        String customerId = request.getParameter("customerId");
        String agentId = request.getParameter("agentId");
        String[] packageIds = request.getParameterValues("packageIds");
        String totalBill = request.getParameter("totalBill");

        if (request.getParameter("condition").equals("customPackage")) {
            request.setAttribute("condition", "customPackage");
        }


        request.setAttribute("selectedCustomer", customerId);
        request.setAttribute("selectedAgent", agentId);
        request.setAttribute("packageIds", packageIds);
        request.setAttribute("paymentAmount", totalBill);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String paymentAmountStr = request.getParameter("paymentAmount");
        double paymentAmount = Double.parseDouble(paymentAmountStr);
        String cardNumber = request.getParameter("cardNumber");
        String[] packageIds = request.getParameterValues("packageIds");
        String customerId = request.getParameter("customerId");
        String agentId = request.getParameter("agentId");


        if (customerId.equals("null")) {
            String customerUserName = (String) session.getAttribute("username");
            customerId = String.valueOf(userDAO.getID(customerUserName));
        }

        // Process the payment and get the status
        String status = paymentDAO.processPayment(String.valueOf(paymentAmount), cardNumber);

        if (status.equals("succeeded")) {
            // Payment succeeded

            for (int i = 0; i < packageIds.length; i++) {
                String bookingId = generateBookingId();


                if (!request.getParameter("condition").equals("customPackage")) {
                    Timestamp departureDate = cartDAO.getDepartureDate(customerId, packageIds[i], null, agentId);


                    bookingDAO.saveBooking(bookingId, packageIds[i], customerId, departureDate);

                } else {
                    int packageIdtoBook = Integer.parseInt(packageIds[i]) + 499;
                    Timestamp departureDate = cartDAO.getDepartureDate(customerId, null, String.valueOf(packageIdtoBook), agentId);


                    bookingDAO.saveCustomBooking(bookingId, String.valueOf(packageIdtoBook), customerId, departureDate);

                }

                //Notification code
                if ("Customer".equals(session.getAttribute("role"))) {
                    String message = "The booking was successful with the booking ID: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
                    if (!request.getParameter("condition").equals("customPackage")) {
                        cartDAO.removeFromCartForCustomer(packageIds[i] + 499, customerId);
                    } else {
                        int packageIdtoBook = Integer.parseInt(packageIds[i]) + 499;
                        cartDAO.removeFromCartForUserPackage(String.valueOf(packageIdtoBook), customerId);
                    }
                    notificationDAO.sendNotificationToUser(String.valueOf(customerId), message);
                } else if ("Agent".equals(session.getAttribute("role"))) {
                    String customerUsername = (String) session.getAttribute("username");
                    agentId = String.valueOf(userDAO.getID(customerUsername));

                    User user = userDAO.getUserById(String.valueOf(agentId));

                    String fullname = user.getFirstName() + " " + user.getLastName();

                    String message = "Your agent: " + fullname + " has booked a package with booking Id: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
                    cartDAO.removeFromCartForAgent(packageIds[i], customerId, agentId);
                    notificationDAO.sendNotificationToUser(String.valueOf(customerId), message);
                } else if ("Admin".equals(session.getAttribute("role"))) {

                    String message = "The Admin has booked a package with booking Id: " + bookingId + ", check your bookings in (View Your Bookings) section now!";
                    cartDAO.removeFromCartForAgent(packageIds[i], customerId, agentId);
                    notificationDAO.sendNotificationToUser(String.valueOf(customerId), message);
                }
            }

            request.setAttribute("heading", "Payment Confirmation:");
            request.setAttribute("message", "Your Payment is successfully processed!!");
            List<Booking> bookings = bookingDAO.getAllBookings();
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("modify_success.jsp").forward(request, response);
        } else {
            // Payment failed
            request.setAttribute("errorMessage", "Payment Failed:");
            request.setAttribute("errorCode", "Your Payment has failed!!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }


    private String generateBookingId() {
        int random = (int) (Math.random() * 9000) + 1000;
        long timestamp = System.currentTimeMillis();
        String bookingId = String.valueOf(random) + String.valueOf(timestamp);
        bookingId = bookingId.substring(bookingId.length() - 4);
        return bookingId;
    }
}
