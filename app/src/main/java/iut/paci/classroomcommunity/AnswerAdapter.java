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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class AnswerAdapter extends ArrayAdapter {
    Activity activity;
    ArrayList<Answer> items;
    int customViewResourceId;

    public AnswerAdapter(Activity activity, int customViewResourceId, ArrayList<Answer> items) {
        super(activity, customViewResourceId, items);
        this.customViewResourceId = customViewResourceId;
        this.items = items;
        this.activity = activity;
        System.out.println("ADAPTER");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(customViewResourceId, parent, false);
        }
        Button response = (Button) layout.findViewById(R.id.response);
        response.setText(String.valueOf(items.get(position).getTexte()));

        btnClick(layout, items.get(position).getIsReponse());
        return layout;
    }

    void btnClick(View layout, boolean isReponse) {
        Button response = (Button) layout.findViewById(R.id.response);
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView gridView = QuizActivity.getGridView();
                final int size = gridView.getChildCount();
                for (int i = 0; i < size; i++) {
                    ViewGroup gridChild = (ViewGroup) gridView.getChildAt(i);
                    int childSize = gridChild.getChildCount();
                    for (int k = 0; k < childSize; k++) {
                        if (gridChild.getChildAt(k) instanceof Button) {
                            gridChild.getChildAt(k).setClickable(false);
                            System.out.println("********ITEMS **********" + items.get(i).toString());
                            if(items.get(i).getIsReponse()){
                                gridChild.getChildAt(k).setBackgroundColor(gridChild.getChildAt(k).getContext().getResources().getColor(R.color.green));
                            } else {
                                gridChild.getChildAt(k).setBackgroundColor(gridChild.getChildAt(k).getContext().getResources().getColor(R.color.red));
                            }
                        }
                    }
                }
            }
        });
    }
}
