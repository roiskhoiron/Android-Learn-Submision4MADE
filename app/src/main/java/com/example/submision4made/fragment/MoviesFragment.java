package com.example.submision4made.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submision4made.R;
import com.example.submision4made.adapter.MoviesAdapter;
import com.example.submision4made.model.Movies;
import com.example.submision4made.viewModel.MoviesViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = "MoviesFragment";

    private RecyclerView rvMovies;
    private ArrayList<Movies> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private ProgressDialog progressDialog;
    private MoviesViewModel moviesViewModel;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: running");
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_text));

        if (movies.size() <= 0) {
            showLoading(true);
        } else {
            showLoading(false);
        }

        rvMovies = view.findViewById(R.id.rv_category);
        rvMovies.setLayoutManager(new LinearLayoutManager(this.getContext()));

        moviesAdapter = new MoviesAdapter(this);
        moviesAdapter.notifyDataSetChanged();

        rvMovies.setAdapter(moviesAdapter);


        moviesViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MoviesViewModel.class);

        moviesViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movies>>() {

            @Override
            public void onChanged(ArrayList<Movies> movies) {
                Log.d(TAG, "onChanged: running");
                if (movies != null) {
                    moviesAdapter.setData(movies);
                    showLoading(false);
                }
            }
        });

        moviesViewModel.setMovies();
    }

    private void showLoading(Boolean state) {
        if (state) {
            Log.d(TAG, "showLoading: on");
            progressDialog.show();
        } else {
            Log.d(TAG, "showLoading: off");
            progressDialog.dismiss();
        }
    }
}
