package com.example.nelli.posters2.requests;

public class RegistrationBody {
    public String full_name;
    public String username;
    public String password;

    public RegistrationBody(String full_name, String username, String password) {
        this.full_name = full_name;
        this.username = username;
        this.password = password;
    }
}
