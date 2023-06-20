package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage;

import java.util.List;

public interface CartDAO {

    void addToCart(String userId, String packageId, String agentId);
    List<TravelPackage> getPackagesInCartForCustomer(String userId);

    List<TravelPackage> getPackagesInCartForAgent(String userId, String agentId);

    Boolean removeFromCartForAgent(String package_id,String userId, String agentId);
    Boolean removeFromCartForCustomer(String package_id,String userId);

}
