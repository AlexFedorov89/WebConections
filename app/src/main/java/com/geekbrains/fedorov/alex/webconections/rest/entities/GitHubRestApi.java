package com.geekbrains.fedorov.alex.webconections.rest.entities;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubRestApi {
    @GET("users/{path}")
    Single<UserRestModel> getUser(@Path("path") String user);

    @GET("users/{path}/repos")
    Single<List<ReposRestModel>> getRepos(@Path("path") String user);

}
