package lordslightoftheworld.com.coursmodeprojet.Presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Hashtable;

import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Model.User;
import lordslightoftheworld.com.coursmodeprojet.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class contains common data
 */
public class CommonPresenter {

    // Infos Last user connected
    public static final String USER_IS_CONNECTED = "USER_IS_CONNECTED";
    public static final String USER_CONNECTED_INFOS = "USER_CONNECTED_INFOS";
    public static final String USER_ACTIVITY_TITLE = "USER_ACTIVITY_TITLE";
    public static final String USER_ACTIVITY_METHOD = "USER_ACTIVITY_METHOD";
    public static final int USER_ACTIVITY_RETURN_CODE = 17;
    // Detail product
    public static final String DETAIL_PRODUCT = "DETAIL_PRODUCT";
    // Detail product favorite
    public static final String FAVORITE_ACTIVITY_TITLE = "FAVORITE_ACTIVITY_TITLE";
    public static final String PRODUCT_FAVORITE_DATA = "PRODUCT_FAVORITE_DATA";

    /**
     * Create GSon object
     * @return
     */
    public static Gson createGsonObject(){
        return new GsonBuilder().serializeNulls().create();
    }

    /**
     * Get product object from json
     * @param json
     * @return
     */
    public static Product getProductFromJSON(String json){
        try {
            Type type = new TypeToken<Product>(){}.getType();
            Gson gson = createGsonObject();
            return gson.fromJson(json, type);
        }
        catch (Exception ex){}
        return null;
    }

    /**
     * Get product favorite object from json
     * @param json
     * @return
     */
    public static Favorite getFavoriteFromJSON(String json){
        try {
            Type type = new TypeToken<Favorite>(){}.getType();
            Gson gson = createGsonObject();
            return gson.fromJson(json, type);
        }
        catch (Exception ex){}
        return null;
    }

    /**
     * Get comment object from json
     * @param json
     * @return
     */
    public static Comment getCommentFromJSON(String json){
        try {
            Type type = new TypeToken<Comment>(){}.getType();
            Gson gson = createGsonObject();
            return gson.fromJson(json, type);
        }
        catch (Exception ex){}
        return null;
    }

    /**
     * Show message in the snack bar
     * @param view
     * @param message
     */
    public static void showSnackBarMessage(View view, String message){
        try {
            if(view != null && message != null){
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        catch (Exception ex){}
    }

    /**
     * Method to get view in terms of the context
     * @param context
     * @return
     */
    public static View getViewInTermsOfContext(Context context){
        try {
            Activity activity = getActivityFromContext(context);
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            return rootView;
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CommonPresenter-->getViewInTermsOfContext() : "+ex.getMessage());
        }
        return null;
    }

    /**
     * Get product object from json
     * @param json
     * @return
     */
    public static User getUserFromJSON(String json){
        try {
            Type type = new TypeToken<User>(){}.getType();
            Gson gson = createGsonObject();
            return gson.fromJson(json, type);
        }
        catch (Exception ex){}
        return null;
    }

    /**
     * Change key word color in the text
     * @param text
     * @param keyWord
     * @param color
     * @return
     */
    public static Spannable colorKeyWordInText(String text, String keyWord, String color){
        String mText = text;
        String mKeyWord = keyWord;
        Spannable wordToSpan = new SpannableString(mText);
        int longueurMotClef = mKeyWord.length();
        for (int i = -1; (i = mText.toLowerCase().indexOf(mKeyWord.toLowerCase(), i + 1)) != -1; ) {
            wordToSpan.setSpan(new ForegroundColorSpan(Color.parseColor(color)), i, i+longueurMotClef, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (int i = -1; (i = replaceAccents(mText.toLowerCase()).indexOf(mKeyWord.toLowerCase(), i + 1)) != -1; ) {
            wordToSpan.setSpan(new ForegroundColorSpan(Color.parseColor(color)), i, i+longueurMotClef, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(isInteger(keyWord)){
            for (int i = -1; (i = replaceAccents(mText.toLowerCase().replace(" 0", "0")).indexOf(mKeyWord.toLowerCase().replace(" ", ""), i + 1)) != -1; ) {
                wordToSpan.setSpan(new ForegroundColorSpan(Color.parseColor(color)), i, i+longueurMotClef, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return wordToSpan;
    }

    /**
     * Is string integer
     * @param s
     * @return
     */
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s.trim().replace(" ", ""));
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * Replace accents
     * @param s
     * @return
     */
    private static String replaceAccents(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    /**
     * Is mobile connected
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnected();
        }
        else{
            // Android >= 10 (API 29, ...)
            Network[] networks = connectivityManager.getAllNetworks();
            boolean hasInternet = false;
            if(networks.length > 0){
                for(Network network : networks){
                    NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
                    if(nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet = true;
                }
            }
            return hasInternet;
        }
    }

    /**
     * Verify if email is valid
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Very if user has permission to read or write file
     * @param context
     * @return
     */
    public static boolean isUserHasPermissionToReadOrWriteFile(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void saveDataInSharedPreferences(Context context, String key, String dataToSave){
        try {
            if(context != null && key != null && dataToSave != null){
                SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, dataToSave);
                editor.commit(); // <=> editor.apply();
            }
        }
        catch (Exception ex){}
    }

    public static String getDavaFromSharedPreferences(Context context, String key){
        try {
            if(context != null && key != null){
                SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
                return sharedPreferences.getString(key, "");
            }
        }
        catch (Exception ex){}
        return null;
    }

    /**
     * Verify if it's a tablet
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) == android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) == android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * Get column number
     * @param context
     * @return
     */
    public static int getColumnNumber(Context context){
        boolean isTablette = isTablet(context);
        Hashtable<String, Integer> resolution = getScreenResolution(context);
        boolean isPortrait = resolution.get("width") < resolution.get("height");
        int columnNumber = 0;
        if(isTablette){ columnNumber = isPortrait ? 4 : 7; }
        else{columnNumber = isPortrait ? 2 : 4;}
        return columnNumber;
    }

    /**
     * Get activity from context
     * @param context
     * @return
     */
    public static Activity getActivityFromContext(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity)context;
        else if (context instanceof ContextWrapper)
            return getActivityFromContext(((ContextWrapper)context).getBaseContext());
        return null;
    }

    /**
     * Method to get screen resolution
     * @param context
     * @return
     */
    public static Hashtable<String, Integer> getScreenResolution(Context context) {
        Hashtable<String, Integer> dimension = new Hashtable<>();
        Activity activity = getActivityFromContext(context);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dimension.put("width", width);
        dimension.put("height", height);
        return dimension;
    }

    /**
     * Get retrofit instance
     * @param context
     * @return
     */
    public static Retrofit getRetrofitInstance(Context context){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.http_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
