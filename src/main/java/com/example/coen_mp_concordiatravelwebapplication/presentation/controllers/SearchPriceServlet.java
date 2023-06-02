package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAO;
import com.example.coen_mp_concordiatravelwebapplication.dataaccess.PackageDAOImpl;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchPriceServlet", value = "/SearchPriceServlet")
public class SearchPriceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PackageDAO packageDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the PackageDAO implementation
        packageDAO = new PackageDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the price range from the form
        int minPrice = Integer.parseInt(request.getParameter("minPrice"));
        int maxPrice = Integer.parseInt(request.getParameter("maxPrice"));

        List<TravelPackage> searchResults = packageDAO.searchByPrice(minPrice, maxPrice);

        // Set the search results as a request attribute
        request.setAttribute("searchResults", searchResults);

        // Forward the request to the displaysearchresults.jsp page
        request.getRequestDispatcher("/displaysearchresults.jsp").forward(request, response);
    }
}