package com.example.nelli.posters2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nelli.posters2.requests.EventsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EventFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private EventsResponse eventsResponse;
    @BindView(R.id.title)
    TextView  title;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.cost)
    TextView cost;
    private GoogleMap googleMap;
    private Unbinder unbind;

    public EventFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        unbind = ButterKnife.bind(this, rootView);

        if (this.eventsResponse != null) {
            setExistingParams();
        }

        return rootView;
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

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap lGoogleMap) {
        this.googleMap = lGoogleMap;
        if (eventsResponse == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(68.965448, 33.081779), 15));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(eventsResponse.latitude, eventsResponse.longitude), 15));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(eventsResponse.latitude, eventsResponse.longitude)).title("Place is here"));
        }
    }

    public void setAdapter(EventsResponse event) {
        this.eventsResponse = event;
    }

    public void setExistingParams() {
        title.setText(this.eventsResponse.title);
        info.setText(this.eventsResponse.info);
        cost.setText(String.valueOf(this.eventsResponse.cost_of_entry));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.close)
    void closeFragment() {
        mListener.closeEventFragment();
    }

    public interface OnFragmentInteractionListener {
        void closeEventFragment();
    }
}
