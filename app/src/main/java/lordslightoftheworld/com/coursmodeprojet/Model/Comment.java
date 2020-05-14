package lordslightoftheworld.com.coursmodeprojet.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;

public class Comment {
    // Attributs
    @SerializedName("id")
    private int commentId;
    @SerializedName("productId")
    private int productId;
    @SerializedName("email")
    private String email;
    @SerializedName("content")
    private String content;

    @SerializedName("success")
    private int success;
    @SerializedName("message")
    private String message;

    // Constructors
    public Comment(int commentId, int productId, String email, String content) {
        this.commentId = commentId;
        this.productId = productId;
        this.email = email;
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return CommonPresenter.createGsonObject().toJson(this);
    }

    // Getters and setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
