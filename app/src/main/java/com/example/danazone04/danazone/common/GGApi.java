package com.example.danazone04.danazone.common;

import com.example.danazone04.danazone.remote.IGoogleApi;
import com.example.danazone04.danazone.remote.RetrofClient;

public class GGApi {
    public static final String baseURL = "https://maps.googleapis.com";

    public static IGoogleApi getGoogleAPI() {
        return RetrofClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
