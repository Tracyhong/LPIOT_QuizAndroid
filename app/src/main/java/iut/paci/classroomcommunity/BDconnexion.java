package iut.paci.classroomcommunity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class BDconnexion {

    private static String bdd = "http://10.10.12.156:80/lpiot/api/";
    private static String key = "iot1235";
    private static boolean loginExist=false;
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
        System.out.println("-------------USER APP"+username + password);
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
                                loginExist=false;
                            }
                            else {
                                Toast.makeText(ctx, "LOGIN", Toast.LENGTH_SHORT).show();
                                loginExist=true;
                            }
                        }
                    });
        }
        System.out.println(loginExist);
        return loginExist;
    }

}
