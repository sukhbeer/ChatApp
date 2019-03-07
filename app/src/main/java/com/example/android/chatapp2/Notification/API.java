package com.example.android.chatapp2.Notification;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAM8gmJrU:APA91bEVrtOiKg98HvFD68LSPPnK9hpukyyshMFfu4LDe4rxVq2Uz8-tjgFGLMjvz8-syCMqcoTgkpcRHdnJaaonBKzbkZ3gLlcyVXm9LLVDcKv32RzRoUsiXO_WcqYj87N9x64kfS8c"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
