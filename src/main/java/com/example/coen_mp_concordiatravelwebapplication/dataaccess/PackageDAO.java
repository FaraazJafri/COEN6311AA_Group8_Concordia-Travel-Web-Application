package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel;
import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;
import jakarta.servlet.ServletException;
import org.eclipse.persistence.jpa.jpql.parser.AbstractEclipseLinkTraverseChildrenVisitor;

import java.util.List;

public interface PackageDAO {
    List<TravelPackage> getAllPackages();

    void savePackage(String packageId, String packageName, String packageDescription, double packagePrice, List<Flight> flights, List<Hotel> hotels, List<Activity> activities);

    List<TravelPackage> searchByPrice(int minPrice, int maxPrice) throws ServletException;

    List<TravelPackage> searchByLocation(String location) throws ServletException;

    TravelPackage getSelectedTravelPackage(String packageId) throws ServletException;

    Boolean modifyPackageDetails(String packageId, TravelPackage travelPackage) throws ServletException;

    Boolean removePackage(String packageId) throws ServletException;
}