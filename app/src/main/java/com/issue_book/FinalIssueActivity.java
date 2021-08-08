package com.issue_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.liberianpro.R;

import java.time.ZoneId;

public class FinalIssueActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_issue);
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
            return true;
        }
        return false;
    }

    /**
     * This class retrieves the details of student and book for the issueing of the desired
     * book.
     */
    class RetrieveDetailsForTransacntionTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }


    /**
     * This class issues the book out of the library and
     * stores it in the database.
     */
    class IssueTransanctionTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }


}