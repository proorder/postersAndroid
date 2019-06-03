package com.example.nelli.posters2.infrastructures;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nelli.posters2.DownloadImageTask;
import com.example.nelli.posters2.R;
import com.example.nelli.posters2.infrastructures.EditableInfrastrcuturesFragment.OnListFragmentInteractionListener;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.InfrastructuresResponse;

import java.util.List;
import java.util.ListIterator;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EditableInfrastructuresAdapter extends RecyclerView.Adapter<EditableInfrastructuresAdapter.ViewHolder> {

    private final List<InfrastructuresResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public EditableInfrastructuresAdapter(List<InfrastructuresResponse> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editable_infrastrcutures_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        holder.vTitle.setText(mValues.get(i).title);
        holder.vInfo.setText(mValues.get(i).info);
        if (!mValues.get(i).images.isEmpty()) {
            new DownloadImageTask(holder.vImage).execute("http://10.0.2.2:8000/media/" + mValues.get(i).images.get(0));
        }
        holder.changeInfrastructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfrastructure(i);
            }
        });
        holder.deleteInfrastructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deleteInfrastructure.setEnabled(false);
                holder.progressBar.setVisibility(View.VISIBLE);
                new BackendConnection(context).deleteInfrastructure(mValues.get(i).id)
                        .subscribe(new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {}

                            @Override
                            public void onNext(Object o) {
                                holder.deleteInfrastructure.setEnabled(true);
                                holder.progressBar.setVisibility(View.GONE);
                                mValues.remove(i);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onComplete() {}
                        });
            }
        });
    }

    private void changeInfrastructure(int i) {
        mListener.startCreateInfrastructureFragment(this, mValues.get(i));
    }

    public void addItem(InfrastructuresResponse item) {
        mValues.add(0, item);
        notifyDataSetChanged();
    }

    public void setById(InfrastructuresResponse item) {
        for (ListIterator<InfrastructuresResponse> iterator = mValues.listIterator(); iterator.hasNext(); ) {
            if (iterator.next().id == item.id) {
                iterator.set(item);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vTitle;
        public TextView vInfo;
        public ImageView vImage;
        public Button changeInfrastructure;
        public Button deleteInfrastructure;
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.item_title);
            vInfo = view.findViewById(R.id.item_info);
            vImage = view.findViewById(R.id.image_view);
            changeInfrastructure = view.findViewById(R.id.change_event);
            deleteInfrastructure = view.findViewById(R.id.delete_event);
            progressBar = view.findViewById(R.id.item_progress);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
