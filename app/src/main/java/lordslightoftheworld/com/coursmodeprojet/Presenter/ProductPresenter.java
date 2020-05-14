package lordslightoftheworld.com.coursmodeprojet.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import lordslightoftheworld.com.coursmodeprojet.Model.CRUDComment;
import lordslightoftheworld.com.coursmodeprojet.Model.CRUDFavorite;
import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Model.User;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.CoursModeProjetApi;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.ProductView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class manage ProductActivity data
 */
public class ProductPresenter implements ProductView.IPresenter {

    // Interface references
    private ProductView.IProduct iProduct;

    // Constructor
    public ProductPresenter(ProductView.IProduct iProduct) {
        this.iProduct = iProduct;
    }

    /**
     * Load detail product
     * @param context
     * @param intent
     */
    @Override
    public void loadDetailProductData(Context context, Intent intent) {
        try {
            if(iProduct != null && context != null && intent != null){
                iProduct.findWidgetsById();
                iProduct.widgetsEvents();
                iProduct.toolbarImagesVisibility(View.VISIBLE, View.INVISIBLE);
                iProduct.modifyToolbarTitle(context.getString(R.string.lb_product_detail));
                String isUserConnected = CommonPresenter.getDavaFromSharedPreferences(context, CommonPresenter.USER_IS_CONNECTED);
                int sendMsgVisibility = isUserConnected != null && isUserConnected.equalsIgnoreCase("YES") ? View.VISIBLE : View.GONE;
                iProduct.sendCommentVisibility(sendMsgVisibility);
                String jsonProduct = intent.getStringExtra(CommonPresenter.DETAIL_PRODUCT);
                Product product = CommonPresenter.getProductFromJSON(jsonProduct);
                // Show toolbar favorite button
                if(isUserConnected != null && isUserConnected.equalsIgnoreCase("YES")){
                    iProduct.toolbarImagesVisibility(View.VISIBLE, View.VISIBLE);
                    iProduct.modifyToolBarImageRight(R.drawable.ic_add_to_favorite_32dp);
                }
                // Vérify if connection exists
                if(CommonPresenter.isMobileConnected(context)){
                    loadDetailProduct(context, product, CommonPresenter.getRetrofitInstance(context));
                }
                else{
                    // Verify if comments exists
                    CRUDComment crudComment = new CRUDComment(context);
                    ArrayList<Comment> comments  = crudComment.listByProductId(product.getProductId());
                    if(comments != null && comments.size() == 0){ comments = null; }
                    //--
                    loadDataInRecyclerView(context, product, comments, false, false);
                    View view = CommonPresenter.getViewInTermsOfContext(context);
                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_no_network_find));
                }
                // Change image right icon
                CRUDFavorite crudFavorite = new CRUDFavorite(context);
                boolean isProductExists = crudFavorite.isProductExists(product.getProductId());
                iProduct.changeImageRightResource(isProductExists ? R.drawable.ic_favorite_32dp : R.drawable.ic_add_to_favorite_32dp);
                // Check if data came from FavoriteActivity
                String jsonFavorite = intent.getStringExtra(CommonPresenter.PRODUCT_FAVORITE_DATA);
                if(jsonFavorite != null && !jsonFavorite.trim().isEmpty()){
                    iProduct.toolbarImagesVisibility(View.VISIBLE, View.INVISIBLE);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->loadDetailProductData() : "+ex.getMessage());
        }
    }

    /**
     * Send product comment
     * @param editText
     */
    @Override
    public void sendProductComment(View view, TextInputEditText editText, Intent intent) {
        try {
            if(editText != null && iProduct != null && intent != null){
                String messageToSend = editText.getText().toString().trim();
                if (messageToSend.isEmpty()){ return; }
                editText.setText("");
                if(CommonPresenter.isMobileConnected(view.getContext())){
                    // Retrieve product data
                    String jsonProduct = intent.getStringExtra(CommonPresenter.DETAIL_PRODUCT);
                    Product product = CommonPresenter.getProductFromJSON(jsonProduct);
                    // Retrieve comment data
                    ArrayList<Comment> comments = iProduct.getProductComments();
                    // Add new comment
                    ArrayList<Comment> newComments = new ArrayList<>();
                    String jsonUser = CommonPresenter.getDavaFromSharedPreferences(view.getContext(), CommonPresenter.USER_CONNECTED_INFOS);
                    User user = CommonPresenter.getUserFromJSON(jsonUser);
                    Comment comment = new Comment(0, product.getProductId(), user.getEmail(), messageToSend);
                    newComments.add(comment);
                    for (Comment c : comments){
                        newComments.add(c);
                    }
                    // Save new comment data
                    iProduct.saveProductComments(newComments);
                    loadDataInRecyclerView(view.getContext(), product, newComments, false, false);
                    // Send comment to server
                    addNewCommentToProduct(view.getContext(), comment, CommonPresenter.getRetrofitInstance(view.getContext()));
                }
                else{
                    CommonPresenter.showSnackBarMessage(view, view.getContext().getString(R.string.lb_no_network_find));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->sendProductComment() : "+ex.getMessage());
        }
    }

    /**
     * Add or Remove product to favorites
     */
    @Override
    public void addProductToFavorites(View view, Intent intent) {
        try {
            if(iProduct != null && view != null && intent != null){
                Context context = view.getContext();
                String jsonProduct = intent.getStringExtra(CommonPresenter.DETAIL_PRODUCT);
                Product product = CommonPresenter.getProductFromJSON(jsonProduct);
                CRUDFavorite crudFavorite = new CRUDFavorite(context);
                boolean isProductExists = crudFavorite.isProductExists(product.getProductId());
                if(isProductExists){
                    crudFavorite.deleteByProductId(product.getProductId());
                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_product_delete_to_favorite));
                    iProduct.changeImageRightResource(R.drawable.ic_add_to_favorite_32dp);
                }
                else{
                    crudFavorite.add(new Favorite(0, product));
                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_product_add_to_favorite));
                    iProduct.changeImageRightResource(R.drawable.ic_favorite_32dp);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->addProductToFavorites() : "+ex.getMessage());
        }
    }

    /**
     * Add new comment to product
     * @param context
     * @param comment
     * @param retrofit
     */
    private void addNewCommentToProduct(final Context context, final Comment comment, Retrofit retrofit){
        try {
            if(iProduct != null && context != null && comment != null && retrofit != null){
                final View view = CommonPresenter.getViewInTermsOfContext(context);
                // Create interface objet
                CoursModeProjetApi modeProjetApi = retrofit.create(CoursModeProjetApi.class);
                // Retrieve prototype method from api
                Call<Comment> call = modeProjetApi.addComment(comment.getProductId(), comment.getEmail(), comment.getContent());
                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if(!response.isSuccessful()){
                            CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_traitement_error));
                            return;
                        }
                        Comment mComment = response.body();
                        Log.i("TAG_APP", "CODE = "+mComment.getSuccess()+", MESSAGE = "+mComment.getMessage());
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        CommonPresenter.showSnackBarMessage(view, t.getMessage());
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->addNewCommentToProduct() : "+ex.getMessage());
        }
    }

    /**
     * Load detail product
     * @param context
     * @param product
     * @param retrofit
     */
    private void loadDetailProduct(final Context context, final Product product, Retrofit retrofit){
        try {
            if(iProduct != null && context != null && product != null && retrofit != null){
                iProduct.loadDetailProductData(product, null, 1, true);
                // Create interface objet
                CoursModeProjetApi modeProjetApi = retrofit.create(CoursModeProjetApi.class);
                // Retrieve prototype method from api
                Call<List<Comment>> call = modeProjetApi.getProductComments(product.getProductId());
                call.enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        if(!response.isSuccessful()){
                            iProduct.modifyToolbarTitle(context.getString(R.string.lb_traitement_error));
                            loadDataInRecyclerView(context, product, new ArrayList<Comment>(), false, false);
                            return;
                        }
                        //--
                        ArrayList<Comment> comments = (ArrayList<Comment>)response.body();
                        loadDataInRecyclerView(context, product, comments, false, true);
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {
                        loadDataInRecyclerView(context, product, new ArrayList<Comment>(), false, false);
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->loadDetailProduct() : "+ex.getMessage());
        }
    }

    /**
     * Load data in the recyclerView
     * @param context
     * @param product
     * @param comments
     * @param showProgress
     * @param saveData
     */
    private void loadDataInRecyclerView(Context context, Product product, ArrayList<Comment> comments, boolean showProgress, boolean saveData){
        try {
            if(product != null && comments != null && context != null && iProduct != null){
                iProduct.loadDetailProductData(product, comments, 1, false);
                if(saveData){
                    CRUDComment crudComment = new CRUDComment(context);
                    crudComment.deleteAll();
                    int totalSave = 0;
                    if(comments.size() > 0){
                        for (Comment comment : comments){
                            if(totalSave < 100){
                                crudComment = new CRUDComment(context);
                                crudComment.add(comment);
                            }
                            totalSave++;
                        }
                    }
                    iProduct.saveProductComments(comments);
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "ProductPresenter-->loadDataInRecyclerView() : "+ex.getMessage());
        }
    }
}
