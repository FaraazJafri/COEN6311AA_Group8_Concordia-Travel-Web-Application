package com.example.coen_mp_concordiatravelwebapplication.models.bookingModels;

public class Cart {
    private int cartId;
    private int packageId;
    private int userId;

    public Cart(int packageId, int userId) {
        this.packageId = packageId;
        this.userId = userId;
    }

    public int getCartId() {
        return cartId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
