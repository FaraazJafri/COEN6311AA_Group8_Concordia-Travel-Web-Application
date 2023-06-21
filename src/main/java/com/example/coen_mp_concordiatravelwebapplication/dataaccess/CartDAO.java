package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.sql.Timestamp;
import java.util.List;

public interface CartDAO {

    void addToCart(String userId, String packageId, String agentId, String customPackageId, Timestamp departureDate);
    List<TravelPackage> getPackagesInCartForCustomer(String userId);

    List<TravelPackage> getPackagesInCartForAgent(String userId, String agentId);

    Boolean removeFromCartForAgent(String package_id,String userId, String agentId);
    Boolean removeFromCartForCustomer(String package_id,String userId);

    List<String> getPackageIdsInCartForCustomer(String userId);

    List<String> getPackageIdsInCartForAgent(String userId, String agentId);

    boolean checkDuplicatePackage(String userId, String packageId, String agentId);

    Timestamp getDepartureDate(String userId, String packageId, String customPackageId, String agentId);
    List<String> getCustomPackageIdsInCartForCustomer(String userId);

    Boolean removeFromCartForUserPackage(String customPackageId, String userId);


}
