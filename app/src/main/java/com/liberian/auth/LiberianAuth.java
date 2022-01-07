/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liberian.auth;


import com.department.Department;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Sama
 */
public class LiberianAuth {


    private static final MediaType MEDIA_TYPE_PDF = MediaType.parse("image/png");

    /**
     * This method signs in the Liberian.<br/>
     * If <b>result = null</b> it means email does not exist.<br/>
     * If <b>result = some data </b> it means email exist.
     * <br/>
     * if <b>result = error</b> it means connection problem.
     * @param email
     * @return result
     */
    public static UserDetail signin(String email) throws IOException{
        UserDetail result;
        result = signinMethod(email);
        return result;
    }


    /**
     * This method is the core of the signin method
     * @param email
     * @return
     * @throws IOException
     */
    private static UserDetail signinMethod(String email) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/account/signin.php?email="+email)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        UserDetail userDetail1 = gson.fromJson(json, UserDetail.class);
        return userDetail1;
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
                .url("http://192.168.10.1/liberian/category/addcategory.php?table="+table+"&booktitle="+category)
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
                .url("http://192.168.10.1/liberian/category/removecategory.php?table="+table+"&booktitle="+category)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
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
                .url("http://192.168.10.1/liberian/category/retrievecategory.php?table="+table)
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
                .url("http://192.168.10.1/liberian/category/addschool.php?table="+table+"&school="+school+"&meaning="+meaning)
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
                .url("http://192.168.10.1/liberian/category/retrieveschool.php?table="+table)
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
                .url("http://192.168.10.1/liberian/category/removeschool.php?table="+table+"&school="+school)
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
                .url("http://192.168.10.1/liberian/books/addbook.php?table=" +tableName+
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
    public static String updateBook(String tableName, long isbn, String bookTitle,
                                    String author, int publish_year, String category, int copies) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/books/update_book.php?table=" +tableName+
                        "&isbn=" +isbn+
                        "&title=" +bookTitle +
                        "&author=" +author +
                        "&year=" +publish_year+
                        "&category=" +category+
                        "&copy=" + copies)
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


    /***
     * This method retrieves the details of student during issue and return transactions.
     */
    public static StudentInfosTransaction retrieveStudentDetails(String tableName, int number) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/retrieve_detail1.php?table=" +tableName+"&number="+number)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        StudentInfosTransaction transactions = gson.fromJson(response.body().string(),StudentInfosTransaction.class);
        return transactions;
    }


    /**
     * This method retrieves the details of book during issue and return transactions
     */
    public static BookInfosTransaction retrieveBookDetail(String tableName, long isbn) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/retrieve_detail2.php?table=" +tableName +
                        "&isbn="+isbn)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        Gson gson = new Gson();
        BookInfosTransaction transactions = gson.fromJson(response.body().string(),BookInfosTransaction.class);
        return transactions;
    }

    /**
     * This method retrieves the details of book during issue and return transactions
     */
    public static Book retrieveBook(String tableName, long isbn) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/books/retrievesingle.php?table=" +tableName +
                        "&isbn="+isbn)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        Gson gson = new Gson();
        Book book = gson.fromJson(response.body().string(),Book.class);
        return book;
    }


    /**
     * This method is used to issue the outgoing book.
     */
    public static String issueBook(String tableName, int regNumber, long isbn, String date1, String date2){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/issue.php?table=" +tableName+
                        "&reg_number="+regNumber+
                        "&isbn="+isbn+
                        "&issuedate="+date1+
                        "&returndate="+date2+
                        "&return_status="+"Not returned")
                .method("GET", null)
                .build();

        try{
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            Status status = gson.fromJson(response.body().string(),Status.class);
            return status.getStatus();
        }catch (IOException e){
            return "error";
        }
    }


    /**
     * checks if record exist
     * @return
     * @param tableName
     */
    public static boolean recordExist(String tableName,int number, long isbn ) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/check_issue.php?table=" +tableName+
                        "&number="+number+
                        "&isbn="+isbn)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        CheckRecord checkRecord = gson.fromJson(response.body().string(), CheckRecord.class);
        if (checkRecord.getNumber()>0) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     *
     * @param tableName
     * @param regNumber
     * @param isbn
     * @throws IOException
     */
    public static RetrieveTransactionDate retrieveTransactionDate(String tableName, int regNumber, long isbn) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/retrieve_transaction.php?table=" +tableName+
                        "&number="+regNumber+
                        "&isbn="+isbn)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();

        RetrieveTransactionDate r = gson.fromJson(response.body().string(), RetrieveTransactionDate.class);
        return r;
    }


    /**
     *
     * @param tableName
     * @param number
     * @param isbn
     * @param todaysDate
     * @param returnStatus
     * @throws IOException
     */
    public static String returnBook(String tableName, int number, long isbn, String todaysDate, String returnStatus) throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/transactions/return.php?table=" +tableName+
                        "&number="+number+
                        "&isbn="+isbn+
                        "&ac_return_date="+todaysDate+
                        "&return_status="+returnStatus)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        Gson gson = new Gson();
        Status status = gson.fromJson(response.body().string(),Status.class);
        return status.getStatus();
    }




    public static String saveSignature(File file1, String email) throws IOException {

        String serverURL = "http://192.168.10.1/liberian/setting/setting.php?email="+email;

        //post request to send file
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "name", RequestBody.create(MEDIA_TYPE_PDF, file1))
                .build();

        Request request = new Request.Builder().url(serverURL)
                .post(requestBody).build();

        Response response = client.newCall(request).execute();
        String result = null;

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        else{
            Gson gson = new Gson();
            Status status = gson.fromJson(response.body().string(),Status.class);
            result = status.getStatus();
        }
        return result;
    }


    public static String addDepartments(String table, String department) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/department/add_department.php?table="+table+"&department="+department)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        Status status = gson.fromJson(json,Status.class);
        return status.getStatus();
    }


    public static List<Department> retrieveDepartments(String table) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/department/get_department.php?table="+table)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Gson gson = new Gson();
        Department[] departments = gson.fromJson(json,Department[].class);
        return Arrays.asList(departments);
    }


    public static String removeDepartments(String table, String department) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.10.1/liberian/department/remove_department.php?table="+table+"&department="+department)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
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
