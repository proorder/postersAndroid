package com.example.nelli.posters2.events;

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
import com.example.nelli.posters2.events.EditableEventsFragment.OnListFragmentInteractionListener;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.EventsResponse;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EditableEventsAdapter extends RecyclerView.Adapter<EditableEventsAdapter.ViewHolder> {

    private final List<EventsResponse> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public EditableEventsAdapter(List<EventsResponse> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editable_events_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        holder.vTitle.setText(mValues.get(i).title);
        holder.vDate.setText(mValues.get(i).date);
        holder.vInfo.setText(mValues.get(i).info);
        if (!mValues.get(i).images.isEmpty()) {
            new DownloadImageTask(holder.vImage).execute("http://10.0.2.2:8000/media/" + mValues.get(i).images.get(0));
        }
        holder.changeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEvent(i);
            }
        });
        holder.deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deleteEvent.setEnabled(false);
                holder.progressBar.setVisibility(View.VISIBLE);
                new BackendConnection(context).deleteEvent(mValues.get(i).id)
                        .subscribe(new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object o) {
                                holder.deleteEvent.setEnabled(true);
                                holder.progressBar.setVisibility(View.GONE);
                                mValues.remove(i);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    private void changeEvent(int i) {
        mListener.startCreateEventFragment(this, mValues.get(i));
    }

    public void addItem(EventsResponse item) {
        mValues.add(0, item);
        notifyDataSetChanged();
    }

    public void setById(EventsResponse item) {
        for (ListIterator<EventsResponse> iterator = mValues.listIterator(); iterator.hasNext(); ) {
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
        public TextView vDate;
        public TextView vInfo;
        public ImageView vImage;
        public Button changeEvent;
        public Button deleteEvent;
        public ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.item_title);
            vDate = view.findViewById(R.id.item_date);
            vInfo = view.findViewById(R.id.item_info);
            vImage = view.findViewById(R.id.image_view);
            changeEvent = view.findViewById(R.id.change_event);
            deleteEvent = view.findViewById(R.id.delete_event);
            progressBar = view.findViewById(R.id.item_progress);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
