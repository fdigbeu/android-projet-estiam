package lordslightoftheworld.com.coursmodeprojet.View.Interfaces;

import java.util.List;
import java.util.Map;

import lordslightoftheworld.com.coursmodeprojet.Model.Comment;
import lordslightoftheworld.com.coursmodeprojet.Model.Product;
import lordslightoftheworld.com.coursmodeprojet.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface CoursModeProjetApi {
    /**
     * Display all products
     * @return
     */
    @GET("productsList.php")
    Call<List<Product>> getAllProducts();

    /**
     * Registration (Create new user)
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("registration.php")
    Call<User> createUser(
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Connect user
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("authentication.php")
    Call<List<User>> connectUser(
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Modify user data
     * @param name
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("modifyUserAccount.php")
    Call<User> modifyUser(
            @Field("id") int userId,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    /**
     * Add new comment
     * @param productId
     * @param email
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("addComment.php")
    Call<Comment> addComment(
            @Field("productId") int productId,
            @Field("email") String email,
            @Field("content") String content
    );

    /**
     * Add new comment
     * @param comment
     * @return
     */
    @POST("addComment.php")
    Call<Comment> addComment(@Body Comment comment);

    /**
     * Display all product's comments
     * @param productId
     * @return
     */
    @FormUrlEncoded
    @POST("commentsList.php")
    Call<List<Comment>> getProductComments(
            @Field("productId") int productId
    );
}
