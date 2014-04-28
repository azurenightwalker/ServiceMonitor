package com.androidproductions.servicemonitor.app;

import android.app.Activity;
import android.os.Bundle;

import com.androidproductions.servicemonitor.app.fragments.OnFragmentInteractionListener;
import com.androidproductions.servicemonitor.app.fragments.ServiceDetailsFragment;


public class DetailsActivity extends Activity implements OnFragmentInteractionListener{

    public static final String ServiceKey = "SERVICEKEY";
    private String _id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null)
            _id = getIntent().getExtras().getString(ServiceKey);
        super.onCreate(savedInstanceState);
        ServiceDetailsFragment.newInstance(_id).show(getFragmentManager(),"DETAILSACT");
    }

    @Override
    public void CloseFragment() {
        finish();
    }
}
