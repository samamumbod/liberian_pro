package com.liberianpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.category.CategoryActivity;
import com.issue_book.IssueBookActivity;
import com.school.SchoolActivity;
import com.settings.SettingsActivity;
import com.signin.SignInActivity;
import com.record_book.RecordBookActivity;
import com.return_book.ReturnBookActivity;
import com.signature.SignatureActivity;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    List<Option> options;

    public MainAdapter(List<Option> options) {
        this.options = options;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.options_layout,viewGroup,false);
        return (new MainHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder mainHolder, int i) {

        mainHolder.imageView.setImageResource(options.get(i).getImage());
        mainHolder.textView.setText(options.get(i).getLabel());
        mainHolder.constraintLayout.setOnClickListener(v -> {
            switch (options.get(i).getLabel()){
                case "Record book":
                    v.getContext().startActivity(new Intent(v.getContext(), RecordBookActivity.class));
                    break;
                case "Issue book":
                    v.getContext().startActivity(new Intent(v.getContext(), IssueBookActivity.class));
                    break;
                case "Return book":
                    v.getContext().startActivity(new Intent(v.getContext(), ReturnBookActivity.class));
                    break;
                case "Category":
                    v.getContext().startActivity(new Intent(v.getContext(), CategoryActivity.class));
                    break;
                case "Signature":
                    v.getContext().startActivity(new Intent(v.getContext(), SignatureActivity.class));
                    break;
                case "Settings":
                    v.getContext().startActivity(new Intent(v.getContext(), SettingsActivity.class));
                    break;
                case "Log out":
                    SharedPreferences preferences = v.getContext().getSharedPreferences("Liberian",v.getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("activity","signin");
                    editor.apply();
                    v.getContext().startActivity(new Intent(v.getContext(), SignInActivity.class));
                    break;
                case "School":
                    v.getContext().startActivity(new Intent(v.getContext(), SchoolActivity.class));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class MainHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public ConstraintLayout constraintLayout;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView2);
            constraintLayout = itemView.findViewById(R.id.container);
        }
    }
}
