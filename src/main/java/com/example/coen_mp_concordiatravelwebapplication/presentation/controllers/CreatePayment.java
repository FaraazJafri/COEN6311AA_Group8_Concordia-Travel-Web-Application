package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.google.gson.annotations.SerializedName;

class CreatePayment {
    @SerializedName("items")
    Object[] items;

    public Object[] getItems() {
        return items;
    }
}
