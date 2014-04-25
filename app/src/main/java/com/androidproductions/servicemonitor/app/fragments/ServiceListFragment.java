package com.androidproductions.servicemonitor.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;

import com.androidproductions.servicemonitor.app.data.ServiceStatusAdapter;
import com.androidproductions.servicemonitor.app.data.ServiceStatusContract;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.androidproductions.servicemonitor.app.fragments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.androidproductions.servicemonitor.app.fragments.ServiceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ServiceListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private OnFragmentInteractionListener mListener;
    private CursorAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the pDrugCursorAdapterrovided parameters.
     *
     * @return A new instance of fragment EmptyFragment.
     */
    public static ServiceListFragment newInstance() {
        return new ServiceListFragment();
    }
    public ServiceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ServiceStatusAdapter(getActivity());
        setListAdapter(mAdapter);
        this.getLoaderManager().initLoader(1, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(final int i, final Bundle bundle) {
        return new CursorLoader(getActivity(),ServiceStatusContract.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
        if(mAdapter!=null && cursor!=null)
        mAdapter.swapCursor(cursor);
        else
            Log.v("SM", "OnLoadFinished: mAdapter is null");
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> cursorLoader) {
        if(mAdapter!=null)
            mAdapter.swapCursor(null);
        else
            Log.v("SM","OnLoadFinished: mAdapter is null");
    }
}
