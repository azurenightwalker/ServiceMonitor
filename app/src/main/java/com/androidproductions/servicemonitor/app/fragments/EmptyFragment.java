package com.androidproductions.servicemonitor.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidproductions.servicemonitor.app.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.androidproductions.servicemonitor.app.fragments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.androidproductions.servicemonitor.app.fragments.EmptyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EmptyFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmptyFragment.
     */
    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }
    public EmptyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empty, container, false);
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
}
