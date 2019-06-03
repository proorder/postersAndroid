package com.example.nelli.posters2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nelli.posters2.requests.InfrastructuresResponse;
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


public class InfrastructureFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private InfrastructuresResponse response;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info)
    TextView info;
    private GoogleMap googleMap;
    private Unbinder unbind;

    public InfrastructureFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_infrastructure, container, false);

        unbind = ButterKnife.bind(this, rootView);

        if (this.response != null) {
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
        if (response == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(68.965448, 33.081779), 15));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(response.latitude, response.longitude), 15));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(response.latitude, response.longitude)).title("Place is here"));
        }
    }

    public void setAdapter(InfrastructuresResponse infra) {
        this.response = infra;
    }

    public void setExistingParams() {
        title.setText(this.response.title);
        info.setText(this.response.info);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.close)
    void closeFragment() {
        mListener.closeInfrastructureFragment();
    }

    public interface OnFragmentInteractionListener {
        void closeInfrastructureFragment();
    }
}
