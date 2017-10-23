package com.rygital.vktestapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.rygital.vktestapp.R;
import com.rygital.vktestapp.App;
import com.rygital.vktestapp.data.models.user.User;
import com.rygital.vktestapp.data.models.user.UserParcelable;
import com.rygital.vktestapp.ui.user.UserActivity;
import com.rygital.vktestapp.utils.NetworkUtils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements MainView {
    @BindView(R.id.swipeLayout) SwipeRefreshLayout swipeLayout;
    @BindView(R.id.rvData) RecyclerView rvData;
    @BindView(R.id.etUserId) EditText etUserId;

    @Inject
    protected MainPresenter presenter;
    @Inject
    protected MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().injectMainActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter.attachView(this);
        init();
        presenter.onCreate(savedInstanceState);

        // при наличии интернета, если пользователь не вошел в учетную запись, постоянно просим
        // его залогиниться
        if(NetworkUtils.isNetworkConnected(this) && !VKSdk.isLoggedIn())
                VKSdk.login(this, VKScope.FRIENDS);
    }

    private void init() {
        swipeLayout.setColorSchemeResources(R.color.colorAccent);
        swipeLayout.setOnRefreshListener(() -> presenter.syncData());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvData.setLayoutManager(llm);
        rvData.addItemDecoration(new DividerItemDecoration(rvData.getContext(), llm.getOrientation()));
        rvData.setHasFixedSize(true);
        rvData.setAdapter(adapter);
    }

    @OnClick(R.id.btnAddId)
    void onClick() {
        if (NetworkUtils.isNetworkConnected(this))
            presenter.addItem(etUserId.getText().toString());
        // не стал писать вывод сообщения об ошибке в случае отсутсвия подключения
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Log.d("LOG_TAG", "authentication success!");
                presenter.setNewAccessToken(res.accessToken);
                presenter.syncData();
                // Пользователь успешно авторизовался
            }
            @Override
            public void onError(VKError error) {
                Log.d("LOG_TAG", "authentication error: " + error.errorMessage);
                presenter.syncData();
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void showWait() {
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void showUsers(List<User> list) {
        adapter.setData(list);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void addUser(User item) {
        adapter.insertItem(item);
        rvData.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void deleteUser(int position) {
        adapter.removeItem(position);
    }

    @Override
    public void startUserActivity(User item) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra(UserParcelable.class.getCanonicalName(), new UserParcelable(item));
        startActivity(intent);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        swipeLayout.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // не стал менять названия пунктов меню, в зависимости от того,
        // залогинен ли пользователь

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selLogin:
                if (!VKSdk.isLoggedIn()) {
                    if (NetworkUtils.isNetworkConnected(this))
                        VKSdk.login(this, VKScope.FRIENDS);
                }
                break;
            case R.id.selLogout:
                if (NetworkUtils.isNetworkConnected(this) && VKSdk.isLoggedIn()) {
                    VKSdk.logout();
                    presenter.resetAccessToken();
                    presenter.syncData();
                    // после выхода из учетной записи, обнуляем токен до служебного ключа, и обновляем данные
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
