package iut.paci.classroomcommunity;

//import static iut.paci.classroomcommunity.BDconnexion.getQuiz;

import static iut.paci.classroomcommunity.BDconnexion.getQuiz;
import static iut.paci.classroomcommunity.BDconnexion.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizActivity extends AppCompatActivity {

    Handler handler = new Handler();

    ProgressBar progressBar;
    AtomicInteger progress = new AtomicInteger();

    User user;
    int idDistrict;
    private static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mToastRunnable.run();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        String nom = bundle.getString("nom");
        this.user = (User) bundle.getSerializable("user");
        this.idDistrict = (int) bundle.getInt("idDistrict");
        TextView nomJoueurTV = (TextView) findViewById(R.id.nomJoueurTV);
        nomJoueurTV.setText(user.getName());
        TextView districtTV = (TextView) findViewById(R.id.districtTV);
        districtTV.setText("District " + idDistrict);
        System.out.println("------------------" + user.getName());

        ImageView imgAvatarJoueur = (ImageView) findViewById(R.id.imgAvatarJoueur);
        imgAvatarJoueur.setImageResource(R.drawable.avatar_joueur);
        ImageView imgAvatarAdversaire = (ImageView) findViewById(R.id.imgAvatarAdversaire);
        imgAvatarAdversaire.setImageResource(R.drawable.avatar_bot);
        JSONObject quiz = null;
        try {
            quiz = quiz(QuizActivity.this, this.idDistrict);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("********NULLLLLLLL********"+quiz.toString().equals(new JSONObject().toString()));
        int cpt = 0;
        while (quiz.toString().equals(new JSONObject().toString()) && cpt<100) {
            try {
                Thread.sleep(500);
                System.out.println("boucle");
                quiz = getQuiz();
                cpt++;
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        System.out.println("AAAAAAAAAAAAAA :      " + quiz);
        TextView questionTV = (TextView) findViewById(R.id.questionTV);
        Button response1 = (Button) findViewById(R.id.response1);
        try {
            questionTV.setText(quiz.getJSONObject("0").getString("texte"));
            int nbAnswer = quiz.getJSONArray("answer").length();
            this.gridView = (GridView) findViewById(R.id.gridView);
            ArrayList<Answer> arrayAnswer = new ArrayList<>();
            for (int i = 0; i < nbAnswer; i++) {
                String text = quiz.getJSONArray("answer").getJSONObject(i).getString("texte");
                arrayAnswer.add(new Answer(text, text.equals(quiz.getJSONArray("reponse").getJSONObject(0).getString("texte"))));
            }
            AnswerAdapter adapter =
                    new AnswerAdapter(QuizActivity.this,
                            R.layout.answer_item,
                            arrayAnswer);
            gridView.setAdapter(adapter);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        /*
        * JSONObject jsonQuiz = new JSONObject(result);
                          System.out.println(jsonQuiz);
                          map.put("question",jsonQuiz.getJSONObject("0").getString("texte"));
                          for (int i=0;i<jsonQuiz.getJSONArray("answer").length();i++) {
                              JSONObject res = jsonQuiz.getJSONArray("answer").getJSONObject(i);
                              map.put("answer",res.getString("texte"));
                          }
                          map.put("reponse",jsonQuiz.getJSONArray("reponse").getJSONObject(0).getString("texte"));
                          System.out.println(map.toString());*/
    }
    public static GridView getGridView(){
        return gridView;
    }
    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            if (progressBar.getProgress() > 0) {
//                Toast.makeText(QuizActivity.this, "Progress BAR : "+progressBar.getProgress(), Toast.LENGTH_SHORT).show();
                progress.getAndIncrement();
                progressBar.setProgress(100 - (int) progress.get());
                handler.postDelayed(this, 100);
            }
            if (progressBar.getProgress() == 1) {
                Toast.makeText(QuizActivity.this, "TROP TARD ! PERDU :(  \n  😥😩😱", Toast.LENGTH_SHORT).show();
            }

        }
    };
}