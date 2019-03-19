package com.example.android.pawstwo.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers (

            {
                  "Content-Type:application/json", "Authorization:key=AAAARX_gK1k:APA91bHvq2AjVxFWRbl4pbnV9HMkQcCjJDgz-H3xJLsjyyVz-t5fsLb6LkrR8xW5GFDigBj9zaZUhCcvQt510k3LOcSMpMAwprsOf5MjB1pC_RfPkYrSjaWer3u40TZwIKemwiqKzWfv"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification( @Body Sender body);
}
