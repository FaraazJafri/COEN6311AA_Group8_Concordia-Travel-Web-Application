package com.example.coen_mp_concordiatravelwebapplication;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CustomerBookingsServlet")
public class CustomerBookingsServlet extends HttpServlet {
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
        // Retrieve the list of customers from the database
        List<Customer> customers = customerDAO.getAllCustomers();

        // Set the list of customers as a request attribute
        request.setAttribute("customers", customers);

        String condition = request.getParameter("condition");
        if (condition != null && condition.equals("Cancel")) {
            request.getRequestDispatcher("cancelbooking.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("bookings.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Booking> customerBookings = new ArrayList<>();
        String customerId = request.getParameter("customerId");
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();

            // Retrieve customer bookings from the database for the selected customer
            String selectQuery = "SELECT b.bookingId, b.packageId, b.departureDate, c.firstName, c.lastName " +
                    "FROM bookings AS b " +
                    "JOIN customer AS c ON b.customerId = c.customerId " +
                    "WHERE b.customerId = '" + customerId + "'";
            ResultSet resultSet = statement.executeQuery(selectQuery);


            while (resultSet.next()) {

                // Retrieve customer information
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                String fullName = firstName + " " + lastName;
                // Set the selected customer attribute

                request.setAttribute("selectedCustomer", fullName);


                String bookingIdResult = resultSet.getString("bookingId");
                String packageIdResult = resultSet.getString("packageId");
                Timestamp departureDateResult = resultSet.getTimestamp("departureDate");

                Booking booking = new Booking(bookingIdResult, packageIdResult, customerId, departureDateResult);
                customerBookings.add(booking);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("customerBookings", customerBookings);
        doGet(request, response);
    }
}