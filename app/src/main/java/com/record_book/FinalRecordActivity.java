package com.record_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.liberian.auth.CategoryJson;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FinalRecordActivity extends AppCompatActivity {

    int counter =1;
    Timer timer = new Timer();

    String tableName="";
    EditText isbnField;
    EditText bookTitileField;
    EditText authorField;
    EditText publishYearField;
    EditText copiesField;
    Spinner bookCategorySpinner;
    ImageButton decrementButton;
    ImageButton incrementButton;
    Button saveButton;

    SaveBookToDatabaseTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_record);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        isbnField = findViewById(R.id.isbn_field);
        bookTitileField = findViewById(R.id.book_title_field);
        authorField = findViewById(R.id.author_field);
        publishYearField = findViewById(R.id.publish_year_field);
        bookCategorySpinner = findViewById(R.id.spinner);
        copiesField = findViewById(R.id.copies_field);
        decrementButton = findViewById(R.id.decrement_button);
        incrementButton = findViewById(R.id.increment_button);
        saveButton = findViewById(R.id.save_button);

        isbnField.setFocusable(false);
        copiesField.setFocusable(false);

        String ISBN = getIntent().getStringExtra("isbn");
        isbnField.setText(ISBN);


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

        saveButton.setOnClickListener(v -> {
            try{
                if (!isbnField.getText().toString().isEmpty() && !bookTitileField.getText().toString().isEmpty()
                        && !authorField.getText().toString().isEmpty() && !publishYearField.getText().toString().isEmpty()
                        && bookCategorySpinner.getSelectedItemPosition()!=0 && !bookCategorySpinner.getAdapter().isEmpty()
                        && isDigit(isbnField.getText().toString()) && isDigit(publishYearField.getText().toString())){
                    task = new SaveBookToDatabaseTask(v.getContext(),tableName);
                    task.execute();
                }
                else{
                    dailog.show();
                }
            }catch (Exception e){
                dailog.show();
            }
        });
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
            startActivity(new Intent(this,RecordBookActivity.class));
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (task !=null){
            task.cancel(true);
        }
        super.onBackPressed();
    }

    /**
     * This method saves the book information into the database of the institute.
     */
    class SaveBookToDatabaseTask extends AsyncTask<Void,Void,Void>{
        String result;
        Context context;
        ProgressDialog progressDialog;
        AlertDialog dialog = null;
        String selectedItem;
        String tableName;

        public SaveBookToDatabaseTask(Context context, String tableName) {
            this.context = context;
            this.tableName = tableName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Saving...");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());

            selectedItem = bookCategorySpinner.getSelectedItem().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            result = LiberianAuth.recordBook(tableName,
                    Long.parseLong(isbnField.getText().toString()),
                    bookTitileField.getText().toString(),
                    authorField.getText().toString(),
                    Integer.parseInt(publishYearField.getText().toString()),
                    selectedItem,
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
            resetFields(bookCategorySpinner,isbnField,
                    bookTitileField,authorField,publishYearField,copiesField);
            counter =1;
            copiesField.setText(String.valueOf(counter));
        }
    }


    public void resetFields(Spinner spinner, EditText... editTexts){
        for (EditText editText : editTexts) {
            editText.setText("");
        }
        spinner.setSelection(0);
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
            isbnField.setText(result);

        }).setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });

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
                if (s.toString().isEmpty()){
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
                else {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }


    /**
     * This method laods data into spinner remotely
     */
    public void callAsynchronousTask() {
        final Handler handler = new Handler();

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            RetrieveBookTask task =  new RetrieveBookTask(getApplicationContext(),tableName);
                            task.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //execute in every 50000 ms

    }


    private boolean isDigit(String number){
        try{
            Long.parseLong(number);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /***
     * This method retrieves book so that the liberian can select.
     */
    class RetrieveBookTask extends AsyncTask<Void,Void,Void>{

        List<CategoryJson> list;
        List<String> categoryList = new ArrayList();
        String result;
        String tableName;
        Context context;

        public RetrieveBookTask(Context context, String tableName) {
            this.context = context;
            this.tableName =  tableName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            categoryList.add(" -- Select --");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                list = LiberianAuth.retrieveCategory(tableName);
                for (CategoryJson item: list){
                    if (item.getCategory() != null || !item.getCategory().isEmpty()){
                        categoryList.add(item.getCategory());
                    }
                }
            }catch (IOException e){
                result = "error";
            }
            finally {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (result == "error"){
//                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }
            else{
                if (list == null){
                    list = new ArrayList();
                    ArrayAdapter<String> adapter = new ArrayAdapter(context,R.layout.my_selected_item,categoryList);
                    adapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    adapter.notifyDataSetChanged();
                    bookCategorySpinner.setAdapter(adapter);
                }
                else{
                    ArrayAdapter<String> adapter = new ArrayAdapter(context,R.layout.my_selected_item,categoryList);
                    adapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    adapter.notifyDataSetChanged();
                    bookCategorySpinner.setAdapter(adapter);
                }
                timer.cancel();
            }
        }
    }
}