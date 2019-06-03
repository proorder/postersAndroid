package com.example.nelli.posters2.managers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nelli.posters2.R;
import com.example.nelli.posters2.managers.ManagersFragment.OnListFragmentInteractionListener;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.LoginResponse;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ManagersAdapter extends RecyclerView.Adapter<ManagersAdapter.ViewHolder> {

    private final List<LoginResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    private BackendConnection connection;

    public ManagersAdapter(List<LoginResponse> items, OnListFragmentInteractionListener listener, BackendConnection connection) {
        mValues = items;
        mListener = listener;
        this.connection = connection;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_managers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mValues.get(position).full_name);
        if (Boolean.valueOf(mValues.get(position).is_staff)) {
            holder.action.setEnabled(false);
        } else {
            holder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connection.makeManager(mValues.get(position).id)
                            .subscribe(new Observer() {
                                @Override
                                public void onSubscribe(Disposable d) {}

                                @Override
                                public void onNext(Object o) {
                                     holder.action.setEnabled(false);
                                }

                                @Override
                                public void onError(Throwable e) {}

                                @Override
                                public void onComplete() {}
                            });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final Button action;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.full_name);
            action = (Button) view.findViewById(R.id.action);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
