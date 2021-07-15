package com.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liberian.auth.CategoryJson;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.MainActivity;
import com.liberianpro.R;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    static RecyclerView recyclerView;
    static CategoryAdapter categoryAdapter;
    static List<CategoryJson> categoryList= new ArrayList<>();
    static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        recyclerView = findViewById(R.id.category_list);
        progressBar = findViewById(R.id.progressBar3);

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
        dialog.show();
    }


    static class AddBookTask extends AsyncTask<Void,Void,Void>{

        Context context;
        String category;
        String result;

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
                result = LiberianAuth.addCategory("uba",category);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (result == "Success"){
                Toast.makeText(context,"Book category added",Toast.LENGTH_SHORT).show();
            }
            else if (result == "Failed"){
                Toast.makeText(context,"Book category already exist",Toast.LENGTH_SHORT).show();
            } else if (result == "error"){
                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }

            RetrieveBookTask task = new RetrieveBookTask(context);
            task.execute();
        }
    }

    static class RetrieveBookTask extends AsyncTask<Void,Void,Void>{

        List<CategoryJson> list;
        String result;
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
                list = LiberianAuth.retrieveCategory("uba");
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
            if (result == "error"){
                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }
            else{
                if (list == null){
                    categoryList = new ArrayList<>();
                    categoryAdapter = new CategoryAdapter(categoryList);
                    recyclerView.setAdapter(categoryAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                }
                else{
                    categoryList = list;
                    categoryAdapter = new CategoryAdapter(categoryList);
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