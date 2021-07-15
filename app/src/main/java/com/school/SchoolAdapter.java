package com.school;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.liberian.auth.LiberianAuth;
import com.liberian.auth.SchoolJson;
import com.liberianpro.R;

import java.io.IOException;
import java.util.List;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolHoler> {

    List<SchoolJson> schoolList;



    public SchoolAdapter( List<SchoolJson> schoolList) {
        this.schoolList = schoolList;
    }

    @NonNull
    @Override
    public SchoolHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_school,parent,false);
        return (new SchoolHoler(view));
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolHoler holder, int position) {
        holder.school.setText(schoolList.get(position).getSchool());
        holder.meaning.setText(schoolList.get(position).getMeaning());
        holder.delete.setOnClickListener(v -> {
            // Task to remove Json
            RemoveItemTask task = new RemoveItemTask(v.getContext(),schoolList.get(position).getSchool(),position);
            task.execute();
        });
    }

    @Override
    public int getItemCount() {
        return schoolList.size();
    }

    public void addNewSchool(SchoolJson schoolJson){
        schoolList.add(schoolJson);
        int pos= schoolList.indexOf(schoolJson);
        notifyItemInserted(pos);
    }

    public void setSchoolList(List<SchoolJson> schoolList) {
        this.schoolList = schoolList;
    }

    class SchoolHoler extends RecyclerView.ViewHolder{

        ImageButton delete;
        TextView school;
        TextView meaning;

        public SchoolHoler(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.imageButton2);
            school = itemView.findViewById(R.id.textView6);
            meaning = itemView.findViewById(R.id.textView8);

        }
    }


    class RemoveItemTask extends AsyncTask<Void,Void,Void>{

        private String school;
        private String result;
        private int position;
        private Context context;

        public RemoveItemTask( Context context, String school, int position) {
            this.context = context;
            this.school = school;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Removing School",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                result = LiberianAuth.removeSchool("uba",school);
            } catch (IOException e) {
                result = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            switch (result) {
                case "Success":
                    Toast.makeText(context,"Schoool removed",Toast.LENGTH_SHORT).show();
                    break;
                case "error":
                    Toast.makeText(context,"Ooops network problem",Toast.LENGTH_SHORT).show();
                    break;
            }
            SchoolActivity.restart(context);
        }
    }

}


