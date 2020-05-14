package lordslightoftheworld.com.coursmodeprojet.View.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.Presenter.FavoritePresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Adapters.FavoriteAdapter;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.FavoriteView;

public class FavoriteActivity extends AppCompatActivity implements FavoriteView.IFavorite {
    // Atributs
    private Intent intent;

    // Toolbar widgets
    private MaterialToolbar toolbar;
    private ImageView imageLeft;
    private MaterialTextView toolbarTitle;
    private ImageView imageRight;

    // Others widgets
    private RecyclerView productsFavoritesRV;
    private MaterialTextView favoriteInfoMessage;

    // Presenter
    private FavoritePresenter favoritePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        // Load favorites data
        favoritePresenter = new FavoritePresenter(this);
        favoritePresenter.loadProductsFavoritesData(FavoriteActivity.this, this.getIntent());
    }

    @Override
    public void findWidgetsById() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLeft = findViewById(R.id.imageLeft);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageRight = findViewById(R.id.imageRight);

        productsFavoritesRV = findViewById(R.id.productsFavoritesRV);
        favoriteInfoMessage = findViewById(R.id.favoriteInfoMessage);
    }

    @Override
    public void widgetsEvents() {
        // Close activity
        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadProductsFavoritesData(ArrayList<Favorite> favorites, int columnNumber) {
        StaggeredGridLayoutManager staggeredGrid = new StaggeredGridLayoutManager(columnNumber, LinearLayoutManager.VERTICAL) ;
        productsFavoritesRV.removeAllViews();
        productsFavoritesRV.setLayoutManager(staggeredGrid);
        productsFavoritesRV.setHasFixedSize(true);
        FavoriteAdapter adapter = new FavoriteAdapter(FavoriteActivity.this, favorites, this);
        productsFavoritesRV.setAdapter(adapter);
        productsFavoritesRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void modifyToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void toolbarImagesVisibility(int visibilityLeft, int visibilityRight) {
        imageLeft.setVisibility(visibilityLeft);
        imageRight.setVisibility(visibilityRight);
    }

    @Override
    public void modifyFavoriteInfo(String message) {
        favoriteInfoMessage.setText(message);
        favoriteInfoMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Launch product activity
     * @param favorite
     */
    @Override
    public void launchProductActivity(Favorite favorite) {
        Intent intent = new Intent(FavoriteActivity.this, ProductActivity.class);
        intent.putExtra(CommonPresenter.DETAIL_PRODUCT, CommonPresenter.createGsonObject().toJson(favorite.getProduct()));
        intent.putExtra(CommonPresenter.PRODUCT_FAVORITE_DATA, CommonPresenter.createGsonObject().toJson(favorite));
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
