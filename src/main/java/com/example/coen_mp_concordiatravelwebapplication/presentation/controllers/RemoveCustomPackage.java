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

@WebServlet("/RemoveCustomPackage")
public class RemoveCustomPackage extends HttpServlet {
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

        request.setAttribute("userPackages", userPackages);
        request.getRequestDispatcher("removecustompackage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the package ID parameter from the request
        String packageId = request.getParameter("packageId");
        packageId = String.valueOf(Integer.parseInt(packageId)+499);
        if (customPackageDAO.deletePackage(packageId)) {
            request.setAttribute("heading", "Custom Package Deleted:");
            request.setAttribute("message", "Your package is deleted successfully!!");
            request.getRequestDispatcher("modify_success.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Custom Package Deletion Failed:");
            request.setAttribute("errorCode", "Your package could not be deleted!!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
