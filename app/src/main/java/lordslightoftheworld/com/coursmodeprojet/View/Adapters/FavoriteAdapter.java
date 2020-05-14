package lordslightoftheworld.com.coursmodeprojet.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Hashtable;

import lordslightoftheworld.com.coursmodeprojet.Model.Favorite;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.Presenter.FavoritePresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.FavoriteView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> implements FavoriteView.IFavoriteAdapter {

    // Attributs
    private Context context;
    private ArrayList<Favorite> favorites;
    private FavoritePresenter favoritePresenter;
    private Hashtable<Integer, MyViewHolder> myViewHolder;
    private FavoriteView.IFavoriteAdapter iAdapter;

    // Constructor
    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites, FavoriteView.IFavorite iFavorite) {
        this.context = context;
        this.favorites = favorites;
        this.favoritePresenter = new FavoritePresenter(iFavorite);
        myViewHolder = new Hashtable<>();
        this.iAdapter = this;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_favorite, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.position = position;
        myViewHolder.put(position, holder);
        Favorite favorite = favorites.get(position);
        Product product = favorite.getProduct();
        holder.titleFavProduct.setText(product.getTitle());
        Hashtable<String, Integer> screenResolution = CommonPresenter.getScreenResolution(context);
        int columnNumber = CommonPresenter.getColumnNumber(context);
        int imageWidth = screenResolution.get("width")/columnNumber;
        Glide.with(context)
                .load(product.getFilename())
                .override(imageWidth)
                .into(holder.iconFavProduct);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    @Override
    public void removeProductAt(int position) {
        favorites.remove(position);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        // Widgets
        int position;
        View productFavCard;
        ImageView iconFavDelete;
        ImageView iconFavProduct;
        MaterialTextView titleFavProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Layout
            productFavCard = itemView.findViewById(R.id.productFavCard);
            iconFavDelete = itemView.findViewById(R.id.iconFavDelete);
            iconFavProduct = itemView.findViewById(R.id.iconFavProduct);
            titleFavProduct = itemView.findViewById(R.id.titleFavProduct);

            // Item is clicked
            productFavCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoritePresenter.launchProductActivity(context, favorites.get(position));
                }
            });

            // Item is deleted
            iconFavDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoritePresenter.deleteProductFavorite(context, favorites.get(position), position, iAdapter);
                }
            });
        }
    }
}
