package lordslightoftheworld.com.coursmodeprojet.Presenter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lordslightoftheworld.com.coursmodeprojet.Model.CRUDProduct;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Model.User;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.CoursModeProjetApi;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class manage HomeActivity data
 */
public class HomePresenter implements HomeView.IPresenter {

    // Interface reference
    private HomeView.IHome iHome;

    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    /**
     * Load home activity data
     * @param context
     */
    @Override
    public void loadHomeData(Context context) {
        try {
            if(iHome != null && context != null){
                iHome.findWidgetsById();
                iHome.widgetsEvents();

                if(CommonPresenter.isMobileConnected(context)){
                    loadAllProducts(context, CommonPresenter.getRetrofitInstance(context));
                }
                else{
                    CRUDProduct crudProduct = new CRUDProduct(context);
                    ArrayList<Product> products = crudProduct.list();
                    if(products != null && products.size() > 0){
                        iHome.modifyToolbarTitle(context.getString(R.string.lb_our_produts));
                        loadDataInRecyclerView(context, products, false, null);
                        View view = CommonPresenter.getViewInTermsOfContext(context);
                        CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_no_network));
                    }
                    else{
                        iHome.modifyToolbarTitle(context.getString(R.string.lb_no_network));
                        iHome.modifyInfoMessage(context.getString(R.string.lb_no_network_find), context.getResources().getColor(R.color.colorOnBackground));
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->loadHomeData() : "+ex.getMessage());
        }
    }

    public static boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    /**
     * Retrieve user action
     * @param view
     */
    @Override
    public void retrieveUserAction(View view) {
        try {
            Context context = view.getContext();
            if(iHome != null){
                int widgetId = view.getId();
                switch (widgetId){
                    // User icon (Registration / Login)
                    case R.id.imageRight:
                        String userIsConnected = CommonPresenter.getDavaFromSharedPreferences(context, CommonPresenter.USER_IS_CONNECTED);
                        if(userIsConnected != null && userIsConnected.equalsIgnoreCase("YES")){
                            getImageRightMenu(view, true);
                        }
                        else{
                            getImageRightMenu(view, false);
                        }
                        break;

                    // Search floating button
                    case R.id.searchFB:
                        boolean showSearchView = iHome.isSearchInputLayoutVisible();
                        iHome.searchInputLayoutVisibility(showSearchView ? View.VISIBLE : View.GONE);
                        iHome.modifyFloatingButtonIcon(showSearchView ? R.drawable.ic_close_32dp : R.drawable.ic_search_32dp);
                        if(!showSearchView){ iHome.modifySearchFieldData(""); }
                        break;
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    /**
     * Retrieve user action
     * @param searchText
     */
    @Override
    public void retrieveUserAction(Context context, String searchText) {
        try {
            // WHen text field value change
            if (iHome != null && searchText != null){
                ArrayList<Product> products = new ArrayList<>();
                if(searchText.trim().isEmpty()){
                    products = iHome.getAllProductsData();
                    searchText = null;
                }
                else{
                    ArrayList<Product> productsData = iHome.getAllProductsData();
                    for (Product product : productsData){
                        String title = product.getTitle().trim().toLowerCase();
                        if(title.indexOf(searchText.trim().toLowerCase()) >= 0){
                            products.add(product);
                        }
                    }
                }
                loadDataInRecyclerView(context, products, false, searchText);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : "+ex.getMessage());
        }
    }

    /**
     * Launch productActivity
     * @param jsonProduct
     */
    @Override
    public void launchProductActivity(Context context, String jsonProduct) {
        try {
            if (iHome != null){
                iHome.launchProductActivity(jsonProduct);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->launchProductActivity() : "+ex.getMessage());
        }
    }

    /**
     * On activity result code
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        try {
            if(iHome != null && context != null && data != null){
                if(requestCode == CommonPresenter.USER_ACTIVITY_RETURN_CODE){
                    View view = CommonPresenter.getViewInTermsOfContext(context);
                    String jsonUser = data.getStringExtra(CommonPresenter.USER_CONNECTED_INFOS);
                    CommonPresenter.saveDataInSharedPreferences(context, CommonPresenter.USER_CONNECTED_INFOS, jsonUser);
                    CommonPresenter.saveDataInSharedPreferences(context, CommonPresenter.USER_IS_CONNECTED, "YES");
                    getImageRightResource(context);
                    User mUser = CommonPresenter.getUserFromJSON(jsonUser);
                    if(mUser.getMessage() != null && !mUser.getMessage().equalsIgnoreCase("null")){
                        CommonPresenter.showSnackBarMessage(view, mUser.getMessage());
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->onActivityResult() : "+ex.getMessage());
        }
    }

    /**
     * Display all product
     * @param context
     * @param retrofit
     */
    private void loadAllProducts(final Context context, Retrofit retrofit){
        try {
            if(retrofit != null && iHome != null && context != null){
                // View visibility
                iHome.floatingButtonVisibility(View.GONE);
                iHome.progressBarVisibility(View.VISIBLE);
                iHome.modifyToolbarTitle(context.getString(R.string.lb_loading));
                // Create interface objet
                CoursModeProjetApi modeProjetApi = retrofit.create(CoursModeProjetApi.class);
                // Retrieve prototype method from api
                Call<List<Product>> call = modeProjetApi.getAllProducts();
                call.enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        iHome.progressBarVisibility(View.GONE);
                        if(!response.isSuccessful()){
                            iHome.modifyToolbarTitle(context.getString(R.string.lb_traitement_error));
                            iHome.modifyInfoMessage(context.getString(R.string.lb_traitement_error), context.getResources().getColor(R.color.colorError));
                            return;
                        }
                        //--
                        iHome.modifyToolbarTitle(context.getString(R.string.lb_our_produts));
                        iHome.floatingButtonVisibility(View.VISIBLE);
                        iHome.toolbarVisibility(View.INVISIBLE, View.VISIBLE);
                        ArrayList<Product> products = (ArrayList<Product>)response.body();
                        loadDataInRecyclerView(context, products, true, null);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        iHome.progressBarVisibility(View.GONE);
                        iHome.modifyInfoMessage(t.getMessage(), context.getResources().getColor(R.color.colorError));
                        iHome.modifyToolbarTitle(context.getString(R.string.lb_load_error));
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->loadAllProducts() : "+ex.getMessage());
        }
    }

    /**
     * Load data in the recyclerView
     * @param products
     * @param saveData
     */
    private void loadDataInRecyclerView(Context context, ArrayList<Product> products, boolean saveData, String keyWord){
        try {
            if(products != null && iHome != null && context != null){
                int numberColumn = CommonPresenter.getColumnNumber(context);
                iHome.loadProductsData(products, numberColumn, keyWord);
                getImageRightResource(context);
                if(saveData){
                    iHome.saveAllProductsData(products);
                    // Save in database
                    int totalToSave = 0;
                    CRUDProduct crudProduct = new CRUDProduct(context);
                    crudProduct.deleteAll();
                    for (Product product : products){
                        totalToSave++;
                        if(totalToSave <= 15){
                            crudProduct = new CRUDProduct(context);
                            crudProduct.add(product);
                        }
                    }
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->loadDataInRecyclerView() : "+ex.getMessage());
        }
    }

    /**
     * Get user status : connected, disconnected, never connected
     * @param context
     */
    private void getImageRightResource(Context context){
        if(iHome != null && context != null){
            String userIsConnected = CommonPresenter.getDavaFromSharedPreferences(context, CommonPresenter.USER_IS_CONNECTED);
            if(userIsConnected != null && userIsConnected.equalsIgnoreCase("YES")){
                iHome.changeImageRightResource(R.drawable.ic_user_connected_32dp);
                if(!CommonPresenter.isMobileConnected(context)){
                    View view = CommonPresenter.getViewInTermsOfContext(context);
                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_no_network));
                }
            }
            /*else if(userIsConnected != null && userIsConnected.equalsIgnoreCase("NO")){
                iHome.changeImageRightResource(R.drawable.ic_user_disconnected_32dp);
            }*/
            else{
                iHome.changeImageRightResource(R.drawable.ic_more_32dp);
            }
        }
    }

    /**
     * Get image right menu
     * @param view
     */
    private void getImageRightMenu(final View view, boolean isUserConnected) {
        try {
            if(iHome != null && view != null){
                final Context context = view.getContext();
                PopupMenu popup = new PopupMenu(context, view);
                // Display menu icon
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                //--
                popup.getMenuInflater().inflate(isUserConnected ? R.menu.home_menu_connected : R.menu.home_menu_disconnected, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            // Create new user account
                            case R.id.home_menu_create_account:
                                iHome.launchUserActivity(context.getString(R.string.lb_create_account));
                                break;

                             // Login
                            case R.id.home_menu_login:
                                iHome.launchUserActivity(context.getString(R.string.lb_login));
                                break;

                            // Show user info
                            case R.id.home_menu_show_infos:
                                iHome.launchUserActivity(context.getString(R.string.lb_show_info));
                                break;

                            // Products favorites
                            case R.id.home_menu_favorites_products:
                                iHome.launchFavoriteActivity();
                                break;

                            // Logout
                            case R.id.home_menu_login_out:
                                if(CommonPresenter.isMobileConnected(context)){
                                    CommonPresenter.saveDataInSharedPreferences(context, CommonPresenter.USER_IS_CONNECTED, "NO");
                                    getImageRightResource(context);
                                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_disconnect_success));
                                }
                                else{
                                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_no_network_find));
                                }
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "HomePresenter-->getImageRightMenu() : "+ex.getMessage());
        }
    }
}
