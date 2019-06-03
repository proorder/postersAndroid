package com.example.nelli.posters2.requests;

import java.util.List;

public class InfrastructuresResponse {
    public int id;
    public String title;
    public String place;
    public String info;
    public float latitude;
    public float longitude;
    public List<String> images;

    public InfrastructuresResponse(int id, String title, String place, String info, float latitude, float longitude, List<String> images) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
    }
}
