package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;


import com.example.coen_mp_concordiatravelwebapplication.dataaccess.*;
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
import org.glassfish.jersey.internal.inject.Custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ViewCartServlet", value = "/ViewCartServlet")
public class ViewCartServlet extends HttpServlet {

    private CartDAO cartDAO;
    private UserDAO userDAO;

    private CustomPackageDAO customPackageDAO;

    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        userDAO = new UserDAOImpl();
        customPackageDAO = new CustomPackageDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String customerUsername = (String) session.getAttribute("username");


        String userId = String.valueOf(userDAO.getID(customerUsername));

        List<User> customers = userDAO.getLinkedCustomers(userId);

        if("Admin".equals(session.getAttribute("role"))){
            customers = userDAO.getOnlyCustomers();
        }


        List<TravelPackage> travelPackages = cartDAO.getPackagesInCartForCustomer(userId);

        List<String> customPackagesIds = cartDAO.getCustomPackageIdsInCartForCustomer(userId);

        List<TravelPackage> userPackages = new ArrayList<>();
        for (int i = 0; i < customPackagesIds.size(); i++) {
            TravelPackage userPackage = customPackageDAO.retrieveUserPackagesForCart(userId, customPackagesIds.get(i));
            userPackages.add(userPackage);
        }


        request.setAttribute("userPackages", userPackages);
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
