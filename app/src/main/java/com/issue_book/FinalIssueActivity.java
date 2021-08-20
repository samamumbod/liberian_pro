package com.issue_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.liberian.auth.BookInfosTransaction;
import com.liberian.auth.LiberianAuth;
import com.liberian.auth.StudentInfosTransaction;
import com.liberianpro.R;
import com.return_book.FinalReturnActivity;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class FinalIssueActivity extends AppCompatActivity {


    String date1;
    String date2;
    String tableName="";

    EditText reg_numberField;
    EditText studentNameField;
    EditText schoolField;
    EditText departmentField;
    EditText levelField;
    EditText sexField;
    EditText isbnField;
    EditText bookTitleField;
    EditText authorField;
    EditText issueDateField;
    EditText returnDateField;
    Button issueButton;
    ImageButton issueDateButton;
    ImageButton returnDateButton;

    RetrieveDetailsForTransacntionTask transacntionTask = null;
    IssueTransanctionTask task = null;

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_issue);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        scrollView = findViewById(R.id.issue_book_scroll_view);
        reg_numberField = findViewById(R.id.issue_reg_number);
        studentNameField = findViewById(R.id.issue_student_name);
        schoolField = findViewById(R.id.issue_school);
        departmentField = findViewById(R.id.issue_department);
        levelField = findViewById(R.id.issue_level);
        sexField = findViewById(R.id.issue_sex);
        isbnField = findViewById(R.id.issue_isbn);
        bookTitleField = findViewById(R.id.issue_book_title);
        authorField = findViewById(R.id.issue_author);
        issueDateField = findViewById(R.id.issue_issuedate);
        returnDateField = findViewById(R.id.issue_returndate);
        issueButton = findViewById(R.id.button10);
        issueDateButton = findViewById(R.id.imageButton3);
        returnDateButton = findViewById(R.id.imageButton4);

        reg_numberField.setFocusable(false);
        studentNameField.setFocusable(false);
        schoolField.setFocusable(false);
        departmentField.setFocusable(false);
        levelField.setFocusable(false);
        sexField.setFocusable(false);
        isbnField.setFocusable(false);
        bookTitleField.setFocusable(false);
        authorField.setFocusable(false);
        issueDateField.setFocusable(false);
        returnDateField.setFocusable(false);

        String isbn = getIntent().getStringExtra("isbn");
        String regNumber = getIntent().getStringExtra("regNumber");

        if (isbn!=null && regNumber!=null){
             transacntionTask = new RetrieveDetailsForTransacntionTask(this,isbn,regNumber);
            transacntionTask.execute();
        }


        issueDateButton.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        month+=1;
                        if (month<10 && dayOfMonth<10){
                            date1 = year+"-0" + (month) + "-0"+ dayOfMonth;
                            issueDateField.setText(date1);
                        }
                        else if (month<10 && dayOfMonth>9){
                            date1 = year+"-0" + (month) + "-"+ dayOfMonth;
                            issueDateField.setText(date1);
                        }
                        else if (dayOfMonth < 10 && month>9){
                            date1 = year+"-" + (month) + "-0"+ dayOfMonth;
                            issueDateField.setText(date1);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });

        returnDateButton.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        month+=1;
                        if (month<10 && dayOfMonth<10){
                            date2 = year+"-0" + (month) + "-0"+ dayOfMonth;
                            returnDateField.setText(date2);
                        }
                        else if (month<10 && dayOfMonth>9){
                            date2 = year+"-0" + (month) + "-"+ dayOfMonth;
                            returnDateField.setText(date2);
                        }
                        else if (dayOfMonth < 10 && month>9){
                            date2 = year+"-" + (month) + "-0"+ dayOfMonth;
                            returnDateField.setText(date2);
                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });


        issueButton.setOnClickListener(v -> {
            if (    !issueDateField.getText().toString().isEmpty() &&
                    !returnDateField.getText().toString().isEmpty() &&
                    !reg_numberField.getText().toString().isEmpty() &&
                    !isbnField.getText().toString().isEmpty()
                ){
                task = new IssueTransanctionTask(v.getContext(),date1,date2);
                task.execute();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Empty fields");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Okay", (dialog, which) -> {
                    dialog.dismiss();
                });
                alertDialog.show();
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
        if (item.getItemId() == R.id.add_item){
            showDialog(this);
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (transacntionTask!=null){
            transacntionTask.cancel(true);
        }
        if (task != null){
            task.cancel(true);
        }
        super.onBackPressed();
    }

    public void showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_add_isbn_regnumber,null);

        builder.setTitle("Issue book");
        builder.setView(view);
        EditText editText = view.findViewById(R.id.rgField);
        EditText editText1 = view.findViewById(R.id.isbField);
        builder.setPositiveButton("Add", (dialog1, which) -> {
            String r1 = editText.getText().toString();
            String r2 = editText1.getText().toString();
            if (!r1.isEmpty() && isDigit(r2)){
                RetrieveDetailsForTransacntionTask transacntionTask = new RetrieveDetailsForTransacntionTask(this,r2,r1);
                transacntionTask.execute();
            }
            else {
                Toast.makeText(this,"Invalid data", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });

        AlertDialog dialog =  builder.create();
        dialog.show();
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
     * This class retrieves the details of student and book for the issueing of the desired
     * book.
     */
    class RetrieveDetailsForTransacntionTask extends AsyncTask<Void,Void,Void>{

        String isbn;
        int regNumber;
        String result;
        Context context;
        ProgressDialog progressDialog;
        BookInfosTransaction transaction1;
        StudentInfosTransaction transaction2;
        public RetrieveDetailsForTransacntionTask(Context context, String isbn, String regNumber) {
            this.isbn = isbn;
            this.regNumber = getUUID(regNumber);
            this.context  = context;

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
                transaction1 = LiberianAuth.retrieveBookDetail(tableName,Long.parseLong(isbn));
                transaction2 = LiberianAuth.retrieveStudentDetails(tableName,regNumber);
            }catch (Exception e){
                result = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();

            if ((transaction1!=null  && !transaction1.getAuthor().isEmpty()) &&
                    ( transaction2!=null && !transaction2.getName().isEmpty())){
                reg_numberField.setText(transaction2.getNumber());
                studentNameField.setText(transaction2.getName());
                schoolField.setText(transaction2.getSchool());
                departmentField.setText(transaction2.getDepartment());
                levelField.setText(String.valueOf(transaction2.getLevel()));
                sexField.setText(transaction2.getSex());
                isbnField.setText(String.valueOf(transaction1.getIsbn()));
                bookTitleField.setText(transaction1.getBooktitle());
                authorField.setText(transaction1.getAuthor());
            }
            else{
                Toast.makeText(FinalIssueActivity.this,"No record found or network problem",Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * This class issues the book out of the library and
     * stores it in the database.
     */
    class IssueTransanctionTask extends AsyncTask<Void,Void,Void>{

        String result;
        Context context;
        String date1;
        String date2;
        ProgressDialog progressDialog;
        AlertDialog dialog;
        public IssueTransanctionTask(Context context, String date1, String date2) {
            this.context = context;
            this.date1 = date1;
            this.date2 = date2;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Issuing book");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!LiberianAuth.recordExist(tableName,
                        getUUID(reg_numberField.getText().toString()),
                        Long.parseLong(isbnField.getText().toString()))){
                    result = LiberianAuth.issueBook(tableName,getUUID(reg_numberField.getText().toString()),
                            Long.parseLong(isbnField.getText().toString()),
                            date1,
                            date2
                    );
                }
                else{
                    result = "Failed";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            }else{
                dialog.setMessage("Ooops network problem");
                dialog.show();
            }

            reg_numberField.setText("");
            studentNameField.setText("");
            schoolField.setText("");
            departmentField.setText("");
            levelField.setText("");
            sexField.setText("");
            isbnField.setText("");
            bookTitleField.setText("");
            authorField.setText("");
            issueDateField.setText("");
            returnDateField.setText("");
        }
    }

    public int getUUID(String regNumber){
        int result=0;
        char[] number = regNumber.toCharArray();
        for ( char n: number) {
            result += n;
        }
        return  result;
    }
}