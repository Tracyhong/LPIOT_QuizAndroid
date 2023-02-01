package iut.paci.classroomcommunity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static User user;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        String nom = bundle.getString("nom");
        this.user = (User) bundle.getSerializable("user");
        Toast t = Toast.makeText(MainActivity.this, "Bienvenue " + user.getName(), Toast.LENGTH_SHORT);
        t.show();

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), QuizActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        this.gridView = (GridView)findViewById(R.id.gridView);
        ArrayList<District> arrayDistrict = new ArrayList<>();
        for(int i=1; i<=20;i++){
            arrayDistrict.add(new District("district"+i,i));
        }
      /* afficher les noms dans la grid view
      ArrayAdapter<District> arrayAdapter
                = new ArrayAdapter<District>(this, android.R.layout.simple_list_item_1 , arrayDistrict);
        gridView.setAdapter(arrayAdapter);*/
        DistrictAdapter adapter =
                new DistrictAdapter(MainActivity.this,
                        R.layout.district_item,
                        arrayDistrict);
        gridView.setAdapter(adapter);

    }

    public static User getUser() {
        return user;
    }
}