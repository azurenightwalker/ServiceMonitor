package com.androidproductions.servicemonitor.app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidproductions.generic.lib.auth.GoogleCredentials;
import com.androidproductions.generic.lib.dates.DateFormatter;
import com.androidproductions.servicemonitor.app.R;
import com.androidproductions.servicemonitor.app.data.services.ServiceState;
import com.androidproductions.servicemonitor.app.data.services.ServiceStateHelper;
import com.androidproductions.servicemonitor.app.data.services.ServiceStatus;

import java.util.Date;
import java.util.concurrent.Callable;

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
    private String _name;
    private Button claimView;
    private Button updateView;

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

    public static ServiceDetailsFragment newInstance(String serviceName) {
        Bundle args = new Bundle();
        args.putString("name", serviceName);
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
        claimView = (Button) view.findViewById(R.id.claimIssue);
        claimView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        claimIssue();
                    }
                }
        );
        updateView = (Button) view.findViewById(R.id.updateStatus);
        updateView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateStatus();
                    }
                }
        );
        if (getArguments() != null) {
            _id = getArguments().getLong("id", 0);
            _name = getArguments().getString("name", "");
        }
        if (_id > 0)
            _data = ServiceStateHelper.getServiceStatus(getActivity(),_id);
        else
            _data = ServiceStateHelper.getServiceStatus(getActivity(),_name);
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
        TextView sName = (TextView)view.findViewById(R.id.service_name);
        sName.setText(_data.getName());
        ((TextView)view.findViewById(R.id.service_group)).setText(_data.getGroup());
        TextView tv = (TextView) view.findViewById(R.id.service_claimant);
        if (_data.getClaimant() != null) {
            tv.setText(_data.getClaimant());
            tv.setVisibility(View.VISIBLE);
        }
        else
            tv.setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.service_update)).setText(
                DateFormatter.AsCuiDateTime(new Date(_data.getLastUpdate())));
        sName.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(_data.getStatus().getResource()), null, null, null);

        if (_data.getStatus() == ServiceState.DOWN) {
            if (_data.getClaimant() != null && _data.getClaimant().equals(GoogleCredentials.Instance.getAccount().getSelectedAccountName())) {
                claimView.setText(R.string.releaseIssue);
                claimView.setOnClickListener(
                    new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                releaseIssue();
                            }
                        }
                );
            }
            else {
                claimView.setText(R.string.claimIssue);
                claimView.setOnClickListener(
                    new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                claimIssue();
                            }
                        }
                );
            }
            claimView.setEnabled(true);
            claimView.setVisibility(View.VISIBLE);
        }
        else
            claimView.setVisibility(View.GONE);
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
        updateView.setText(R.string.updating);
        updateView.setEnabled(false);
        ServiceStateHelper.refreshServiceState(getActivity(), _data.getId(), new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                _data = ServiceStateHelper.getServiceStatus(getActivity(),_id);
                refreshView(getView());
                updateView.setText(R.string.updateStatus);
                updateView.setEnabled(true);
                return null;
            }
        });
    }

    private void claimIssue()
    {
        claimView.setText(R.string.releaseIssue);
        ServiceStateHelper.claimServiceState(getActivity(),_data.getId());
    }

    private void releaseIssue()
    {
        claimView.setText(R.string.claimIssue);
        ServiceStateHelper.releaseServiceState(getActivity(), _data.getId());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.CloseFragment();
    }
}
