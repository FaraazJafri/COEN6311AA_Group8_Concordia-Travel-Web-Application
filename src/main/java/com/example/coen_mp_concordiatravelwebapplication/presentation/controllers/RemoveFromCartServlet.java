package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CartDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.CartDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.UserDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import com.example.coen_mp_concordiatravelwebapplication.models.userModels.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RemoveFromCartServlet", value = "/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    private CartDAO cartDAO;
    private UserDAO userDAO;

    public void init() throws ServletException {
        cartDAO = new CartDAOImpl();
        userDAO = new UserDAOImpl();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        if(!request.getParameter("condition").equals("customPackage")) {
            if ("Agent".equals(session.getAttribute("role")) || "Admin".equals(session.getAttribute("role"))) {
                String customerId = request.getParameter("customerId");
                String packageId = request.getParameter("packageId");

                String customerUsername = (String) session.getAttribute("username");
                String agentId = String.valueOf(userDAO.getID(customerUsername));

                boolean removed = cartDAO.removeFromCartForAgent(packageId, customerId, agentId);

                if (removed) {
                    request.setAttribute("removalResult", "Successfully removed the package from the cart.");
                } else {
                    request.setAttribute("removalResult", "Failed to remove the package from the cart.");
                }
            } else {
                String customerUsername = (String) session.getAttribute("username");
                String customerId = String.valueOf(userDAO.getID(customerUsername));
                String packageId = request.getParameter("packageId");

                boolean removed = cartDAO.removeFromCartForCustomer(packageId, customerId);

                if (removed) {
                    request.setAttribute("removalResult", "Successfully removed the package from the cart.");
                } else {
                    request.setAttribute("removalResult", "Failed to remove the package from the cart.");
                }
            }


            request.getRequestDispatcher("ViewCartServlet").forward(request, response);
        }else{
            String customerUsername = (String) session.getAttribute("username");
            String customerId = String.valueOf(userDAO.getID(customerUsername));
            int packageId = Integer.parseInt(request.getParameter("packageId")) + 499;

            boolean removed = cartDAO.removeFromCartForUserPackage(String.valueOf(packageId), customerId);

            if (removed) {
                request.setAttribute("removalResult", "Successfully removed the package from the cart.");
            } else {
                request.setAttribute("removalResult", "Failed to remove the package from the cart.");
            }
            request.getRequestDispatcher("ViewCartServlet").forward(request, response);
        }


    }
}
