package com.example.nelli.posters2.events;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nelli.posters2.AdminActivity;
import com.example.nelli.posters2.R;
import com.example.nelli.posters2.UserActivity;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.EventsResponse;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class EditableEventsFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private EditableEventsAdapter adapter;

    public EditableEventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preloader_view, container, false);
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

        Observable observable;
        if (context instanceof UserActivity) {
            observable = new BackendConnection(context).getMyEvents();
        } else {
            observable = new BackendConnection(context).getEvents("");
        }

        observable.subscribe(new Observer<Response<List<EventsResponse>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<EventsResponse>> response) {
                ((ViewGroup) getView()).removeAllViews();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_editable_events, (ViewGroup) getView());

                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                List<EventsResponse> list = response.body();
                Collections.reverse(list);
                adapter = new EditableEventsAdapter(list, mListener, getContext());
                recyclerView.setAdapter(adapter);

                ((Button) view.findViewById(R.id.add_event_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.startCreateEventFragment(adapter);
                    }
                });
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
        void startCreateEventFragment(EditableEventsAdapter adapter);
        void startCreateEventFragment(EditableEventsAdapter adapter, EventsResponse event);
    }
}
