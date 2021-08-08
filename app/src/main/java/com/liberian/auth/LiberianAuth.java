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


    /**
     * This method is the core of the signin method
     * @param email
     * @return
     * @throws IOException
     */
    private static String signinMethod(String email) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/account/signin.php?email="+email)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        Password password1 = gson.fromJson(json,Password.class);
        if (password1.getPassword() == null){
            return "";
        }
        return password1.getPassword();
    }


    /**
     * This method adds a book category into the library system.
     * @param table
     * @param category
     * @return
     * @throws IOException
     */
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


    /**
     * This method removes a book category in the library system
     * @param table
     * @param category
     * @return
     * @throws IOException
     */
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


    /**
     * This method retrieves all the book category in the library
     * @param table
     * @return
     * @throws IOException
     */
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


    /**
     * This method keeps record of all the schools in the university
     * @param table
     * @param school
     * @param meaning
     * @return
     * @throws IOException
     */
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


    /**
     * This method retrieves all the school in the university
     * @param table
     * @return
     * @throws IOException
     */
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


    /**
     * This method removes a school if there's a mistake
     * @param table
     * @param school
     * @return
     * @throws IOException
     */
    public static String removeSchool(String table, String school) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/category/removeschool.php?table="+table+"&school="+school)
                .method("GET", null)
                .build();
        Response response = null;
            response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Status status = gson.fromJson(json,Status.class);
            return status.getStatus();
    }

    /**
     * This method records book in the library into the library system.
     * @param tableName
     * @param isbn
     * @param bookTitle
     * @param author
     * @param publish_year
     * @param category
     * @param copies
     * @return
     * @throws IOException
     */
    public static String recordBook(String tableName, long isbn, String bookTitle,
                                    String author, int publish_year, String category, int copies) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www.mshelter.tech/liberian/books/addbook.php?table=" +tableName+
                        "&isbn=" +isbn+
                        "&booktitle=" +bookTitle +
                        "&author=" +author +
                        "&publish_year=" +publish_year+
                        "&category=" +category+
                        "&copies=" + copies)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            Status status = gson.fromJson(response.body().string(),Status.class);
            return status.getStatus();
        }catch (IOException e){
            return "error";
        }

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
