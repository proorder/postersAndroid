package com.example.nelli.posters2.requests;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InfrastructuresRequestInterface {

    @GET("api/v1/infrastructures")
    Observable<Response<List<InfrastructuresResponse>>> get(@Query("search") String search);

    @Multipart
    @POST("api/v1/infrastructures")
    Observable<Response<InfrastructuresResponse>> post(
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("api/v1/infrastructures")
    Observable<Response<InfrastructuresResponse>> update(
            @Part("id") RequestBody id,
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("api/v1/infrastructures")
    Observable<Response<InfrastructuresResponse>> update(
            @Part("id") RequestBody id,
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude
    );

    @HTTP(method = "DELETE", path = "/api/v1/infrastructures", hasBody = true)
    Observable<ResponseBody> delete(@Body EventId eventId);
}
