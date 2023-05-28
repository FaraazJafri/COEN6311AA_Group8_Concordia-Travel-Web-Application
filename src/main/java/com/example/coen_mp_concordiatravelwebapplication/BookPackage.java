package com.example.coen_mp_concordiatravelwebapplication;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Bookings;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.coen_mp_concordiatravelwebapplication.config.CONFIG;

@WebServlet(name = "BookPackageServlet", value = "/BookPackageServlet")
public class BookPackage extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Create an empty list to store travel packages
        List<TravelPackage> travelPackages = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Bookings> bookings = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(CONFIG.SQLURL, CONFIG.SQLUSER, CONFIG.SQLPASS)) {
            // Create a statement
            Statement statement = connection.createStatement();

            // Retrieve travel packages
            String query = "SELECT * FROM travel_package";
            ResultSet resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                String packageId = resultSet.getString("package_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                // Create a travel package object
                TravelPackage travelPackage = new TravelPackage(packageId, name, description, price,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                // Add the travel package to the list
                travelPackages.add(travelPackage);
            }

            // Retrieve flights, hotels, and activities for each travel package
            for (TravelPackage travelPackage : travelPackages) {
                // Retrieve flights
                query = "SELECT * FROM flight INNER JOIN package_flight ON flight.flight_id = package_flight.flight_id " +
                        "WHERE package_flight.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String flightId = resultSet.getString("flight_id");
                    String airline = resultSet.getString("airline");
                    Timestamp departure = resultSet.getTimestamp("departure");
                    Timestamp arrival = resultSet.getTimestamp("arrival");
                    double flightPrice = resultSet.getDouble("price");

                    Flight flight = new Flight(flightId, airline, departure, arrival, flightPrice);
                    travelPackage.addFlight(flight);
                }

                // Retrieve hotels
                query = "SELECT * FROM hotel INNER JOIN package_hotel ON hotel.hotel_id = package_hotel.hotel_id " +
                        "WHERE package_hotel.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String hotelId = resultSet.getString("hotel_id");
                    String hotelName = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    double hotelPrice = resultSet.getDouble("price");

                    Hotel hotel = new Hotel(hotelId, hotelName, location, hotelPrice);
                    travelPackage.addHotel(hotel);
                }

                // Retrieve activities
                query = "SELECT * FROM activity INNER JOIN package_activity ON activity.activity_id = package_activity.activity_id " +
                        "WHERE package_activity.package_id = '" + travelPackage.getPackageId() + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String activityId = resultSet.getString("activity_id");
                    String activityName = resultSet.getString("name");
                    String activityDescription = resultSet.getString("description");
                    double activityPrice = resultSet.getDouble("price");

                    Activity activity = new Activity(activityId, activityName, activityDescription, activityPrice);
                    travelPackage.addActivity(activity);
                }
            }

            String queryCustomer = "SELECT * FROM Customer";
            ResultSet resultSetCustomer = statement.executeQuery(queryCustomer);

            // Process the result set
            while (resultSetCustomer.next()) {
                String customerId = resultSetCustomer.getString("customerId");
                String firstName = resultSetCustomer.getString("firstName");
                String lastName = resultSetCustomer.getString("lastName");
                String phoneNumber = resultSetCustomer.getString("phoneNumber");
                String emailId = resultSetCustomer.getString("emailId");
                int age = resultSetCustomer.getInt("age");
                String gender = resultSetCustomer.getString("gender");


                Customer customer = new Customer(customerId, firstName, lastName, phoneNumber, age, gender, emailId);

                // Add the travel package to the list
                customers.add(customer);
            }

            // Set the travel packages as a request attribute
            request.setAttribute("travelPackages", travelPackages);
            request.setAttribute("customers", customers);

            // Forward the request to the JSP page
            request.getRequestDispatcher("bookapackage.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // JDBC connection details
        String url = CONFIG.SQLURL;
        String username = CONFIG.SQLUSER;
        String password = CONFIG.SQLPASS;

        // Retrieve the package ID, customer ID, and departure date from the request
        String packageIdToBook = request.getParameter("packageId");
        String customerIdToBook = request.getParameter("customerId");
        String departureDate = request.getParameter("departureDate");

        String bookingId = generateBookingId();

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Establish a connection to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Create a statement
            Statement statement = connection.createStatement();

            // Insert the booking into the database
            String insertQuery = "INSERT INTO bookings (bookingId, packageId, customerId, departureDate) VALUES " +
                    "('" + bookingId + "', '" + packageIdToBook + "', '" + customerIdToBook + "', '" + departureDate + "')";
            statement.executeUpdate(insertQuery);

            // Retrieve all bookings from the database
            String selectQuery = "SELECT * FROM bookings";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Process the result set (you can customize this as per your requirement)
            List<Bookings> bookings = new ArrayList<>();
            while (resultSet.next()) {
                String bookingIdResult = resultSet.getString("bookingId");
                String packageIdResult = resultSet.getString("packageId");
                String customerIdResult = resultSet.getString("customerId");
                Timestamp departureDateResult = resultSet.getTimestamp("departureDate");

                Bookings booking = new Bookings(bookingIdResult, packageIdResult, customerIdResult, departureDateResult);
                bookings.add(booking);
            }

            // Set the bookings as a request attribute
            request.setAttribute("bookings", bookings);

            // Forward the request to the JSP page
            request.getRequestDispatcher("createpackagesuccess.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
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
