package com.app.nb.minitwitter.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.app.nb.minitwitter.R;
import com.app.nb.minitwitter.common.Constants;
import com.app.nb.minitwitter.common.SharedPreferencesManager;
import com.app.nb.minitwitter.data.TweetViewModel;
import com.app.nb.minitwitter.retrofit.response.Like;
import com.app.nb.minitwitter.retrofit.response.Tweet;
import com.bumptech.glide.Glide;

import java.util.List;

public class TweetRecyclerViewAdapter extends RecyclerView.Adapter<TweetRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> mValues;
    private String username;

    private TweetViewModel tweetViewModel;

    public TweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        this.context = context;
        //Obtiene el nombre usado almcenado en shared preferences
        username = SharedPreferencesManager.getStringValue(Constants.PREF_USERNAME);
        tweetViewModel = ViewModelProviders.of((FragmentActivity) context).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues != null) { //Solo se carga si la lista existe
            holder.mItem = mValues.get(position);

            holder.tvUsername.setText("@" + holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikeCount.setText(holder.mItem.getLikes().size() + "");

            String photoUrl = holder.mItem.getUser().getPhotoUrl();
            if (!photoUrl.equals("")) {
                //Cargando imagen con Glide
                Glide.with(context)
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photoUrl)
                        .into(holder.ivAvatar);
            }

            Glide.with(context)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            // Cambia el color del contador de likes
            holder.tvLikeCount.setTextColor(context.getResources().getColor(R.color.colorAzulDark));
            holder.tvLikeCount.setTypeface(null, Typeface.NORMAL);

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Invoca metodo like del ViewModel
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            for (Like like : holder.mItem.getLikes()) {
                if (like.getUsername().equals(username)) {
                    Glide.with(context)
                            .load(R.drawable.ic_like_purple)
                            .into(holder.ivLike);
                    // Cambia el color del contador de likes
                    holder.tvLikeCount.setTextColor(context.getResources().getColor(R.color.purple));
                    holder.tvLikeCount.setTypeface(null, Typeface.BOLD);
                    break;
                }
            }
        }
    }

    public void setData(List<Tweet> tweetList) {
        this.mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mValues != null) // Devuelve cero en caso la lista no exista
            return mValues.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikeCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikeCount = view.findViewById(R.id.textViewLike);
        }
    }
}
