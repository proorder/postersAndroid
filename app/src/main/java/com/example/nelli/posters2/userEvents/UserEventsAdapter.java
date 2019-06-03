package com.example.nelli.posters2.userEvents;

import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nelli.posters2.DownloadImageTask;
import com.example.nelli.posters2.R;
import com.example.nelli.posters2.requests.EventsResponse;
import com.example.nelli.posters2.userEvents.UserEventsFragment.OnListFragmentInteractionListener;

import java.util.List;

public class UserEventsAdapter extends RecyclerView.Adapter<UserEventsAdapter.ViewHolder> {

    private List<EventsResponse> mValues;
    private final OnListFragmentInteractionListener mListener;

    public UserEventsAdapter(List<EventsResponse> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_events_list, parent, false);
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
        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.startEventFragment(mValues.get(i));
            }
        });
    }

    public void setValues(List<EventsResponse> list) {
        mValues = list;
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
        public Button open;

        public ViewHolder(View view) {
            super(view);
            vTitle = view.findViewById(R.id.item_title);
            vDate = view.findViewById(R.id.item_date);
            vInfo = view.findViewById(R.id.item_info);
            vImage = view.findViewById(R.id.image_view);
            open = view.findViewById(R.id.open);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
