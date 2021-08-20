package com.return_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.liberian.auth.BookInfosTransaction;
import com.liberian.auth.LiberianAuth;
import com.liberian.auth.RetrieveTransactionDate;
import com.liberian.auth.StudentInfosTransaction;
import com.liberianpro.R;

import java.io.IOException;
import java.util.Calendar;

public class FinalReturnActivity extends AppCompatActivity {

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
    EditText ac_returnDateField;
    Button returnButton;
    ScrollView scrollView;

    RetrieveDetailsForTransacntionTask transacntionTask = null;
    ReturnTransanctionTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_return);

        SharedPreferences preferences = getSharedPreferences("Liberian",MODE_PRIVATE);
        tableName = preferences.getString("table","");

        scrollView = findViewById(R.id.return_book_scrollview);
        reg_numberField = findViewById(R.id.return_reg_number);
        studentNameField = findViewById(R.id.return_student_name);
        schoolField = findViewById(R.id.return_school);
        departmentField = findViewById(R.id.return_department);
        levelField = findViewById(R.id.return_level);
        sexField = findViewById(R.id.return_sex);
        isbnField = findViewById(R.id.return_isbn);
        bookTitleField = findViewById(R.id.return_book_title);
        authorField = findViewById(R.id.return_author);
        issueDateField = findViewById(R.id.return_issuedate);
        returnDateField = findViewById(R.id.return_returndate);
        ac_returnDateField = findViewById(R.id.return_ac_returndate);
        returnButton = findViewById(R.id.returnButton);

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
        ac_returnDateField.setFocusable(false);


        String isbn = getIntent().getStringExtra("isbn");
        String regNumber = getIntent().getStringExtra("regNumber");

        if (isbn!=null && regNumber!=null){
            transacntionTask = new RetrieveDetailsForTransacntionTask(this,isbn,regNumber);
            transacntionTask.execute();
        }



        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ac_returnDateField.getText().toString().isEmpty())
                {
                    task  = new ReturnTransanctionTask(FinalReturnActivity.this, reg_numberField.getText().toString(),
                            isbnField.getText().toString(),ac_returnDateField.getText().toString());
                    task.execute();
                }
                else {
                    AlertDialog  alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Empty fields");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Okay",(dialog, which) -> {dialog.dismiss();});
                    alertDialog.show();
                }
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

        if (task !=null){
            task.cancel(true);
        }
        super.onBackPressed();
    }

    public void showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.layout_add_isbn_regnumber,null);

        builder.setTitle("Return book");
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
        RetrieveTransactionDate retrieveTransactionDate;

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
                retrieveTransactionDate = LiberianAuth.retrieveTransactionDate(tableName,regNumber,Long.parseLong(isbn));
                if (retrieveTransactionDate!=null){
                    transaction1 = LiberianAuth.retrieveBookDetail(tableName,Long.parseLong(isbn));
                    transaction2 = LiberianAuth.retrieveStudentDetails(tableName,regNumber);
                }
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
                issueDateField.setText(retrieveTransactionDate.getIssuedate());
                returnDateField.setText(retrieveTransactionDate.getReturndate());

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                mMonth+=1;
                String date1 = "";
                if (mMonth<10 && mDay<10){
                    date1 = mYear+"-0" + (mMonth) + "-0"+ mDay;
                }
                else if (mMonth<10 && mDay>9){
                    date1 = mYear+"-0" + (mMonth) + "-"+ mDay;
                }
                else if (mDay < 10 && mMonth>9){
                    date1 = mYear+"-" + (mMonth) + "-0"+ mDay;
                }

                ac_returnDateField.setText(date1);
            }
            else {
                Toast.makeText(FinalReturnActivity.this,"No record found or network problem",Toast.LENGTH_LONG).show();
            }
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


    /**
     * This class issues the book out of the library and
     * stores it in the database.
     */
    class ReturnTransanctionTask extends AsyncTask<Void,Void,Void>{

        String regNumber;
        String isbn;
        String todaysDate;
        String result;
        Context context;
        ProgressDialog progressDialog;
        AlertDialog alertDialog;
        public ReturnTransanctionTask(Context context, String regNumber, String isbn, String todaysDate) {
            this.context = context;
            this.regNumber = regNumber;
            this.isbn = isbn;
            this.todaysDate = todaysDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Returning book");
            progressDialog.setCancelable(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", (dialog, which) -> dialog.dismiss());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                result = LiberianAuth.returnBook(tableName, getUUID(regNumber),Long.parseLong(isbn),todaysDate,"Returned");
            }catch (IOException e){
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
            if ("error".equals(result)){
                alertDialog.setMessage("Ooops network problem.");
                alertDialog.show();
            }
            else if ("Success".equals(result)){
                alertDialog.setMessage("Success");
                alertDialog.show();
            }
            else {
                alertDialog.setMessage("Book already returned");
                alertDialog.show();
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
            ac_returnDateField.setText("");
        }
    }
}