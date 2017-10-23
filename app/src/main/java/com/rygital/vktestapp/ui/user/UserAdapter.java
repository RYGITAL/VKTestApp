package com.rygital.vktestapp.ui.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rygital.vktestapp.R;
import com.rygital.vktestapp.data.models.friends.Friend;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.FriendsViewHolder> {
    private List<Friend> data;
    private Context context;

    @Inject
    UserAdapter(Context context) {
        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Friend> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto) ImageView ivPhoto;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvFriends) TextView tvFriends;

        FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Friend user) {
            tvName.setText(user.getFullName());
            tvFriends.setText("");

            Glide.with(context)
                    .load(user.getPhoto50())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .into(ivPhoto);
        }
    }
}
