package com.example.nelli.posters2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.nelli.posters2.events.EditableEventsAdapter;
import com.example.nelli.posters2.requests.BackendConnection;
import com.example.nelli.posters2.requests.EventsResponse;
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


public class CreateEventFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.screenView)
    ScrollView scrollView;
    @BindView(R.id.event_title)
    EditText eventTitle;
    @BindView(R.id.event_info)
    EditText eventInfo;
    @BindView(R.id.event_place)
    EditText eventPlace;
    @BindView(R.id.transparent_image)
    ImageView transparentImage;
    @BindView(R.id.event_image_view)
    ImageView eventImageView;
    @BindView(R.id.event_cost)
    EditText eventCost;
    @BindView(R.id.event_date)
    Button eventDate;
    @BindView(R.id.save_event_btn)
    Button saveEventBtn;
    private Marker currentMarker;
    private OnFragmentInteractionListener mListener;
    private Unbinder unbind;
    private Uri imageUri;
    private EditableEventsAdapter eventsAdapter;
    private GoogleMap googleMap;
    private EventsResponse eventsResponse;

    public CreateEventFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
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

        if (this.eventsResponse != null) {
            setExistingParams();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            currentMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(eventsResponse.latitude, eventsResponse.longitude)).title("Place is here"));
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

    public void setAdapter(EditableEventsAdapter adapter) {
        this.eventsAdapter = adapter;
    }

    public void setAdapter(EditableEventsAdapter adapter, EventsResponse event) {
        this.eventsAdapter = adapter;
        this.eventsResponse = event;
    }

    public void setExistingParams() {
        eventTitle.setText(this.eventsResponse.title);
        eventInfo.setText(this.eventsResponse.info);
        eventDate.setText(this.eventsResponse.date);
        eventPlace.setText(this.eventsResponse.place);
        eventCost.setText(String.valueOf(this.eventsResponse.cost_of_entry));
        saveEventBtn.setText("Сохранить");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                        eventImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {}
                }
        }
    }

    public void setDate(String dateString) {
        eventDate.setText(dateString);
    }

    private void updateEvent() {
        Observable connection;
        if (imageUri != null) {
            connection = new BackendConnection(getContext()).updateEvent(
                    String.valueOf(eventsResponse.id),
                    String.valueOf(eventTitle.getText()),
                    String.valueOf(eventInfo.getText()),
                    String.valueOf(eventPlace.getText()),
                    String.valueOf(eventDate.getText()),
                    String.valueOf(eventCost.getText()),
                    String.valueOf(currentMarker.getPosition().latitude),
                    String.valueOf(currentMarker.getPosition().longitude),
                    new File(getRealPathFromUri(getContext(), imageUri))
            );
        } else {
            connection = new BackendConnection(getContext()).updateEvent(
                    String.valueOf(eventsResponse.id),
                    String.valueOf(eventTitle.getText()),
                    String.valueOf(eventInfo.getText()),
                    String.valueOf(eventPlace.getText()),
                    String.valueOf(eventDate.getText()),
                    String.valueOf(eventCost.getText()),
                    String.valueOf(currentMarker.getPosition().latitude),
                    String.valueOf(currentMarker.getPosition().longitude)
            );
        }
        connection.subscribe(new Observer<Response<EventsResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<EventsResponse> response) {
                if (response.code() == 200) {
                    eventsAdapter.setById(response.body());
                    mListener.closeCreateEventFragment();
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    private void saveEvent() {
        new BackendConnection(getContext()).postEvent(
                String.valueOf(eventTitle.getText()),
                String.valueOf(eventInfo.getText()),
                String.valueOf(eventPlace.getText()),
                String.valueOf(eventDate.getText()),
                String.valueOf(eventCost.getText()),
                String.valueOf(currentMarker.getPosition().latitude),
                String.valueOf(currentMarker.getPosition().longitude),
                new File(getRealPathFromUri(getContext(), imageUri))
        ).subscribe(new Observer<Response<EventsResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<EventsResponse> response) {
                if (response.code() == 201) {
                    eventsAdapter.addItem(response.body());
                    mListener.closeCreateEventFragment();
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    @OnClick({R.id.event_date, R.id.event_image, R.id.save_event_btn, R.id.close_event_canvas_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.event_date:
                mListener.startDatePicker();
                break;
            case R.id.event_image:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
                break;
            case R.id.save_event_btn:
                if (eventsResponse != null) {
                    updateEvent();
                } else {
                    saveEvent();
                }
                break;
            case R.id.close_event_canvas_btn:
                mListener.closeCreateEventFragment();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void closeCreateEventFragment();
        void startDatePicker();
    }

    public static String getRealPathFromUri(Context ctx, Uri uri) {
        String[] filePathColumn = { MediaStore.Files.FileColumns.DATA };
        String picturePath = "";

        Cursor cursor = ctx.getContentResolver().query(uri, filePathColumn,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.e("", "picturePath : " + picturePath);
            cursor.close();
        }
        return picturePath;
    }
}
