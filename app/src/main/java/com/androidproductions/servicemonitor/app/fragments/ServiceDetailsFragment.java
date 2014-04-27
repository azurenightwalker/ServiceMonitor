package com.androidproductions.servicemonitor.app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidproductions.generic.lib.dates.DateFormatter;
import com.androidproductions.servicemonitor.app.R;
import com.androidproductions.servicemonitor.app.data.ServiceStateHelper;
import com.androidproductions.servicemonitor.app.data.ServiceStatus;

import java.util.Date;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.androidproductions.servicemonitor.app.fragments.ServiceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ServiceDetailsFragment extends DialogFragment {
    private OnFragmentInteractionListener mListener;
    private long _id;
    private ServiceStatus _data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceDetailsFragment.
     */
    public static ServiceDetailsFragment newInstance(long serviceId) {
        Bundle args = new Bundle();
        args.putLong("id", serviceId);
        ServiceDetailsFragment frag = new ServiceDetailsFragment();
        frag.setArguments(args);
        return frag;
    }
    public ServiceDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.service_details, container, false);
        view.findViewById(R.id.claimIssue).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        claimIssue();
                    }
                }
        );
        view.findViewById(R.id.updateStatus).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatus();
                    }
                }
        );
        if (getArguments() != null)
            _id = getArguments().getLong("id");
        _data = ServiceStateHelper.getServiceStatus(getActivity(),_id);
        refreshView(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void refreshView(View view)
    {
        ((TextView)view.findViewById(R.id.service_name)).setText(_data.getName());
        ((TextView)view.findViewById(R.id.service_group)).setText(_data.getGroup());
        ((TextView)view.findViewById(R.id.service_update)).setText(
                DateFormatter.AsCuiDateTime(new Date(_data.getLastUpdate())));
        ((ImageView) view.findViewById(R.id.service_status))
                .setImageResource(_data.getStatus().getResource());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void updateStatus()
    {

    }

    private void claimIssue()
    {

    }

    private void releaseIssue()
    {

    }
}
