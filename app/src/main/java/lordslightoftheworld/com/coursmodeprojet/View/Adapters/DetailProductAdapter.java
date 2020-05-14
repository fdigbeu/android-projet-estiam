package lordslightoftheworld.com.coursmodeprojet.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Hashtable;

import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.R;

public class DetailProductAdapter extends RecyclerView.Adapter<DetailProductAdapter.MyViewHolder> {

    // Attributs
    private Context context;
    private Product product;
    private ArrayList<Comment> comments;
    private boolean showProgress;

    // Constructor
    public DetailProductAdapter(Context context, Product product, ArrayList<Comment> comments, boolean showProgress) {
        this.context = context;
        this.product = product;
        this.comments = comments;
        this.showProgress = showProgress;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.productHeaderCard.setVisibility(View.GONE);
        holder.productLoadCommentCard.setVisibility(View.GONE);
        holder.productCommentCard.setVisibility(View.GONE);
        //--
        holder.position = position;
        if(position == 0){
            Hashtable<String, Integer> screenResolution = CommonPresenter.getScreenResolution(context);
            int imageWidth = screenResolution.get("width");
            Glide.with(context)
                    .load(product.getFilename())
                    .override(imageWidth)
                    .into(holder.productDetailImg);
            holder.productDetailTitle.setText(product.getTitle());
            holder.productHeaderCard.setVisibility(View.VISIBLE);
            holder.productLoadCommentCard.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            //--
            if(comments != null && comments.size()==0){
                holder.progressDetail.setVisibility(View.GONE);
                holder.productLoadCommentCard.setVisibility(View.VISIBLE);
                holder.productDetailLoading.setText(context.getString(R.string.lb_no_comment));
            }
        }
        else{
            Comment comment = comments.get(position-1);
            holder.productCommentCard.setVisibility(View.VISIBLE);
            holder.productDetailComment.setText(comment.getContent());
            holder.productCommentAuthor.setText(comment.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return (comments==null ? 1 : comments.size()+1);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        // Widgets
        int position;
        View productHeaderCard;
        ImageView productDetailImg;
        MaterialTextView productDetailTitle;
        //--
        View productLoadCommentCard;
        ProgressBar progressDetail;
        MaterialTextView productDetailLoading;
        //--
        View productCommentCard;
        MaterialTextView productDetailComment;
        MaterialTextView productCommentAuthor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Layout
            productHeaderCard = itemView.findViewById(R.id.productHeaderCard);
            productDetailImg = itemView.findViewById(R.id.productDetailImg);
            productDetailTitle = itemView.findViewById(R.id.productDetailTitle);
            productLoadCommentCard = itemView.findViewById(R.id.productLoadCommentCard);
            progressDetail = itemView.findViewById(R.id.progressDetail);
            productDetailLoading = itemView.findViewById(R.id.productDetailLoading);
            productCommentCard = itemView.findViewById(R.id.productCommentCard);
            productDetailComment = itemView.findViewById(R.id.productDetailComment);
            productCommentAuthor = itemView.findViewById(R.id.productCommentAuthor);
        }
    }
}
