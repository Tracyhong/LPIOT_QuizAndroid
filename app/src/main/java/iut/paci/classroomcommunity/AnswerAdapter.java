package iut.paci.classroomcommunity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AnswerAdapter extends ArrayAdapter {
    Activity activity;
    ArrayList<Answer> items;
    int customViewResourceId;

    public AnswerAdapter(Activity activity, int customViewResourceId, ArrayList<Answer> items) {
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
        Button response = (Button) layout.findViewById(R.id.response);
        response.setText(String.valueOf(items.get(position).getTexte()));

        btnClick(layout, items.get(position).getIsReponse());
        return layout;
    }

    void btnClick(View layout, boolean isReponse){
        Button response = (Button) layout.findViewById(R.id.response);
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //changer la couleur en fonction de is reponse
                if(isReponse){
                    response.setBackgroundColor(response.getContext().getResources().getColor(R.color.green));
                }
                else{
                    response.setBackgroundColor(response.getContext().getResources().getColor(R.color.red));

                }

            }
        });
    }
}
