package com.department;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.IOException;
import java.util.List;

public class DepartmentActivity extends AppCompatActivity {

    static String tableName="";
    static ProgressBar progressBar;
    static DepartmentAdapter departmentAdapter;
    static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        progressBar = findViewById(R.id.progressBar4);
        recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        RetrieveDepartmentTask task = new RetrieveDepartmentTask(this);
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
            showDialog(this);
            return true;
        }
        else if (item.getItemId() == R.id.refresh) {
            RetrieveDepartmentTask task = new RetrieveDepartmentTask(this);
            task.execute();
            return true;
        }
        return false;
    }


    private void showDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_department_dialog,null);

        dialog.setTitle("Department");
        dialog.setView(view);
        EditText editText = view.findViewById(R.id.departmentEditText);

        dialog.setPositiveButton("Add", (dialog1, which) -> {
            String result = editText.getText().toString();
            if (!result.isEmpty()){
                AddDepartmentTask task = new AddDepartmentTask(context,result);
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


    static class AddDepartmentTask extends AsyncTask<Void,Void,Void>{

        Context context;
        String department;
        String result;

        public AddDepartmentTask(Context context, String department) {
            this.context = context;
            this.department = department;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Adding department",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                result = LiberianAuth.addDepartments(tableName,department);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (result.equals("Success")){
                Toast.makeText(context,"Department added",Toast.LENGTH_SHORT).show();
                RetrieveDepartmentTask task = new RetrieveDepartmentTask(context);
                task.execute();
            }
            else if (result.equals("Failed")){
                Toast.makeText(context,"Department already exist",Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
            }
        }
    }


    static class RetrieveDepartmentTask extends AsyncTask<Void,Void,Void>{

        List<Department> list = null;
        String result = "";
        Context context;

        public RetrieveDepartmentTask(Context context) {
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
                list = LiberianAuth.retrieveDepartments(tableName);
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
                if(list!=null){
                    departmentAdapter = new DepartmentAdapter(list);
                    recyclerView.setAdapter(departmentAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
                }
            }
        }
    }


    static void restart(Context context){
        RetrieveDepartmentTask task = new RetrieveDepartmentTask(context);
        task.execute();
    }

}