package com.example.nelli.posters2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.nelli.posters2.infrastructures.EditableInfrastructuresAdapter;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.InfrastructuresResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.example.nelli.posters2.CreateEventFragment.getRealPathFromUri;


public class CreateInfrastructureFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.screenView)
    ScrollView scrollView;
    @BindView(R.id.title)
    EditText infrastructureTitle;
    @BindView(R.id.info)
    EditText infrastructureInfo;
    @BindView(R.id.place)
    EditText infrastructurePlace;
    @BindView(R.id.transparent_image)
    ImageView transparentImage;
    @BindView(R.id.image_view)
    ImageView infrastructureImageView;
    @BindView(R.id.save_btn)
    Button saveBtn;
    private Marker currentMarker;
    private OnFragmentInteractionListener mListener;
    private Unbinder unbind;
    private Uri imageUri;
    private EditableInfrastructuresAdapter infrastructureAdapter;
    private GoogleMap googleMap;
    private InfrastructuresResponse infrastructureResponse;

    public CreateInfrastructureFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_infrastructure, container, false);
        unbind = ButterKnife.bind(this, rootView);

        transparentImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        if (this.infrastructureResponse != null) {
            setExistingParams();
        }

        return rootView;
    }

    public void setAdapter(EditableInfrastructuresAdapter adapter) {
        this.infrastructureAdapter = adapter;
    }

    public void setAdapter(EditableInfrastructuresAdapter adapter, InfrastructuresResponse event) {
        this.infrastructureAdapter = adapter;
        this.infrastructureResponse = event;
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
        if (infrastructureResponse == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(68.965448, 33.081779), 15));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(infrastructureResponse.latitude, infrastructureResponse.longitude), 15));
            currentMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(infrastructureResponse.latitude, infrastructureResponse.longitude)).title("Place is here"));
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (currentMarker != null) {
                    currentMarker.setPosition(latLng);
                } else {
                    currentMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Place is here"));
                }
            }
        });
    }

    @OnClick({R.id.image, R.id.save_btn, R.id.close_canvas_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
                break;
            case R.id.save_btn:
                if (infrastructureResponse != null) {
                    updateEvent();
                } else {
                    saveInfrastructure();
                }
                break;
            case R.id.close_canvas_btn:
                mListener.closeCreateInfrastructureFragment();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            default:
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                        infrastructureImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {}
                }
        }
    }

    private void updateEvent() {
        Observable connection;
        if (imageUri != null) {
            connection = new BackendConnection(getContext()).updateInfrastructure(
                    String.valueOf(infrastructureResponse.id),
                    String.valueOf(infrastructureTitle.getText()),
                    String.valueOf(infrastructureInfo.getText()),
                    String.valueOf(infrastructurePlace.getText()),
                    String.valueOf(currentMarker.getPosition().latitude),
                    String.valueOf(currentMarker.getPosition().longitude),
                    new File(getRealPathFromUri(getContext(), imageUri))
            );
        } else {
            connection = new BackendConnection(getContext()).updateInfrastructure(
                    String.valueOf(infrastructureResponse.id),
                    String.valueOf(infrastructureTitle.getText()),
                    String.valueOf(infrastructureInfo.getText()),
                    String.valueOf(infrastructurePlace.getText()),
                    String.valueOf(currentMarker.getPosition().latitude),
                    String.valueOf(currentMarker.getPosition().longitude)
            );
        }
        connection.subscribe(new Observer<Response<InfrastructuresResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<InfrastructuresResponse> response) {
                if (response.code() == 200) {
                    infrastructureAdapter.setById(response.body());
                    mListener.closeCreateInfrastructureFragment();
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    private void saveInfrastructure() {
        new BackendConnection(getContext()).postInfrastructure(
                String.valueOf(infrastructureTitle.getText()),
                String.valueOf(infrastructureInfo.getText()),
                String.valueOf(infrastructurePlace.getText()),
                String.valueOf(currentMarker.getPosition().latitude),
                String.valueOf(currentMarker.getPosition().longitude),
                new File(getRealPathFromUri(getContext(), imageUri))
        ).subscribe(new Observer<Response<InfrastructuresResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<InfrastructuresResponse> response) {
                if (response.code() == 201) {
                    infrastructureAdapter.addItem(response.body());
                    mListener.closeCreateInfrastructureFragment();
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    public void setExistingParams() {
        infrastructureTitle.setText(this.infrastructureResponse.title);
        infrastructureInfo.setText(this.infrastructureResponse.info);
        infrastructurePlace.setText(this.infrastructureResponse.place);
        saveBtn.setText("Сохранить");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void closeCreateInfrastructureFragment();
    }
}
