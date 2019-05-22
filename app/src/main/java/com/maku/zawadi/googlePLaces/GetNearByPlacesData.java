package com.maku.zawadi.googlePLaces;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.maku.zawadi.MainActivity;

public class GetNearByPlacesData extends AsyncTask<Object, String, String> {

    public  static  final String TAG = GetNearByPlacesData.class.getSimpleName();

    //vars
    String googlePlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: onPostExecute");
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(Object... objects) {
        Log.d(TAG, "doInBackground: do in background");
        return null;
    }
}
