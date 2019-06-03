package com.example.nelli.posters2.requests;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginRequestInterface {
    @POST("api/v1/auth")
    Observable<LoginResponse> login(@Body LoginBody loginBody);

    @POST("api/v1/registration")
    Observable<Response<LoginResponse>> registration(@Body RegistrationBody registrationBody);

    @POST("api/v1/profile/name")
    Observable<ResponseBody> changeName(@Body Name name);

    @GET("api/v1/users")
    Observable<Response<List<LoginResponse>>> getUsersList();

    @POST("api/v1/make_manager")
    Observable<ResponseBody> makeManager(@Body EventId id);
}
