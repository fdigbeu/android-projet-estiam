package lordslightoftheworld.com.coursmodeprojet.Model;

import com.google.gson.annotations.SerializedName;

public class Product {
    // Attributs
    @SerializedName("id")
    private int productId;
    @SerializedName("title")
    private String title;
    @SerializedName("filename")
    private String filename;

    // Constructors
    public Product(int productId, String title, String filename) {
        this.productId = productId;
        this.title = title;
        this.filename = filename;
    }

    // Getters and Setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
