package com.rygital.vktestapp.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.rygital.vktestapp.R;
import com.rygital.vktestapp.App;
import com.rygital.vktestapp.data.models.friends.Friend;
import com.rygital.vktestapp.data.models.user.User;
import com.rygital.vktestapp.data.models.user.UserParcelable;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity implements UserView {
    @BindView(R.id.tvProfilerName) TextView tvProfilerName;
    @BindView(R.id.tvSex) TextView tvSex;
    @BindView(R.id.rvFriends) RecyclerView rvFriends;

    @Inject
    UserPresenter presenter;
    @Inject
    UserAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().injectUserActivity(this);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        presenter.attachView(this);

        init();
        if (user != null) presenter.loadFriends(user);
        presenter.onCreate(savedInstanceState);
    }

    private void init() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFriends.setLayoutManager(llm);
        rvFriends.addItemDecoration(new DividerItemDecoration(rvFriends.getContext(), llm.getOrientation()));
        rvFriends.setHasFixedSize(true);
        rvFriends.setAdapter(adapter);

        Intent intent = getIntent();
        if(intent != null) {
            UserParcelable userParcelable = intent.getParcelableExtra(UserParcelable.class.getCanonicalName());
            if (userParcelable != null) {
                user = userParcelable.getUser();
            }
        }

        if (user != null) {
            tvProfilerName.setText(user.getFullName());
            switch (user.getSex()){
                case 1: tvSex.setText(R.string.female);
                case 2: tvSex.setText(R.string.male);
                default: tvSex.setText(R.string.not_specified);
            }
        }
    }

    @Override
    public void showFriends(List<Friend> list) {
        adapter.setData(list);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }
}
