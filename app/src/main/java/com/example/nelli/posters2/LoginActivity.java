package com.example.nelli.posters2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.LoginBody;
import com.example.nelli.posters2.requests.LoginResponse;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login)
    EditText login;
    @BindView(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferences sh = getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
        if (sh.contains("Token")) {
            if (sh.getString("UserType", "").equals("admin")) {
                goToAdmin();
            } else if (sh.getString("UserType", "").equals("manager")) {
                goToManagerActivity();
            } else {
                goToUserActivity();
            }
        }
    }

    @OnClick(R.id.registration_button)
    void goToRegistration() {
        startActivity(new Intent(this, RegistrActivity.class));
    }

    @OnClick(R.id.login_button)
    void authorization() {
        new BackendConnection(this).login(new LoginBody(String.valueOf(login.getText()), String.valueOf(password.getText())))
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(LoginResponse loginResponse) {
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

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LOG ERROR", e.toString());
                    }

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
