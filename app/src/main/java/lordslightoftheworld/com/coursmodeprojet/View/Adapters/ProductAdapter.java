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

import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.Presenter.HomePresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.HomeView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    // Attributs
    private Context context;
    private ArrayList<Product> products;
    private String keyWord;
    private HomePresenter homePresenter;
    private Hashtable<Integer, MyViewHolder> myViewHolder;

    // Constructor
    public ProductAdapter(Context context, ArrayList<Product> products, String keyWord, HomeView.IHome iHome) {
        this.context = context;
        this.products = products;
        this.keyWord = keyWord;
        this.homePresenter = new HomePresenter(iHome);
        myViewHolder = new Hashtable<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.position = position;
        myViewHolder.put(position, holder);
        Product product = products.get(position);
        if(keyWord != null) { holder.titleProduct.setText(CommonPresenter.colorKeyWordInText(product.getTitle(), keyWord, "#D50000")); }
        else{ holder.titleProduct.setText(product.getTitle()); }

        Hashtable<String, Integer> screenResolution = CommonPresenter.getScreenResolution(context);
        int columnNumber = CommonPresenter.getColumnNumber(context);
        int imageWidth = screenResolution.get("width")/columnNumber;
        Glide.with(context)
                .load(product.getFilename())
                .override(imageWidth)
                .into(holder.iconProduct);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        // Widgets
        int position;
        View productCard;
        ImageView iconProduct;
        MaterialTextView titleProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Layout
            productCard = itemView.findViewById(R.id.productCard);
            iconProduct = itemView.findViewById(R.id.iconProduct);
            titleProduct = itemView.findViewById(R.id.titleProduct);

            // Item is clicked
            productCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homePresenter.launchProductActivity(context, CommonPresenter.createGsonObject().toJson(products.get(position)));
                }
            });
        }
    }
}
