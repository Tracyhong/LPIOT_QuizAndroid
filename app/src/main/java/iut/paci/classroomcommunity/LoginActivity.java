package iut.paci.classroomcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast t = Toast.makeText(LoginActivity.this, "Bonjour", Toast.LENGTH_SHORT);
        t.show();
        Button b = (Button) findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final EditText username = (EditText) findViewById(R.id.username);
                final EditText password = (EditText) findViewById(R.id.password);

                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                if(!usernameText.equals("") && !passwordText.equals("")) {
                    /*BDconnexion.login(LoginActivity.this, usernameText, passwordText)
                            .setCallback(new FutureCallback<JsonResponse>() {

                                // onCompleted is executed on ui thread
                                @Override
                                public void onCompleted(Exception e, JsonResponse jsonResponse) {
                                    if (jsonResponse.ok) {
                                        User user = new User(usernameText,passwordText);
                                        Log.i("tag", usernameText);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        Bundle bundle = new Bundle();
//                                        bundle.putString("nom",usernameText);
                                        bundle.putSerializable("user", user);
                                        intent.putExtras(bundle);
                                        Toast t = Toast.makeText(LoginActivity.this, usernameText, Toast.LENGTH_SHORT);
                                        t.show();
                                        startActivity(intent);
                                    } else {
                                        Toast t = Toast.makeText(LoginActivity.this, "INCONNU !", Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                }
                            });
                }*/
                        if(BDconnexion.login(LoginActivity.this, usernameText, passwordText)){
                            User user = new User(usernameText,passwordText);
                            Log.i("tag", usernameText);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
        //                bundle.putString("nom",usernameText);
                            bundle.putSerializable("user", user);
                            intent.putExtras(bundle);
                            Toast t = Toast.makeText(LoginActivity.this, usernameText, Toast.LENGTH_SHORT);
                            t.show();
                            startActivity(intent);
                        }
                        else{
                            Toast t = Toast.makeText(LoginActivity.this, "INCONNU !", Toast.LENGTH_SHORT);
                            t.show();
                        }
                }
                else{
                    Toast t = Toast.makeText(LoginActivity.this, "CONNECTE TOI !", Toast.LENGTH_SHORT);
                    t.show();
                }

            }
        });
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast t = Toast.makeText(MainActivity.this, "Bonjour", Toast.LENGTH_SHORT);
        t.show();
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i("tag", "bouton cliqué!!!");
                setContentView(R.layout.activity_login);
            }
        });*/
    }
}