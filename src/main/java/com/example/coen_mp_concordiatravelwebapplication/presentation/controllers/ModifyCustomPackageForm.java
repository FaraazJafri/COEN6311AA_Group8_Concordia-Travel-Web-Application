package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomPackageDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CustomPackageDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/ModifyCustomPackageForm")
public class ModifyCustomPackageForm extends HttpServlet {
    private UserDAO userDAO;
    private CustomPackageDAO customPackageDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAOImpl();
        customPackageDAO = new CustomPackageDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String customerUsername = (String) session.getAttribute("username");
        String userID = String.valueOf(userDAO.getID(customerUsername));

        List<TravelPackage> userPackages = customPackageDAO.retrieveUserPackages(userID);
        String packageId = request.getParameter("packageId");
        request.setAttribute("packageId", packageId);
        request.setAttribute("userPackages", userPackages);
        request.getRequestDispatcher("modifycustompackageform.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String packageId = request.getParameter("packageId");
        String activityIds = request.getParameter("activityIds");
        String flightIds = request.getParameter("flightIds");
        String hotelIds = request.getParameter("hotelIds");
        HttpSession session = request.getSession();
        String customerUsername = (String) session.getAttribute("username");
        String userID = String.valueOf(userDAO.getID(customerUsername));

        // Redirect back to the user packages page
        response.sendRedirect("UserPackages");
    }
}
