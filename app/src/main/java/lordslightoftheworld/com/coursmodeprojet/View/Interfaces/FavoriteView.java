package lordslightoftheworld.com.coursmodeprojet.View.Interfaces;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;

public class FavoriteView {

    // FavoriteActivity's interface
    public interface IFavorite {
        public void findWidgetsById();
        public void widgetsEvents();
        public void loadProductsFavoritesData(ArrayList<Favorite> favorites, int columnNumber);
        public void modifyToolbarTitle(String title);
        public void toolbarImagesVisibility(int visibilityLeft, int visibilityRight);
        public void modifyFavoriteInfo(String message);
        public void launchProductActivity(Favorite favorite);
    }

    // FavoriteAdapter's interface
    public interface IFavoriteAdapter{
        public void removeProductAt(int position);
    }

    // FavoritePresenter's interface
    public interface IPresenter{
        public void loadProductsFavoritesData(Context context, Intent intent);
        public void launchProductActivity(Context context, Favorite favorite);
        public void deleteProductFavorite(Context context, Favorite favorite, int position, IFavoriteAdapter iAdapter);
    }
}
