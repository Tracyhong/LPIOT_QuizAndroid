package iut.paci.classroomcommunity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/*import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;*/

/*import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.future.SimpleFuture;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class BDconnexion {

    private static String bdd = "http://192.168.137.1:80/lpiot/api/";
    private static String key = "iot1235";
    private static boolean loginExist=false;
    private static JSONObject quiz = new JSONObject();

    public static boolean checkConnection(Context ctx) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI &&
                        networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
// No internet connection
            return false;
        } else
            return true;
    }

    public static boolean login(Context ctx, String username, String password){

        String url = bdd + "login.php?key=" + key + "&username=" + username + "&password=" + password;
        System.out.println(url);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                client = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    String result = response.body().string();
                    System.out.println("******LOGIN********"+result);
                    loginExist=result.contains("name");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
            return loginExist;
        }


    public static JSONObject quiz(Context ctx, int idDistrict) throws InterruptedException {
        if (checkConnection(ctx)) {
          String url = bdd + "get-question.php?theme=" + idDistrict;
          System.out.println(url);

          Thread thread = new Thread(new Runnable() {

              @Override
              public void run() {
                  OkHttpClient client = new OkHttpClient();
                  OkHttpClient.Builder builder = new OkHttpClient.Builder();
                  builder.connectTimeout(30, TimeUnit.SECONDS);
                  builder.readTimeout(30, TimeUnit.SECONDS);
                  builder.writeTimeout(30, TimeUnit.SECONDS);
                  client = builder.build();
                  Request request = new Request.Builder()
                          .url(url)
                          .build();

                  try (Response response = client.newCall(request).execute()) {
                      String result = response.body().string();
                      System.out.println("******QUIZ********" + result);
                      try {
                          JSONObject jsonQuiz = new JSONObject(result);
                          System.out.println(jsonQuiz);
                          quiz=jsonQuiz;
                      } catch (JSONException ex) {
                          throw new RuntimeException(ex);
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
          });

          thread.start();
        }
        /*while(quiz.toString().isEmpty() && checkConnection(ctx)){
            Thread.sleep(500);
        }*/
       // System.out.println("*********QUIZ NULL*********" + quiz.toString().isEmpty());
        return quiz;
    }
    public static JSONObject getQuiz(){
        return quiz;
    }
        /*
            Ion.getDefault(ctx).getConscryptMiddleware().enable(false);
            Ion.with(ctx)
                    .load("GET",url)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            System.out.println("--------------------"+result);
                            if (result == null){
                                Toast.makeText(ctx, "RIEN", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ctx, "LOGIN", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jObject = new JSONObject(result);
                                    System.out.println(jObject);
                                    map.put("question","");
                                } catch (JSONException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    });
        }
        System.out.println(loginExist);
        return map;
    }*/

    private String request(String url){
        final String[] result = {""};
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                client = builder.build();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    result[0] = response.body().string();
                    System.out.println("**************"+ result[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        thread.start();
        return result[0];
    }

}
