/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liberian.auth;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 *
 * @author Sama
 */
public class ValidateEmailPassword {


    public static boolean checkPassword(String password){
        if (isValidPassword(password)) {
            return true;
        }
        return false;
    }


    public static boolean checkEmail(String email) throws IOException, InterruptedException{
        if (emailPattern(email)) {
            if (isExisting(email)) {
                return true;
            }
        }
        return false;
    }


    private static boolean emailPattern(String email){
        if (email.matches("[^@]+@[^@]+\\.[^@]+")) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean isExisting(String email) throws IOException, InterruptedException{

        String apiKey = "12ae7876-e75c-4f49-b96e-4778258d6049";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://isitarealemail.com/api/email/validate?email=" + email)
                .header("Authorization","Bearer "+apiKey)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Gson gson = new Gson();
        Status status = gson.fromJson(result,Status.class);

        if ("valid".equals(status.getStatus())) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean isValidPassword(String password){
        String regex = "[a-zA-Z0-9\\S]{8,16}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


}
