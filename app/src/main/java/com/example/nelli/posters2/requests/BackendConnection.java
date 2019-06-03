package com.example.nelli.posters2.requests;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

public class BackendConnection {

    public static String BASE_URL = "http://10.0.2.2:8000";
    private Retrofit retrofit;

    public BackendConnection(Context context) {
        Gson gson = new GsonBuilder().create();

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        if (!token.equals("")) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Token " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }


    public Observable registration(@Body RegistrationBody registrationBody) {
        final LoginRequestInterface loginApi = retrofit.create(LoginRequestInterface.class);
        return loginApi.registration(registrationBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable login(@Body LoginBody loginBody) {
        final LoginRequestInterface loginApi = retrofit.create(LoginRequestInterface.class);
        return loginApi.login(loginBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getEvents(String search) {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.getEvents(search.trim()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getMyEvents() {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.getMyEvents().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable postEvent(String title, String info, String place, String date, String cost_of_entry, String latitude, String longitude, File image) {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.postEvent(
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), date),
                RequestBody.create(MediaType.parse("multipart/form-data"), cost_of_entry),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude),
                MultipartBody.Part.createFormData("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable updateEvent(String id, String title, String info, String place, String date, String cost_of_entry, String latitude, String longitude, File image) {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.updateEvent(
                RequestBody.create(MediaType.parse("multipart/form-data"), id),
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), date),
                RequestBody.create(MediaType.parse("multipart/form-data"), cost_of_entry),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude),
                MultipartBody.Part.createFormData("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable updateEvent(String id, String title, String info, String place, String date, String cost_of_entry, String latitude, String longitude) {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.updateEvent(
                RequestBody.create(MediaType.parse("multipart/form-data"), id),
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), date),
                RequestBody.create(MediaType.parse("multipart/form-data"), cost_of_entry),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude)
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable deleteEvent(int id) {
        final EventsRequestInterface api = retrofit.create(EventsRequestInterface.class);
        return api.deleteEvent(new EventId(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getInfrastructures(String search) {
        final InfrastructuresRequestInterface api = retrofit.create(InfrastructuresRequestInterface.class);
        return api.get(search).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable postInfrastructure(String title, String info, String place, String latitude, String longitude, File image) {
        final InfrastructuresRequestInterface api = retrofit.create(InfrastructuresRequestInterface.class);
        return api.post(
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude),
                MultipartBody.Part.createFormData("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable updateInfrastructure(String id, String title, String info, String place, String latitude, String longitude, File image) {
        final InfrastructuresRequestInterface api = retrofit.create(InfrastructuresRequestInterface.class);
        return api.update(
                RequestBody.create(MediaType.parse("multipart/form-data"), id),
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude),
                MultipartBody.Part.createFormData("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable updateInfrastructure(String id, String title, String info, String place, String latitude, String longitude) {
        final InfrastructuresRequestInterface api = retrofit.create(InfrastructuresRequestInterface.class);
        return api.update(
                RequestBody.create(MediaType.parse("multipart/form-data"), id),
                RequestBody.create(MediaType.parse("multipart/form-data"), title),
                RequestBody.create(MediaType.parse("multipart/form-data"), info),
                RequestBody.create(MediaType.parse("multipart/form-data"), place),
                RequestBody.create(MediaType.parse("multipart/form-data"), latitude),
                RequestBody.create(MediaType.parse("multipart/form-data"), longitude)
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable deleteInfrastructure(int id) {
        final InfrastructuresRequestInterface api = retrofit.create(InfrastructuresRequestInterface.class);
        return api.delete(new EventId(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable changeName(String name) {
        final LoginRequestInterface api = retrofit.create(LoginRequestInterface.class);
        return api.changeName(new Name(name)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable getUsersList() {
        final LoginRequestInterface api = retrofit.create(LoginRequestInterface.class);
        return api.getUsersList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable makeManager(int id) {
        final LoginRequestInterface api = retrofit.create(LoginRequestInterface.class);
        return api.makeManager(new EventId(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}

class EventId {
    public int id;

    public EventId(int id) {
        this.id = id;
    }
}

class Name {
    public String name;

    public Name(String name) {
        this.name = name;
    }
}