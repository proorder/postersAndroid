package com.example.nelli.posters2.requests;

import java.util.List;

public class EventsResponse {
    public int id;
    public String owner;
    public String title;
    public String place;
    public String date;
    public String info;
    public float cost_of_entry;
    public float latitude;
    public float longitude;
    public List<String> images;

    public EventsResponse(int id, String owner, String title, String place, String date, String info, float cost_of_entry, float latitude, float longitude, List<String> images) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.place = place;
        this.date = date;
        this.info = info;
        this.cost_of_entry = cost_of_entry;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
    }
}
