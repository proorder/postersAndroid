package com.example.nelli.posters2.requests;

import java.util.HashMap;
import java.util.List;

public class LoginResponse {
    public int id;
    public String token;
    public String full_name;
    public String is_superuser;
    public String is_staff;
    public List<HashMap> errors;
}
