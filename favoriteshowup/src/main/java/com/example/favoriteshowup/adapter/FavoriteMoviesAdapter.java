package com.example.favoriteshowup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoriteshowup.R;
import com.example.favoriteshowup.model.Movies;

import java.util.ArrayList;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.RecycleViewHolder> {

    private ArrayList<Movies> movies = new ArrayList<>();
    private Context context;

    public FavoriteMoviesAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Movies> items) {
        movies.clear();
        movies.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<Movies> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movies> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, final int i) {
        holder.tvTitle.setText(movies.get(i).getTitle());
        holder.tvOverview.setText(movies.get(i).getOverview());

        String url_photo = movies.get(i).getPhoto();
        Glide.with(context).load(url_photo)
                .into(holder.imgPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Movie " + movies.get(i).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
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
