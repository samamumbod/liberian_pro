package com.settings;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.liberianpro.R;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsHolder> {

    List<Setting> settingList;

    public SettingsAdapter(List<Setting> settingList) {
        this.settingList = settingList;
    }

    @NonNull
    @Override
    public SettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.settings_layout,parent,false);
        return (new SettingsHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsHolder holder, int position) {
        if (position==0){
            holder.constraintLayout.setEnabled(false);
        }
        holder.title.setText(settingList.get(position).getTitle());
        holder.subtitle.setText(settingList.get(position).getSubtiltle());
        holder.constraintLayout.setOnClickListener(v -> {
            switch (position){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    String[] recipients={"sama@mshelter.tech"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    emailIntent.setType("text/plain");
                    v.getContext().startActivity(emailIntent);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    class SettingsHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subtitle;
        ConstraintLayout constraintLayout;

        public SettingsHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView3);
            subtitle = itemView.findViewById(R.id.textView4);
            constraintLayout = itemView.findViewById(R.id.settomg_container);

        }
    }

}
