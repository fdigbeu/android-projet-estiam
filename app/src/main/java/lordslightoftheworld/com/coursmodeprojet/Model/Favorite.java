package lordslightoftheworld.com.coursmodeprojet.Model;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    // Attributes
    @SerializedName("id")
    private int id;
    @SerializedName("product")
    private Product product;

    // Constructeurs
    public Favorite(int id, Product product) {
        this.id = id;
        this.product = product;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
