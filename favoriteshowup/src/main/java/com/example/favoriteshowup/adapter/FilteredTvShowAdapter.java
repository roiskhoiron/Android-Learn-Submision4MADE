package com.example.favoriteshowup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoriteshowup.R;
import com.example.favoriteshowup.model.TvShow;

import java.util.ArrayList;

public class FilteredTvShowAdapter extends RecyclerView.Adapter<FilteredTvShowAdapter.RecycleViewHolder>  {
    private static final String TAG = "FilteredTvShowAdapter";
    private ArrayList<TvShow> tvShows;
    private Context context;

    public ArrayList<TvShow> getTvShows() {
        Log.d(TAG, "getTvShows: running");
        return tvShows;
    }

    public void setTvShows(ArrayList<TvShow> tvShows) {
        Log.d(TAG, "setTvShows: running");
        this.tvShows = tvShows;
    }

    public FilteredTvShowAdapter(Context context) {
        Log.d(TAG, "FilteredTvShowAdapter: running");
        this.context = context;
    }

    public void setData(ArrayList<TvShow> items) {
        Log.d(TAG, "setData: running");
        tvShows.clear();
        tvShows.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilteredTvShowAdapter.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: running");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new FilteredTvShowAdapter.RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilteredTvShowAdapter.RecycleViewHolder recycleViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: running");
        recycleViewHolder.tvTitle.setText(tvShows.get(i).getTitle());
        recycleViewHolder.tvOverview.setText(tvShows.get(i).getOverview());
        Glide.with(context).load(tvShows.get(i).getPhoto())
                .into(recycleViewHolder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: running");
        return tvShows.size();
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
