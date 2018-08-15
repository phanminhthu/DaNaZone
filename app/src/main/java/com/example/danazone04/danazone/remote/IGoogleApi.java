package com.example.danazone04.danazone.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by PC on 1/26/2018.
 */

public interface IGoogleApi {
    @GET
    Call<String> getPath(@Url String url);
}
