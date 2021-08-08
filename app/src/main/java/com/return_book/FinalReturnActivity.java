package com.return_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.liberianpro.R;

public class FinalReturnActivity extends AppCompatActivity {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_return);

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
    class RetrieveDetailsForTransacntionTask extends AsyncTask<Void,Void,Void> {

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
    class ReturnTransanctionTask extends AsyncTask<Void,Void,Void>{

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