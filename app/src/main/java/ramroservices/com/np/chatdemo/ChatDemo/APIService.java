package ramroservices.com.np.chatdemo.ChatDemo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ramroservices.com.np.chatdemo.Notification.MyResponse;
import ramroservices.com.np.chatdemo.Notification.Sender;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAArHhYA1Q:APA91bH-r0KqVM0v9z8Yo7KiQqcRBscNbTgJPt3xW7FEf_SwY3gTT592Ut-dFPydgLGDb1aSG1teV_DonMSn9cx-AsW2dYaCV9GhBSeFYYyZy6vHumdAAGNOFVNtLI6-puVBJyPucDYa"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendnotification(@Body Sender body);
}
