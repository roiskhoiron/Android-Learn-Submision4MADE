package com.example.submision4made.adapter;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.submision4made.activity.DetailActivity;
import com.example.submision4made.R;
import com.example.submision4made.fragment.MoviesFragment;
import com.example.submision4made.model.Movies;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private static final String TAG = "MoviesAdapter";
    private MoviesFragment context;
    private final ArrayList<Movies> mData = new ArrayList<>();

    public MoviesAdapter(MoviesFragment context) {
        this.context = context;
    }

    public void setData(ArrayList<Movies> items) {
        Log.d(TAG, "setData: running");
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: running");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_film, parent, false);
        return new MoviesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: running");
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: running");
        return mData.size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "MoviesViewHolder";
        TextView tvTitle;
        TextView tvOverview;
        ImageView imgPhoto;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MoviesViewHolder: running");
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvOverview = itemView.findViewById(R.id.tv_item_overview);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: running");

            int position = getAdapterPosition();

            Movies movie = mData.get(position);

            movie.setId(movie.getId());
            movie.setTitle(movie.getTitle());
            movie.setOverview(movie.getOverview());
            movie.setPhoto(movie.getPhoto());
            movie.setAverage(movie.getAverage());
            movie.setYear(movie.getYear());

            Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.MOVIE_INDEX, movie);
            itemView.getContext().startActivity(intent);

        }

        public void bind(Movies movies) {
            Log.d(TAG, "bind: running");
            String url_photo = movies.getPhoto();

            tvTitle.setText(movies.getTitle());
            tvOverview.setText(movies.getOverview());

            Glide.with(itemView.getContext())
                    .load(url_photo)
                    .apply(new RequestOptions().override(55, 55))
                    .into(imgPhoto);
        }
    }
}
