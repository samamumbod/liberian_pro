package com.record_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.liberianpro.R;

public class FinalRecordActivity extends AppCompatActivity {

    int counter =1;
    EditText isbnField;
    EditText bookTitileField;
    EditText authorField;
    EditText publishYearField;
    EditText copiesField;
    Spinner bookCategorySpinner;
    ImageButton decrementButton;
    ImageButton incrementButton;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_record);
        isbnField = findViewById(R.id.isbn_field);
        bookTitileField = findViewById(R.id.book_title_field);
        authorField = findViewById(R.id.author_field);
        publishYearField = findViewById(R.id.publish_year_field);
        bookCategorySpinner = findViewById(R.id.spinner);
        copiesField = findViewById(R.id.copies_field);
        decrementButton = findViewById(R.id.decrement_button);
        incrementButton = findViewById(R.id.increment_button);
        saveButton = findViewById(R.id.save_button);


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
            return true;
        }
        return false;
    }


    /**
     * This method saves the book information into the database of the institute.
     */
    class SaveBookToDatabaseTask extends AsyncTask<Void,Void,Void>{

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