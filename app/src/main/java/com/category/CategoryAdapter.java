package com.category;

import android.content.Context;
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

import com.liberian.auth.CategoryJson;
import com.liberian.auth.LiberianAuth;
import com.liberianpro.R;

import java.io.IOException;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    List<CategoryJson> categoryList;

    public CategoryAdapter(List<CategoryJson> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_layout,parent,false);
        return (new CategoryHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.categoryTextView.setText(categoryList.get(position).getCategory());

        holder.delete.setOnClickListener(v -> {
            DeleteTask task = new DeleteTask(v.getContext(), categoryList.get(position).getCategory());
            task.execute();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



    class CategoryHolder extends RecyclerView.ViewHolder{

        TextView categoryTextView;
        ImageButton delete;
        ConstraintLayout constraintLayout;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView =  itemView.findViewById(R.id.textView5);
            constraintLayout = itemView.findViewById(R.id.category_holder);
            delete = itemView.findViewById(R.id.imageButton);
        }
    }

    class DeleteTask extends AsyncTask<Void,Void,Void> {

        Context context;
        String item;
        String result;

        public DeleteTask(Context context, String item) {
            this.context = context;
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Removing book category",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                result = LiberianAuth.removeCategory("uba", item);
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
                Toast.makeText(context,"Book category removed.",Toast.LENGTH_SHORT).show();
            }
            else if (result.equals("error")){
                Toast.makeText(context,"Ooops network problem.",Toast.LENGTH_SHORT).show();
            }

            CategoryActivity.restart(context);
        }
    }
}
