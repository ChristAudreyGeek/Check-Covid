package scolaire.gestion.payementmobilproject.Server;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAjgsrbR4:APA91bHD1kuK5kw-FwaQ87bz4Gk2QW5CU88q9vSVFzqRuRp0evps7RTENzB4xF_Wjfh8i6WoRwj10iv1-yHw6qoYlQrimSWd6_8ZFG2kO_fXocr_VvH0S9D52xbKdWO98EJlrHbVjIot"

    }
    )


    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
