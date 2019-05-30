package com.maku.zawadi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maku.zawadi.POJOModels.Result;
import com.maku.zawadi.adapter.MenuAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MenuFragment extends Fragment {

    TextView mNameLabel;
    TextView mRatingLabel;

    //Arraylist of categories
    ArrayList<String> mMenu;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Context context;

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    private Result mResult;
// is used instead of a constructor and returns a new instance of our MenuFragment
    public static MenuFragment newInstance(Result result) {
        MenuFragment menuFragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putParcelable("restaurant", Parcels.wrap(result));
        menuFragment.setArguments(args);
        return menuFragment;
    }

    //is called when the fragment is created. Here, we unwrap our Result object from the arguments we added in the newInstance() method.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResult = Parcels.unwrap(getArguments().getParcelable("restaurant"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        ButterKnife.bind(this, container);

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        mNameLabel = v.findViewById(R.id.NameTextView);
        mRatingLabel = v.findViewById(R.id.ratingTextView);
        recyclerView = v.findViewById(R.id.menuRecycler);

        mNameLabel.setText(mResult.getName());
        mRatingLabel.setText(mResult.getRating() + "/5");

        //ArrayList
        mMenu = new ArrayList<String>();
        mMenu.add("omlette");
        mMenu.add("cappucino");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");
        mMenu.add("pancakes");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter = new MenuAdapter(mMenu);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        // GoogleSignInOptions 개체 구성

        // Inflate the layout for this fragment
        return v;
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

    public void setInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }
}
