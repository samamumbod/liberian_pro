package com.category;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liberian.auth.CategoryJson;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.IOException;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    static String tableName="";
    static RecyclerView recyclerView;
    static CategoryAdapter categoryAdapter;
    static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        recyclerView = findViewById(R.id.category_list);
        progressBar = findViewById(R.id.progressBar3);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        RetrieveBookTask task = new RetrieveBookTask(CategoryActivity.this);
        task.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_category,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add){
            showDialog(CategoryActivity.this);
            return true;
        }
        else if (item.getItemId() == R.id.refresh) {
            RetrieveBookTask task = new RetrieveBookTask(CategoryActivity.this);
            task.execute();
        }
        return false;
    }

    public void showDialog(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_category_dialog,null);

        dialog.setTitle("Category");
        dialog.setView(view);
        EditText editText = view.findViewById(R.id.categoryTextBox);


        dialog.setPositiveButton("Add", (dialog1, which) -> {
            String result = editText.getText().toString();
            if (result.isEmpty()){

            }
            else{
                AddBookTask task = new AddBookTask(context,result);
                task.execute();
            }

        }).setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

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
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                }
                else{
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }


    static class AddBookTask extends AsyncTask<Void,Void,Void>{

        Context context;
        String category;
        String result="";

        public AddBookTask(Context context, String category) {
            this.context = context;
            this.category = category;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Adding book category",Toast.LENGTH_SHORT).show();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                result = LiberianAuth.addCategory(tableName,category);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (result.equals("Success")){
                Toast.makeText(context,"Book category added",Toast.LENGTH_SHORT).show();
                RetrieveBookTask task = new RetrieveBookTask(context);
                task.execute();
            }
            else if (result.equals("Failed")){
                Toast.makeText(context,"Book category already exist",Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }
        }
    }


    static class RetrieveBookTask extends AsyncTask<Void,Void,Void>{

        List<CategoryJson> list;
        String result="";
        Context context;

        public RetrieveBookTask(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                list = LiberianAuth.retrieveCategory(tableName);
            }catch (IOException e){
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }
            else{
                if (list != null){
                    categoryAdapter = new CategoryAdapter(list);
                    recyclerView.setAdapter(categoryAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                }
            }
        }
    }

    static void restart(Context context){
        RetrieveBookTask task = new RetrieveBookTask(context);
        task.execute();
    }

}