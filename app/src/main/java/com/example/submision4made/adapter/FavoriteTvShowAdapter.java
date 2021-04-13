package com.example.submision4made.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.submision4made.model.TvShow;

import java.util.ArrayList;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.RecycleViewHolder> {

    private ArrayList<TvShow> tvShows = new ArrayList<>();
    private Context context;

    public FavoriteTvShowAdapter() {
    }

    public FavoriteTvShowAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<TvShow> items) {
        tvShows.clear();
        tvShows.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<TvShow> getMovies() {
        return tvShows;
    }

    public void setTvShows(ArrayList<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new FavoriteTvShowAdapter.RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, final int i) {
        holder.tvTitle.setText(tvShows.get(i).getTitle());
        holder.tvOverview.setText(tvShows.get(i).getOverview());

        String url_photo = tvShows.get(i).getPhoto();
        Glide.with(context).load(url_photo)
                .into(holder.imgPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.TVSHOW_INDEX, getMovies().get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView imgPhoto;

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvOverview = itemView.findViewById(R.id.tv_item_overview);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
        }
    }
}
