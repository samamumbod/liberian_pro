/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liberian.auth;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Sama
 */
public class LiberianAuth {


    /**
     * This method signs in the Liberian.<br/>
     * If <b>result = null</b> it means email does not exist.<br/>
     * If <b>result = some data </b> it means email exist.
     * <br/>
     * if <b>result = error</b> it means connection problem.
     * @param email
     * @return result
     */
    public static String signin(String email){
        String result = "";
        try{
            result = signinMethod(email);
        }catch(IOException e){
            result="error";
        }
        return result;
    }


    private static String signinMethod(String email) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost/liberian/account/signin.php?email="+email)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        Password password1 = gson.fromJson(json,Password.class);
        return password1.getPassword();
    }

    public static String addCategory(String table, String category) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/addcategory.php?table="+table+"&booktitle="+category)
                .method("GET", null)
                .build();
        Response response = null;
            response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Status status = gson.fromJson(json,Status.class);
            return status.getStatus();

    }

    public static String removeCategory(String table, String category) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/removecategory.php?table="+table+"&booktitle="+category)
                .method("GET", null)
                .build();
        Response response = null;

            response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Status status = gson.fromJson(json,Status.class);
            return status.getStatus();

    }

    public static List<CategoryJson> retrieveCategory(String table) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/retrievecategory.php?table="+table)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            CategoryJson[] categoryJson = gson.fromJson(json,CategoryJson[].class);
            return Arrays.asList(categoryJson);
    }


    public static String addSchool(String table, String school, String meaning) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/addschool.php?table="+table+"&school="+school+"&meaning="+meaning)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Status status = gson.fromJson(json,Status.class);
            return status.getStatus();
    }


    public static List<SchoolJson> retrieveSchool(String table) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/retrieveschool.php?table="+table)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            SchoolJson[] schoolJson = gson.fromJson(json,SchoolJson[].class);
            return Arrays.asList(schoolJson);
    }


    public static String removeSchool(String table, String school) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/removechool.php?table="+table+"&school="+school)
                .method("GET", null)
                .build();
        Response response = null;
            response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Status status = gson.fromJson(json,Status.class);
            return status.getStatus();
    }


    private static String resetUserPassword(String email, String password) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost/liberian/account/change_password.php?email="+email+"&password="+password)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        Status status = gson.fromJson(json,Status.class);
        return status.getStatus();
    }



}
