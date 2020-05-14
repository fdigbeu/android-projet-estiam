package lordslightoftheworld.com.coursmodeprojet.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;

public class ProductView {

    // ProductActivity's interface
    public interface IProduct{
        public void findWidgetsById();
        public void widgetsEvents();
        public void loadDetailProductData(Product product, ArrayList<Comment> comments, int columnNumber, boolean showProgress);
        public void sendCommentVisibility(int visibility);
        public void modifyToolbarTitle(String title);
        public void saveProductComments(ArrayList<Comment> comments);
        public ArrayList<Comment> getProductComments();
        public void toolbarImagesVisibility(int visibilityLeft, int visibilityRight);
        public void changeImageRightResource(int drawable);
        public void modifyToolBarImageRight(int drawable);
    }

    // Detail product presenter's interface
    public interface IPresenter{
        public void loadDetailProductData(Context context, Intent intent);
        public void sendProductComment(View view, TextInputEditText editText, Intent intent);
        public void addProductToFavorites(View view, Intent intent);
    }
}
