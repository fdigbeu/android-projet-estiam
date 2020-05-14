package lordslightoftheworld.com.coursmodeprojet.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Product;

/**
 * This class manage HomeActivity's and HomePresenter's interface
 */
public class HomeView {

    // HomeActivity's interface
    public interface IHome{
        public void findWidgetsById();
        public void widgetsEvents();
        public void loadProductsData(ArrayList<Product> products, int columnNumber, String keyWord);
        public void modifyToolbarTitle(String title);
        public void modifyInfoMessage(String title, int color);
        public void saveAllProductsData(ArrayList<Product> products);
        public ArrayList<Product> getAllProductsData();
        public void progressBarVisibility(int visibility);
        public void floatingButtonVisibility(int visibility);
        public void modifyFloatingButtonIcon(int drawable);
        public void toolbarVisibility(int visibilityLeft, int visibilityRight);
        public void searchInputLayoutVisibility(int visibility);
        public void modifySearchFieldData(String text);
        public boolean isSearchInputLayoutVisible();
        public void launchUserActivity(String toolbarTitle);
        public void launchProductActivity(String jsonProduct);
        public void changeImageRightResource(int imageSrc);
        public void launchFavoriteActivity();
    }

    // HomePresenter's interface
    public interface IPresenter{
        public void loadHomeData(Context context);
        public void retrieveUserAction(View view);
        public void retrieveUserAction(Context context, String searchText);
        public void launchProductActivity(Context context, String jsonProduct);
        public void onActivityResult(Context context, int requestCode, int resultCode, Intent data);
    }
}
