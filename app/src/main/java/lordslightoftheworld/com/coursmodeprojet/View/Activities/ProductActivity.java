package lordslightoftheworld.com.coursmodeprojet.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Presenter.ProductPresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Adapters.DetailProductAdapter;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.ProductView;

public class ProductActivity extends AppCompatActivity implements ProductView.IProduct {

    // Attributs
    private ArrayList<Comment> commentsList;

    // Toolbar widgets
    private MaterialToolbar toolbar;
    private ImageView imageLeft;
    private MaterialTextView toolbarTitle;
    private ImageView imageRight;

    // Others widgets
    private RecyclerView productDetailRV;
    private FloatingActionButton sendMessageFB;
    private TextInputLayout messageInputLayout;
    private TextInputEditText messageField;

    // Presenter
    private ProductPresenter productPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // Load detail product
        productPresenter = new ProductPresenter(this);
        productPresenter.loadDetailProductData(ProductActivity.this, this.getIntent());
    }

    @Override
    public void findWidgetsById() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLeft = findViewById(R.id.imageLeft);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageRight = findViewById(R.id.imageRight);
        productDetailRV = findViewById(R.id.productDetailRV);
        sendMessageFB = findViewById(R.id.sendMessageFB);
        messageInputLayout = findViewById(R.id.messageInputLayout);
        messageField = findViewById(R.id.messageField);
    }

    @Override
    public void widgetsEvents() {
        // Send comment
        sendMessageFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productPresenter.sendProductComment(v, messageField, getIntent());
            }
        });
        // Add to favorite
        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productPresenter.addProductToFavorites(v, getIntent());
            }
        });
        // Close activity
        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Load detail product data
     * @param product
     * @param comments
     * @param columnNumber
     * @param showProgress
     */
    @Override
    public void loadDetailProductData(Product product, ArrayList<Comment> comments, int columnNumber, boolean showProgress) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductActivity.this, columnNumber) ;
        productDetailRV.removeAllViews();
        productDetailRV.setLayoutManager(gridLayoutManager);
        productDetailRV.setHasFixedSize(true);
        DetailProductAdapter adapter = new DetailProductAdapter(ProductActivity.this, product, comments, showProgress);
        productDetailRV.setAdapter(adapter);
        productDetailRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void sendCommentVisibility(int visibility) {
        sendMessageFB.setVisibility(visibility);
        messageInputLayout.setVisibility(visibility);
    }

    @Override
    public void modifyToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void saveProductComments(ArrayList<Comment> comments) {
        this.commentsList = comments;
    }

    @Override
    public ArrayList<Comment> getProductComments() {
        return this.commentsList;
    }

    @Override
    public void toolbarImagesVisibility(int visibilityLeft, int visibilityRight) {
        imageLeft.setVisibility(visibilityLeft);
        imageRight.setVisibility(visibilityRight);
    }

    @Override
    public void changeImageRightResource(int drawable) {
        imageRight.setImageResource(drawable);
    }

    @Override
    public void modifyToolBarImageRight(int drawable) {
        imageRight.setImageResource(drawable);
    }
}
