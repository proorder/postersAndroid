package com.example.nelli.posters2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.LoginResponse;
import com.example.nelli.posters2.requests.RegistrationBody;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class RegistrActivity extends AppCompatActivity {

    @BindView(R.id.full_name)
    EditText full_name;
    @BindView(R.id.login)
    EditText login;
    @BindView(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registration_button)
    void registration() {
        new BackendConnection(this).registration(new RegistrationBody(
                String.valueOf(full_name.getText()),
                String.valueOf(login.getText()),
                String.valueOf(password.getText())
        )).subscribe(new Observer<Response<LoginResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<LoginResponse> response) {
                if (response.code() == 201) {
                    LoginResponse loginResponse = response.body();
                    SharedPreferences sh = getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putString("Token", loginResponse.token);
                    editor.putString("FullName", loginResponse.full_name);
                    editor.putString("UserType", loginResponse.is_superuser);
                    editor.commit();
                    if (loginResponse.is_superuser.equals("admin")) {
                        goToAdmin();
                    } else if (loginResponse.is_superuser.equals("manager")) {
                        goToManagerActivity();
                    } else {
                        goToUserActivity();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    private void goToAdmin() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    private void goToUserActivity() {
        startActivity(new Intent(this, UserActivity.class));
    }

    private void goToManagerActivity() {
        startActivity(new Intent(this, ManagerActivity.class));
    }
}
