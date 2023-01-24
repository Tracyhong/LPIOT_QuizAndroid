package iut.paci.classroomcommunity;


import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class DistrictAdapter extends ArrayAdapter {
    Activity activity;
    ArrayList<District> items;
    int customViewResourceId;

    public DistrictAdapter(Activity activity, int customViewResourceId, ArrayList<District> items) {
        super(activity, customViewResourceId,items);
        this.customViewResourceId = customViewResourceId;
        this.items = items;
        this.activity=activity;
        System.out.println("ADAPTER");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View layout = convertView;
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(customViewResourceId,parent, false);
        }
        TextView nameTV = (TextView) layout.findViewById(R.id.districtTV);
//        nameTV.setText(items.get(position).getName());
        nameTV.setText(String.valueOf(items.get(position).getIdDistrict()));
        ImageView flagIV = (ImageView) layout.findViewById(R.id.districtIMG);
//        R.drawable.getClass().getField("pic" + i).getInt(R.drawable);
        flagIV.setImageResource(activity.getResources().getIdentifier(items.get(position).getImg(), "drawable", activity.getPackageName()));  //.setImageResource(getResources().getIdentifier(items.get(position).getImg(), "drawable", getPackageName()));
//        flagIV.setImageResource(items.get(position).getIdDistrict());
        dialogClick(layout, items.get(position).getIdDistrict(),items.get(position).getName());
        return layout;
    }

    void dialogClick(View layout, int idDistrict, String nameDistrict){
        ImageView img = (ImageView) layout.findViewById(R.id.districtIMG);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Commencer le quiz ?");
                builder.setMessage("Quiz sur "+nameDistrict);
                builder.setPositiveButton("Yes"
                        , new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent(activity, QuizActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", MainActivity.getUser());
                                bundle.putSerializable("idDistrict", idDistrict);
                                intent.putExtras(bundle);
                                activity.startActivity(intent);

                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) { dialog.dismiss(); }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}
