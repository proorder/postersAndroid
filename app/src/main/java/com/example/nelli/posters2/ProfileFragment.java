package com.example.nelli.posters2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nelli.posters2.requests.BackendConnection;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class ProfileFragment extends Fragment {

    private TextView fullName;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fullName = view.findViewById(R.id.full_name);
        final SharedPreferences sh = getContext().getSharedPreferences("MyRefs", Context.MODE_PRIVATE);
        fullName.setText(sh.getString("FullName", ""));

        ((Button) view.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackendConnection(getContext()).changeName(String.valueOf(fullName.getText()))
                        .subscribe(new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {}

                            @Override
                            public void onNext(Object o) {
                                SharedPreferences.Editor editor = sh.edit();
                                editor.putString("FullName", String.valueOf(fullName.getText()));
                                editor.commit();
                                Toast toast = Toast.makeText(getContext(), "Готово!", Toast.LENGTH_SHORT);
                                toast.show();
                                mListener.changeName(String.valueOf(fullName.getText()));
                            }

                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onComplete() {}
                        });
            }
        });

        ((Button) view.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.closeProfile();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void changeName(String name);
        void closeProfile();
    }
}
