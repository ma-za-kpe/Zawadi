package com.maku.zawadi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.parceler.Parcels;


import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.model.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MenuDetailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.restaurantNameTextView)
    TextView mNameLabel;
    @BindView(R.id.ratingTextView) TextView mRatingLabel;

    private Restaurant mRestaurant;

    public static MenuDetailFragment newInstance(Restaurant restaurant) {
        MenuDetailFragment menuDetailFragment = new MenuDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("results", Parcels.wrap(restaurant));
        menuDetailFragment.setArguments(args);
        return menuDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestaurant = Parcels.unwrap(getArguments().getParcelable("results"));
    }

    public MenuDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu_detail, container, false);;
        ButterKnife.bind(this, view);

        mNameLabel.setText(mRestaurant.getName());
        mRatingLabel.setText(Double.toString(mRestaurant.getRating()) + "/5");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
