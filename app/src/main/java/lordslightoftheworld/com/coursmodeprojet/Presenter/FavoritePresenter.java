package lordslightoftheworld.com.coursmodeprojet.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.CRUDFavorite;
import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.FavoriteView;

/**
 * This class manage FavoriteActivity data
 */
public class FavoritePresenter implements FavoriteView.IPresenter {

    // Interface reference
    private FavoriteView.IFavorite iFavorite;

    // Constructors
    public FavoritePresenter(FavoriteView.IFavorite iFavorite) {
        this.iFavorite = iFavorite;
    }

    /**
     * Load products favorites data
     * @param context
     * @param intent
     */
    @Override
    public void loadProductsFavoritesData(Context context, Intent intent) {
        try {
            if(iFavorite != null && intent != null && context != null){
                iFavorite.findWidgetsById();
                iFavorite.widgetsEvents();
                iFavorite.toolbarImagesVisibility(View.VISIBLE, View.INVISIBLE);
                iFavorite.modifyToolbarTitle(context.getString(R.string.lb_favorites_products));
                loadFavoritesDataInRecyclerView(context);
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "FavoritePresenter-->loadProductsFavoritesData() : "+ex.getMessage());
        }
    }

    /**
     * Launch product acticity
     * @param context
     * @param favorite
     */
    @Override
    public void launchProductActivity(Context context, Favorite favorite) {
        try {
            iFavorite.launchProductActivity(favorite);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "FavoritePresenter-->launchProductActivity() : "+ex.getMessage());
        }
    }

    /**
     * Delete pruct to favorites list
     * @param context
     * @param favorite
     * @param position
     * @param iAdapter
     */
    @Override
    public void deleteProductFavorite(Context context, Favorite favorite, int position, FavoriteView.IFavoriteAdapter iAdapter) {
        try {
            if(context != null && favorite != null && iAdapter != null){
                CRUDFavorite crudFavorite = new CRUDFavorite(context);
                crudFavorite.deleteByProductId(favorite.getProduct().getProductId());
                iAdapter.removeProductAt(position);
                if(crudFavorite.list().size() == 0){
                    iFavorite.modifyFavoriteInfo(context.getString(R.string.lb_no_product_favorite));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "FavoritePresenter-->deleteProductFavorite() : "+ex.getMessage());
        }
    }

    /**
     * Load product favorites in recyclerview
     * @param context
     */
    private void loadFavoritesDataInRecyclerView(Context context){
        try {
            if(context != null && iFavorite != null){
                CRUDFavorite crudFavorite = new CRUDFavorite(context);
                ArrayList<Favorite> favorites = crudFavorite.list();
                if(favorites != null && favorites.size() > 0){
                    iFavorite.loadProductsFavoritesData(favorites, CommonPresenter.getColumnNumber(context));
                }
                else{
                    iFavorite.modifyFavoriteInfo(context.getString(R.string.lb_no_product_favorite));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "FavoritePresenter-->loadFavoritesDataInRecyclerView() : "+ex.getMessage());
        }
    }
}
