package com.example.nelli.posters2.managers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nelli.posters2.R;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.LoginResponse;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ManagersFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    public ManagersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_managers_list, container, false);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        new BackendConnection(getContext()).getUsersList()
                .subscribe(new Observer<Response<List<LoginResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<List<LoginResponse>> response) {
                        try {
                            ((ViewGroup) getView()).removeAllViews();
                        } catch (NullPointerException e) {}
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.fragment_managers_list, (ViewGroup) getView());
                        RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.list);
                        Log.i("LOG LOG LOG LOG", String.valueOf(recyclerView == null));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        BackendConnection connection = new BackendConnection(getContext());
                        recyclerView.setAdapter(new ManagersAdapter(response.body(), mListener, connection));
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
    }
}
