package com.strada.mamoapp.service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA4WGGD4A:APA91bGfFC5rZ1LlCF4IJyuqjcYsAIh0TzYwciDWM6UsPs9ltVpfS9Aon1dzNqwn_frrjaa7aF9d37H2vPf25icnR60TgVm1hOieGYskyZG2tvS8uoBPDV0F-QcJcVpJM1G6aZOsvHUz" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}