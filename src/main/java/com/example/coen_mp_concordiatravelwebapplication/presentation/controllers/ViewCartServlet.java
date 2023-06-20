package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;


import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CartDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CartDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewCartServlet", value = "/ViewCartServlet")
public class ViewCartServlet extends HttpServlet {

    private CartDAO cartDAO;
    private UserDAO userDAO;

    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String customerUsername = (String) session.getAttribute("username");


        String userId = String.valueOf(userDAO.getID(customerUsername));

        List<User> customers = userDAO.getLinkedCustomers(userId);

        List<TravelPackage> travelPackages = cartDAO.getPackagesInCartForCustomer(userId);

        request.setAttribute("customers", customers);
        request.setAttribute("travelPackages", travelPackages);
        request.getRequestDispatcher("viewcart.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();


        String customerUsername = (String) session.getAttribute("username");


        String userId = String.valueOf(userDAO.getID(customerUsername));


        List<User> customers = userDAO.getLinkedCustomers(userId);
        String customerId = request.getParameter("customerId");


        List<TravelPackage> travelPackages = cartDAO.getPackagesInCartForAgent(customerId, userId);

        request.setAttribute("customers", customers);
        request.setAttribute("travelPackages", travelPackages);
        request.setAttribute("selectedCustomerId", customerId);
        request.setAttribute("selectedAgentId", userId);

        request.getRequestDispatcher("viewcart.jsp").forward(request, response);
    }


}
