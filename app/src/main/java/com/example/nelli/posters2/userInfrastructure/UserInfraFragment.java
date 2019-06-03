package com.example.nelli.posters2.userInfrastructure;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.nelli.posters2.R;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.EventsResponse;
import com.example.nelli.posters2.requests.InfrastructuresResponse;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class UserInfraFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private UserInfraAdapter adapter;
    private long lastKeyUp;
    private String search;

    public UserInfraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preloader_view, container, false);
        return view;
    }


    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        new BackendConnection(context).getInfrastructures("").subscribe(new Observer<Response<List<InfrastructuresResponse>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<InfrastructuresResponse>> response) {
                ((ViewGroup) getView()).removeAllViews();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_events, (ViewGroup) getView());

                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                List<InfrastructuresResponse> list = response.body();
                Collections.reverse(list);
                adapter = new UserInfraAdapter(list, mListener);
                recyclerView.setAdapter(adapter);

                ((EditText) view.findViewById(R.id.search)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        lastKeyUp = System.currentTimeMillis();
                        search = String.valueOf(charSequence);
                        Log.i("LOG LOG TIME", String.valueOf(lastKeyUp));
                        new TimeWaiter().execute();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    private void newInfraRequest() {
        new BackendConnection(getContext()).getInfrastructures(search)
                .subscribe(new Observer<Response<List<InfrastructuresResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<List<InfrastructuresResponse>> res) {
                        List<InfrastructuresResponse> myList = res.body();
                        Collections.reverse(myList);
                        adapter.setValues(myList);
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    class TimeWaiter extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            if (System.currentTimeMillis() - lastKeyUp > 500) {
                newInfraRequest();
            }
            return null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void startInfraFragment(InfrastructuresResponse infra);
    }
}
