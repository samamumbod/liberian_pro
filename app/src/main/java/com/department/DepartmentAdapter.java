package com.department;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.IOException;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentHolder>{

    List<Department> departmentList;

    public DepartmentAdapter(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @NonNull
    @Override
    public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.department_layout,parent,false);
        return (new DepartmentAdapter.DepartmentHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentHolder holder, int position) {
        holder.departmentTextView.setText(departmentList.get(position).getDepartment());
        holder.delete.setOnClickListener(v->{
            DeleteTask task = new DeleteTask(v.getContext(), departmentList.get(position).getDepartment());
            task.execute();
        });
    }


    @Override
    public int getItemCount() {
        return departmentList.size();
    }


    class DepartmentHolder extends RecyclerView.ViewHolder{
        TextView departmentTextView;
        ImageButton delete;

        public DepartmentHolder(@NonNull View itemView) {
            super(itemView);
            departmentTextView = itemView.findViewById(R.id.textView20);
            delete = itemView.findViewById(R.id.imageButton5);
        }
    }


    class DeleteTask extends AsyncTask<Void,Void,Void> {

        Context context;
        String item;
        String result;
        String tableName;

        public DeleteTask(Context context, String item) {
            this.context = context;
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences preferences = context.getSharedPreferences("Liberian",context.MODE_PRIVATE);
            tableName = preferences.getString("table","");
            Toast.makeText(context,"Removing department",Toast.LENGTH_SHORT).show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                result = LiberianAuth.removeDepartments(tableName, item);
            }
            catch (IOException e) {
                result = "error";
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (result.equals("Success")){
                Toast.makeText(context,"Department removed.",Toast.LENGTH_SHORT).show();
                DepartmentActivity.restart(context);
            }
            else if (result.equals("Failed")){
                Toast.makeText(context,"Department in use.",Toast.LENGTH_SHORT).show();
            }
            else if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
