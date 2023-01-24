package iut.paci.classroomcommunity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class BDconnexion {

    private static String bdd = "http://10.10.12.156:80/lpiot/api/";
    private static String key = "iot1235";
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
        final boolean[] loginExist = {false};
        if(checkConnection(ctx)) {
            String url = bdd + "login.php?key=" + key + "&username=" + username + "&password=" + password;
            System.out.println(url);
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
                                loginExist[0] = false;
                            }
                            else {
                                Toast.makeText(ctx, "LOGIN", Toast.LENGTH_SHORT).show();
                                loginExist[0] = true;
                            }
                        }
                    });
        }
        return loginExist[0];
    }

}
