package lordslightoftheworld.com.coursmodeprojet.View.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.Presenter.HomePresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Adapters.ProductAdapter;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.HomeView;

public class HomeActivity extends AppCompatActivity implements HomeView.IHome {

    // Attributes
    private ArrayList<Product> allProducts;
    private boolean showSearchView = false;

    // Toolbar widgets
    private MaterialToolbar toolbar;
    private ImageView imageLeft;
    private MaterialTextView toolbarTitle;
    private ImageView imageRight;

    // Others widgets
    private RecyclerView productsRV;
    private FloatingActionButton searchFB;
    private ProgressBar homeProgress;
    private MaterialTextView infoMessage;
    private TextInputLayout searchInputLayout;
    private TextInputEditText searchField;

    // Presenter
    private HomePresenter homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Load home data
        homePresenter = new HomePresenter(this);
        homePresenter.loadHomeData(HomeActivity.this);
    }

    /**
     * Find wudgets by id
     */
    @Override
    public void findWidgetsById() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLeft = findViewById(R.id.imageLeft);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageRight = findViewById(R.id.imageRight);
        productsRV = findViewById(R.id.productsRV);
        searchFB = findViewById(R.id.searchFB);
        homeProgress = findViewById(R.id.homeProgress);
        infoMessage = findViewById(R.id.infoMessage);
        searchInputLayout = findViewById(R.id.searchInputLayout);
        searchField = findViewById(R.id.searchField);
    }

    /**
     * widgets events
     */
    @Override
    public void widgetsEvents() {
        // Button to display : Registration view or user infos or disconnect
        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.retrieveUserAction(v);
            }
        });
        // Floating button
        searchFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchView = !showSearchView;
                homePresenter.retrieveUserAction(v);
            }
        });
        // Key search changed
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                homePresenter.retrieveUserAction(HomeActivity.this, s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    /**
     * Load products data
     * @param products
     * @param columnNumber
     * @param keyWord
     */
    @Override
    public void loadProductsData(ArrayList<Product> products, int columnNumber, String keyWord) {
        StaggeredGridLayoutManager staggeredGrid = new StaggeredGridLayoutManager(columnNumber, LinearLayoutManager.VERTICAL) ;
        productsRV.removeAllViews();
        productsRV.setLayoutManager(staggeredGrid);
        productsRV.setHasFixedSize(true);
        ProductAdapter adapter = new ProductAdapter(HomeActivity.this, products, keyWord, this);
        productsRV.setAdapter(adapter);
        productsRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void progressBarVisibility(int visibility) {
        homeProgress.setVisibility(visibility);
    }

    @Override
    public void floatingButtonVisibility(int visibility) {
        searchFB.setVisibility(visibility);
    }

    @Override
    public void modifyFloatingButtonIcon(int drawable) {
        searchFB.setImageDrawable(getResources().getDrawable(drawable));
    }

    @Override
    public void toolbarVisibility(int visibilityLeft, int visibilityRight) {
        imageLeft.setVisibility(visibilityLeft);
        imageRight.setVisibility(visibilityRight);
    }

    @Override
    public void changeImageRightResource(int imageSrc){
        imageRight.setImageResource(imageSrc);
    }

    @Override
    public void launchFavoriteActivity() {
        Intent intent = new Intent(HomeActivity.this, FavoriteActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void modifyToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void modifyInfoMessage(String title, int color) {
        infoMessage.setText(title);
        infoMessage.setTextColor(color);
    }

    @Override
    public void saveAllProductsData(ArrayList<Product> products){
        this.allProducts = products;
    }

    @Override
    public ArrayList<Product> getAllProductsData(){
        return allProducts;
    }

    @Override
    public void searchInputLayoutVisibility(int visibility) {
        searchInputLayout.setVisibility(visibility);
    }

    @Override
    public boolean isSearchInputLayoutVisible() {
        return showSearchView;
    }

    @Override
    public void modifySearchFieldData(String text){
        searchField.setText(text);
    }

    /**
     * Launch user activity
     */
    @Override
    public void launchUserActivity(String toolbarTitle) {
        Intent intent = new Intent(HomeActivity.this, UserActivity.class);
        String jsonUser = CommonPresenter.getDavaFromSharedPreferences(HomeActivity.this, CommonPresenter.USER_CONNECTED_INFOS);
        intent.putExtra(CommonPresenter.USER_CONNECTED_INFOS, jsonUser);
        intent.putExtra(CommonPresenter.USER_ACTIVITY_TITLE, toolbarTitle);
        startActivityForResult(intent, CommonPresenter.USER_ACTIVITY_RETURN_CODE);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Launch product activity
     * @param jsonProduct
     */
    @Override
    public void launchProductActivity(String jsonProduct) {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        intent.putExtra(CommonPresenter.DETAIL_PRODUCT, jsonProduct);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homePresenter.onActivityResult(HomeActivity.this, requestCode, resultCode, data);
    }
}
