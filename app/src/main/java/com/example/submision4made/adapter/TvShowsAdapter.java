package com.example.submision4made.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.submision4made.activity.DetailActivity;
import com.example.submision4made.R;
import com.example.submision4made.fragment.TvShowFragment;
import com.example.submision4made.model.TvShow;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder> {
    private static final String TAG = "TvShowsAdapter";
    private TvShowFragment context;
    private final ArrayList<TvShow> mData = new ArrayList<>();

    public TvShowsAdapter(TvShowFragment context) {
        this.context = context;
    }

    public void setData(ArrayList<TvShow> items) {
        Log.d(TAG, "setData: running");
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowsAdapter.TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemGrid = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_tvshow, parent, false);
        return new TvShowViewHolder(itemGrid);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: running");
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: running");
        return mData.size();
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "TvShowViewHolder";
        ImageView imgPhoto;

        public TvShowViewHolder(View itemGrid) {
            super(itemGrid);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: running");

            int position = getAdapterPosition();

            TvShow tvShow = mData.get(position);

            tvShow.setId(tvShow.getId());
            tvShow.setTitle(tvShow.getTitle());
            tvShow.setOverview(tvShow.getOverview());
            tvShow.setPhoto(tvShow.getPhoto());
            tvShow.setAverage(tvShow.getAverage());
            tvShow.setYear(tvShow.getYear());

            Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.TVSHOW_INDEX, tvShow);
            itemView.getContext().startActivity(intent);
        }

        public void bind(TvShow tvShow) {
            Log.d(TAG, "bind: running");
            String url_photo = tvShow.getPhoto();

            Glide.with(itemView.getContext())
                    .load(url_photo)
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
        }
    }
}
