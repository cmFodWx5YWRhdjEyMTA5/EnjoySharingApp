package enjoysharing.enjoysharing.Activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;
import enjoysharing.enjoysharing.Business.BusinessBase;
import enjoysharing.enjoysharing.R;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    protected AutocompleteSupportFragment autocompleteFragment;
    protected String mPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(MapsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        business = new BusinessBase(MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        initMapComponent();
    }

    protected void initMapComponent()
    {
        if(CheckForPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},102,MapsActivity.this))
        {
            autocompleteFragment.setOnPlaceSelectedListener(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //boolean allPermitted = true;
        switch (requestCode) {
            case 102:  // Request for maps
                initMapComponent();
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onBackPressed() {
        super.StandardOnBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        mMap.addMarker(new MarkerOptions().position(cameraPosition.target).title(mPlaceName));
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        if (mMap == null) {
            return;
        }
        mPlaceName = place.getName().toString();

        final LatLng latLng = place.getLatLng();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
    }

    @Override
    public void onError(@NonNull Status status) {
        String statusMessage = status.getStatusMessage();
    }
}
