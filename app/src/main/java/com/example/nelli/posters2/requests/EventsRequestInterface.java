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

public interface EventsRequestInterface {
    @GET("api/v1/events")
    Observable<Response<List<EventsResponse>>> getEvents(@Query("search") String search);

    @GET("api/v1/my_events")
    Observable<Response<List<EventsResponse>>> getMyEvents();

    @Multipart
    @POST("api/v1/events")
    Observable<Response<EventsResponse>> postEvent(
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("date") RequestBody date,
            @Part("cost_of_entry") RequestBody cost_of_entry,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("api/v1/events")
    Observable<Response<EventsResponse>> updateEvent(
            @Part("id") RequestBody id,
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("date") RequestBody date,
            @Part("cost_of_entry") RequestBody cost_of_entry,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part file
    );

    @Multipart
    @PUT("api/v1/events")
    Observable<Response<EventsResponse>> updateEvent(
            @Part("id") RequestBody id,
            @Part("title") RequestBody title,
            @Part("info") RequestBody  info,
            @Part("place") RequestBody  place,
            @Part("date") RequestBody date,
            @Part("cost_of_entry") RequestBody cost_of_entry,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude
    );

    @HTTP(method = "DELETE", path = "/api/v1/events", hasBody = true)
    Observable<ResponseBody> deleteEvent(@Body EventId eventId);
}
