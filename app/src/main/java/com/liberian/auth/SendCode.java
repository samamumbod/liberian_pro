
package com.liberian.auth;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 *
 * @author Sama
 */
public class SendCode {
    
    
    public static int generate6DigitCode(){
        Random random = new Random();
        int number = random.nextInt(899999) + 100000;
        return number;
    }
    
    
    public static String sendCodeToUser(String email, int code) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/verify/code1.php?reciever="+email+"&code="+code)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();

        Status status = gson.fromJson(json, Status.class);
        return status.getStatus();
    }
}