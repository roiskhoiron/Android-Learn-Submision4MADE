package com.example.submision4made.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.submision4made.activity.DetailActivity;
import com.example.submision4made.R;
import com.example.submision4made.model.Movies;

import java.util.ArrayList;

public class FilteredMoviesAdapter extends RecyclerView.Adapter<FilteredMoviesAdapter.RecycleViewHolder> {
    private static final String TAG = "FilteredMoviesAdapter";
    private ArrayList<Movies> movies;
    private Context context;

    public ArrayList<Movies> getMovies() {
        Log.d(TAG, "getMovies: running");
        return movies;
    }

    public void setMovies(ArrayList<Movies> movies) {
        Log.d(TAG, "setMovies: running");
        this.movies = movies;
    }

    public FilteredMoviesAdapter(Context context) {
        Log.d(TAG, "FilteredMoviesAdapter: running");
        this.context = context;
    }

    public void setData(ArrayList<Movies> items) {
        Log.d(TAG, "setData: running");
        movies.clear();
        movies.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilteredMoviesAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: running");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new FilteredMoviesAdapter.RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilteredMoviesAdapter.RecycleViewHolder recycleViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: running");
        recycleViewHolder.tvTitle.setText(movies.get(i).getTitle());
        recycleViewHolder.tvOverview.setText(movies.get(i).getOverview());
        Glide.with(context).load(movies.get(i).getPhoto())
                .into(recycleViewHolder.imgPhoto);

        recycleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_INDEX, getMovies().get(recycleViewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: running");
        return movies.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "RecycleViewHolder";
        TextView tvTitle;
        TextView tvOverview;
        ImageView imgPhoto;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "RecycleViewHolder: running");
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvOverview = itemView.findViewById(R.id.tv_item_overview);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
        }
    }
}
