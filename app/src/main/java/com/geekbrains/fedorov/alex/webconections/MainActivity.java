package com.geekbrains.fedorov.alex.webconections;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.geekbrains.fedorov.alex.webconections.rest.entities.GitHubRestApi;
import com.geekbrains.fedorov.alex.webconections.rest.entities.ReposRestModel;
import com.geekbrains.fedorov.alex.webconections.rest.entities.UserRestModel;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String HTTPS_API_GITHUB_COM = "https://api.github.com/";
    private EditText login;
    private EditText result;

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(HTTPS_API_GITHUB_COM)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build();

    GitHubRestApi api = retrofit.create(GitHubRestApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        login = findViewById(R.id.request);
        result = findViewById(R.id.result);

        findViewById(R.id.loginBtn).setOnClickListener(view ->
                api.getUser(login.getText().toString())
                        .retry(2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<UserRestModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(UserRestModel userRestModel) {
                                result.setText(userRestModel.getLogin());
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }));

        findViewById(R.id.reposBtn).setOnClickListener(view ->
                api.getRepos(login.getText().toString())
                        .retry(2)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<ReposRestModel>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(List<ReposRestModel> listReposRestModel) {
                                if (listReposRestModel.isEmpty()) return;

                                result.setText(listReposRestModel.get(0).getName());
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }));
    }
}
