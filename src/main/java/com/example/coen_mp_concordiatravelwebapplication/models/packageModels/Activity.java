package com.example.coen_mp_concordiatravelwebapplication.models.packageModels;

public class Activity {
    private String activityId;
    private String name;
    private String description;
    private double price;

    public Activity(String activityId, String name, String description, double price) {
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "activityId='" + activityId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
