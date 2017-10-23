package com.rygital.vktestapp.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rygital.vktestapp.R;
import com.rygital.vktestapp.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.FriendsViewHolder> {
    private List<User> data;
    private MainPresenter presenter;
    private Context context;

    @Inject
    MainAdapter(MainPresenter presenter, Context context) {
        data = new ArrayList<>();
        this.presenter = presenter;
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

        holder.llItem.setTag(position);
        holder.llItem.setOnClickListener(view -> {
            int position1 = (Integer) view.getTag();
            presenter.startUserActivity(data.get(position1));

        });
        holder.llItem.setOnLongClickListener(view -> {
            int position12 = (Integer) view.getTag();
            presenter.deleteItem(data.get(position12), position12);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    void insertItem(User item) {
        if (item != null) {
            data.add(item);
            notifyItemInserted(getItemCount());
        }
    }

    void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llItem) LinearLayout llItem;
        @BindView(R.id.ivPhoto) ImageView ivPhoto;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvFriends) TextView tvFriends;

        FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(User user) {
            tvName.setText(user.getFullName());
            tvFriends.setText(context.getString(R.string.friends_amount) + " " + user.getCounters().getFriends());

            Glide.with(context)
                    .load(user.getPhoto50())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .skipMemoryCache(true)
                    .into(ivPhoto);
        }
    }
}
