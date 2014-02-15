package ngj.app.likeassassinscreedmultiplayer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements 
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient locationClient;
    private Location currentLocation;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the connection client
        locationClient = new LocationClient(this, this, this);
    }

	protected void onStart() {
		super.onStart();
		// Connect the location client
		if(isGooglePlayAvailable()) locationClient.connect();
		else Toast.makeText(this, "Google Play not available. Please try again later", Toast.LENGTH_SHORT).show();
	}
	
	protected void onStop() {
		// Disconnect location client
		locationClient.disconnect();
		super.onStop();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    protected void onActivityResult(
    		int requestCode, int resultCode, Intent data){
    	// Decide what to do based on request code
    	switch(requestCode)
    	{
    		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
    		switch(resultCode)
    		{
    			case Activity.RESULT_OK :
    				/* 
    				 * Try the request again
    				 */
    		}
    		break;
    	}
    }
    
    public boolean isGooglePlayAvailable()
    {
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	if(resultCode == ConnectionResult.SUCCESS)
    	{
    		Log.d("Location updates", "Google Play was available");
    		return true;
    	} else {
    		// int errorCode = ConnectionResult.
    		Log.d("Location updates", "Google Play was not available");
    		return false;
    	}
    }


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("Google Play", "Connection failed");
		if(result.hasResolution())
		{
			try {
				result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, "Connection failed for unknown reason.", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onConnected(Bundle connectionHint) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		currentLocation = locationClient.getLastLocation();
		TextView tw = (TextView) findViewById(R.id.textView1);
		tw.setText("Lat:" + currentLocation.getLatitude() + ", long:" + currentLocation.getLongitude());
	}


	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}
}
