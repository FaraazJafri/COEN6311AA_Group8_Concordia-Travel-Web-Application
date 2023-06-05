package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/ModifyBookingServlet")
public class ModifyBookingServlet extends HttpServlet {

    private PackageDAO packageDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the implementation
        packageDAO = new PackageDAOImpl();
        customerDAO = new CustomerDAOImpl();
        userDAO = new UserDAOImpl();
        bookingDAO = new BookingDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int customerID = 0;
        if ("Customer".equals(session.getAttribute("role"))) {
            String customerUsername = (String) session.getAttribute("username");
            customerID = userDAO.getID(customerUsername);
            Customer customer = customerDAO.getSelectedCustomer(String.valueOf(customerID));

            if (customer != null) {
                String firstName = customer.getFirstName();
                String lastName = customer.getLastName();

                String fullName = firstName + " " + lastName;

                request.setAttribute("selectedCustomer", fullName);
            }
            List<Booking> customerBookings = customerDAO.getSeletedCustomerBookings(String.valueOf(customerID));


            request.setAttribute("customerBookings", customerBookings);
        }if("Agent".equals(session.getAttribute("role"))){
            String customerUsername = (String) session.getAttribute("username");
            String userId = String.valueOf(userDAO.getID(customerUsername));

            List<User> customers = userDAO.getLinkedCustomers(userId);
            List<TravelPackage> travelPackages = packageDAO.getAllPackages();

            request.setAttribute("travelPackages",travelPackages);
            request.setAttribute("customers", customers);

            request.getRequestDispatcher("modifybooking.jsp").forward(request, response);

        }
        if("Admin".equals(session.getAttribute("role"))){
            List<User> customers = userDAO.getOnlyCustomers();
            List<TravelPackage> travelPackages = packageDAO.getAllPackages();

            request.setAttribute("travelPackages",travelPackages);
            request.setAttribute("customers", customers);

            request.getRequestDispatcher("modifybooking.jsp").forward(request, response);
        }

        // Retrieve the list of customers from the database
        List<User> customers = userDAO.getOnlyCustomers();
        List<TravelPackage> travelPackages = packageDAO.getAllPackages();

        request.setAttribute("travelPackages",travelPackages);
        request.setAttribute("customers", customers);

        request.getRequestDispatcher("modifybooking.jsp").forward(request, response);

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the form data
        String bookingId = request.getParameter("bookingId");
        String packageId = request.getParameter("packageId");
        String departureDate = request.getParameter("departureDate");

        // Convert departure and arrival strings to timestamps
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Timestamp departure = null;

        try {
            departure = new Timestamp(inputDateFormat.parse(departureDate).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Perform the modification in the database
        boolean success = bookingDAO.modifyBooking(bookingId, packageId, departure);

        if (success) {
            String heading = "Modify Booking";
            String message = "Your booking has been modified successfully!";
            request.setAttribute("heading", heading);
            request.setAttribute("message", message);
            request.getRequestDispatcher("modify_success.jsp").forward(request,response);
        } else {
            // Modification failed, redirect to an error page
            response.sendRedirect("error.jsp");
        }
    }
}
