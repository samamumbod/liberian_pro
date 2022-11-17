package com.update_book;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.liberian.auth.Book;
import com.liberian.auth.CategoryJson;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;
import com.record_book.RecordBookActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateBookActivity extends AppCompatActivity {

    int counter =1;
    Timer timer = new Timer();
    List<String> categoryList = new ArrayList();
    String tableName="";
    Book book;
    Book book1 = new Book();
    RetrieveBookDetails retrieveBookTask = null;
    EditText isbnField;
    EditText bookTitileField;
    EditText authorField;
    EditText publishYearField;
    EditText copiesField;
    EditText bookCategoryField;
    Spinner bookCategorySpinner;
    ImageButton decrementButton;
    ImageButton incrementButton;
    ImageButton categoryButton;
    Button updateButton;

    ArrayAdapter<String> adapter = null;

    SaveBookToDatabaseTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        isbnField = findViewById(R.id.update_isbn_field);
        bookTitileField = findViewById(R.id.update_book_title_field);
        authorField = findViewById(R.id.update_author_field);
        publishYearField = findViewById(R.id.update_publish_year_field);
        copiesField = findViewById(R.id.update_copies_field);
        bookCategoryField = findViewById(R.id.update_category);
        decrementButton = findViewById(R.id.decrement_button);
        incrementButton = findViewById(R.id.increment_button);
        updateButton = findViewById(R.id.update_button);
        categoryButton = findViewById(R.id.imageButton6);

        categoryList.add(" -- Select -- ");
        isbnField.setFocusable(false);
        copiesField.setFocusable(false);
        bookCategoryField.setFocusable(false);

        String ISBN = getIntent().getStringExtra("isbn");
        if (isDigit(ISBN)){
            retrieveBookTask = new RetrieveBookDetails(ISBN, this);
            retrieveBookTask.execute();
        }


        callAsynchronousTask();

        decrementButton.setOnClickListener(v -> {
            if (counter > 1){
                counter-= 1;
                copiesField.setText(String.valueOf(counter));
            }
        });

        incrementButton.setOnClickListener(v -> {
            counter+=1;
            copiesField.setText(String.valueOf(counter));
        });

        AlertDialog dailog = new AlertDialog.Builder(this).create();
        dailog.setMessage("Empty fields");
        dailog.setTitle("Error");
        dailog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());

        updateButton.setOnClickListener(v -> {
            try{
                if (!isbnField.getText().toString().isEmpty() && !bookTitileField.getText().toString().isEmpty()
                        && !authorField.getText().toString().isEmpty() && !publishYearField.getText().toString().isEmpty()
                        && !bookCategoryField.getText().toString().isEmpty()
                        && isDigit(isbnField.getText().toString()) && isDigit(publishYearField.getText().toString())){

                    book1.setAuthor(authorField.getText().toString());
                    book1.setIsbn(Long.parseLong(isbnField.getText().toString()));
                    book1.setBooktitle(bookTitileField.getText().toString());
                    book1.setPublish_year(Integer.parseInt(publishYearField.getText().toString()));
                    book1.setBook_category(bookCategoryField.getText().toString());
                    book1.setCopies(Integer.parseInt(copiesField.getText().toString()));

                    if (book.compare(book,book1)!=1){
                        task = new SaveBookToDatabaseTask(v.getContext(),tableName);
                        task.execute();
                    }
                    else{
                        Toast.makeText(v.getContext(),"No change",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    dailog.show();
                }
            }catch (Exception e){
                dailog.show();
            }
        });


        categoryButton.setOnClickListener(v-> showBookCategoryDialog(v.getContext()));

    }


    private void showBookCategoryDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.book_category_layout,null);

        builder.setTitle("Book category");
        builder.setView(view);
        bookCategorySpinner  = view.findViewById(R.id.spinner2);
        if (adapter!=null){
            bookCategorySpinner.setAdapter(adapter);
        }

        builder.setPositiveButton("Add", (dialog1, which) -> {
            if (bookCategorySpinner.getSelectedItemPosition() != 0){
                bookCategoryField.setText(bookCategorySpinner.getSelectedItem().toString());
            }
        }).setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        final AlertDialog dialog =  builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()  == R.id.add_item){
            showDialog(this);
            return true;
        }
        else if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(this, RecordBookActivity.class));
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (task !=null){
            task.cancel(true);
        }

        if (retrieveBookTask!=null){
            retrieveBookTask.cancel(true);
        }
        super.onBackPressed();
    }


    /**
     * This method saves the book information into the database of the institute.
     */
    class SaveBookToDatabaseTask extends AsyncTask<Void,Void,Void> {
        String result;
        Context context;
        ProgressDialog progressDialog;
        AlertDialog dialog = null;
        String tableName;

        public SaveBookToDatabaseTask(Context context, String tableName) {
            this.context = context;
            this.tableName = tableName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());


        }

        @Override
        protected Void doInBackground(Void... voids) {

            result = LiberianAuth.updateBook(tableName,
                    Long.parseLong(isbnField.getText().toString()),
                    bookTitileField.getText().toString(),
                    authorField.getText().toString(),
                    Integer.parseInt(publishYearField.getText().toString()),
                    bookCategoryField.getText().toString(),
                    Integer.parseInt(copiesField.getText().toString())
            );
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
            if ("Success".equals(result)){
                dialog.setMessage("Successful");
                dialog.show();
            }
            else if ("Failed".equals(result)){
                dialog.setMessage("Record already exist");
                dialog.show();
            }
            else if ("error".equals(result)){
                dialog.setMessage("Ooops network problem");
                dialog.show();
            }
            resetFields(isbnField,
                    bookTitileField,authorField,publishYearField,copiesField,bookCategoryField);
            counter =1;
            copiesField.setText(String.valueOf(counter));
        }
    }


    public void resetFields( EditText... editTexts){
        for (EditText editText : editTexts) {
            editText.setText("");
        }
    }


    public void showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_add_isbn,null);

        builder.setTitle("ISBN");
        builder.setView(view);
        EditText editText = view.findViewById(R.id.dialog_isbn_field);
        builder.setPositiveButton("Add", (dialog1, which) -> {
            String result = editText.getText().toString();
            if (isDigit(result)){
                retrieveBookTask = new RetrieveBookDetails(result, context);
                retrieveBookTask.execute();
            }
            else{
                Toast.makeText(context,"Invalid ISBN",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        final AlertDialog dialog =  builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!s.toString().isEmpty());
            }
        });
    }


    private boolean isDigit(String number){
        try{
            Long.parseLong(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * This method laods data into spinner remotely
     */
    public void callAsynchronousTask() {
        final Handler handler = new Handler();

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        RetrieveBookTask task =  new RetrieveBookTask(getApplicationContext(),tableName);
                        task.execute();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000);
    }


    /***
     * This method retrieves book so that the liberian can select.
     */
    @SuppressLint("StaticFieldLeak")
    class RetrieveBookTask extends AsyncTask<Void,Void,Void>{

        List<CategoryJson> list;
        String result = "";
        String tableName;
        Context context;

        public RetrieveBookTask(Context context, String tableName) {
            this.context = context;
            this.tableName =  tableName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                list = LiberianAuth.retrieveCategory(tableName);

            }catch (IOException e){
                result = "error";
            }
            finally {
                if (categoryList.size()>1){

                }
                else {
                    if (list!=null){
                        for (CategoryJson item: list){
                            categoryList.add(item.getCategory());
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
                if (categoryList.size()>1){
                    adapter = new ArrayAdapter(context,R.layout.my_selected_item,categoryList);
                    adapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    adapter.notifyDataSetChanged();
                    timer.cancel();
                }
        }
    }


    /**
     * This class retrieves the details of student and book for the issueing of the desired
     * book.
     */
    @SuppressLint("StaticFieldLeak")
    class RetrieveBookDetails extends AsyncTask<Void,Void,Void>{

        String isbn;
        String result;
        Context context;
        ProgressDialog progressDialog;

        public RetrieveBookDetails(String isbn, Context context) {
            this.isbn = isbn;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Retrieving information");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                book = LiberianAuth.retrieveBook(tableName,Long.parseLong(isbn));
            }catch (Exception e){
                result = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
            if ("error".equals(result)){
                Toast.makeText(UpdateBookActivity.this,"Ooops network error",Toast.LENGTH_SHORT).show();
            }
            else{
                if (book!=null){
                    isbnField.setText(book.getIsbn());
                    bookTitileField.setText(book.getBooktitle());
                    authorField.setText(book.getAuthor());
                    publishYearField.setText(book.getPublish_year());
                    bookCategoryField.setText(book.getBook_category());
                    copiesField.setText(book.getCopies());
                    counter = Integer.parseInt(book.getCopies());
                }
                else{
                    isbnField.setText("");
                    bookTitileField.setText("");
                    authorField.setText("");
                    publishYearField.setText("");
                    bookCategoryField.setText("");
                    copiesField.setText("1");
                    counter = 1;
                    Toast.makeText(UpdateBookActivity.this,"Book not found",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}