package com.example.nelli.posters2.infrastructures;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nelli.posters2.R;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.InfrastructuresResponse;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class EditableInfrastrcuturesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private EditableInfrastructuresAdapter adapter;

    public EditableInfrastrcuturesFragment() {
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
        new BackendConnection(context).getInfrastructures("")
                .subscribe(new Observer<Response<List<InfrastructuresResponse>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<List<InfrastructuresResponse>> response) {
                        ((ViewGroup) getView()).removeAllViews();
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_editable_infrastrcutures, (ViewGroup) getView());

                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        List<InfrastructuresResponse> list = response.body();
                        Collections.reverse(list);
                        adapter = new EditableInfrastructuresAdapter(list, mListener, getContext());
                        recyclerView.setAdapter(adapter);

                        ((Button) view.findViewById(R.id.add_infrastructure_button)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.startCreateInfrastructureFragment(adapter);
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
        void startCreateInfrastructureFragment(EditableInfrastructuresAdapter adapter);
        void startCreateInfrastructureFragment(EditableInfrastructuresAdapter adapter, InfrastructuresResponse event);
    }
}
