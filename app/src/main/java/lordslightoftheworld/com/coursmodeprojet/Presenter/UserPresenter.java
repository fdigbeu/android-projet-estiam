package lordslightoftheworld.com.coursmodeprojet.Presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import lordslightoftheworld.com.coursmodeprojet.Model.User;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.CoursModeProjetApi;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.UserView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class manage UserActivity data
 */
public class UserPresenter implements UserView.IPresenter {

    // Interface references
    private UserView.IUser iUser;

    // Constructors
    public UserPresenter(UserView.IUser iUser) {
        this.iUser = iUser;
    }

    @Override
    public void loadFormUserData(Context context, Intent intent) {
        try {
            if(iUser != null && context != null && intent != null){
                iUser.findWidgetsById();
                iUser.widgetsEvents();
                //--
                String toolbarTitle = intent.getStringExtra(CommonPresenter.USER_ACTIVITY_TITLE);
                iUser.modifyToolbarTitle(toolbarTitle);
                String jsonUser = intent.getStringExtra(CommonPresenter.USER_CONNECTED_INFOS);
                User user = CommonPresenter.getUserFromJSON(jsonUser);
                //--
                if(toolbarTitle.equalsIgnoreCase(context.getString(R.string.lb_show_info))){
                    String buttonText = context.getString(R.string.lb_form_modify_infos);
                    iUser.showFormUserInfos(user.getName().replace("null", ""), user.getEmail(), user.getPassword(), buttonText);
                }
                else if(toolbarTitle.equalsIgnoreCase(context.getString(R.string.lb_create_account))){
                    String buttonText = context.getString(R.string.lb_form_register);
                    iUser.showFormRegister(buttonText);
                }
                else if(toolbarTitle.equalsIgnoreCase(context.getString(R.string.lb_login))){
                    String buttonText = context.getString(R.string.lb_form_login);
                    iUser.showFormLogin(buttonText);
                    iUser.modifyFormEmailField(user != null && user.getEmail() != null ? user.getEmail() : "");
                }
                else{}
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "UserPresenter-->loadFormUserData() : "+ex.getMessage());
        }
    }

    /**
     * Send user data
     * @param view
     * @param userName
     * @param userEmail
     * @param userPassword
     * @param button
     */
    @Override
    public void sendFormUserData(View view, TextInputEditText userName, TextInputEditText userEmail, TextInputEditText userPassword, MaterialButton button) {
        try {
            if(iUser != null && view != null){
                Context context = view.getContext();
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if(email.length()==0){ userEmail.setError(context.getString(R.string.lb_field_required)); return; }
                if(password.length()==0){ userPassword.setError(context.getString(R.string.lb_field_required)); return; }
                if(!CommonPresenter.isValidEmail(email)){
                    CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_email_field_invalidate));
                    return;
                }

                User user = new User(name, email, password);;
                String jsonUser = CommonPresenter.getDavaFromSharedPreferences(context, CommonPresenter.USER_CONNECTED_INFOS);
                if(jsonUser != null && !jsonUser.trim().isEmpty()){
                    user = CommonPresenter.getUserFromJSON(jsonUser);
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                }

                if(button.getText().toString().equalsIgnoreCase(context.getString(R.string.lb_form_register))){
                    sendFormDataToServer(context, button.getText().toString(), user, CommonPresenter.getRetrofitInstance(context));
                }
                else if(button.getText().toString().equalsIgnoreCase(context.getString(R.string.lb_form_login))){
                    sendFormDataToServer(context, button.getText().toString(), user, CommonPresenter.getRetrofitInstance(context));
                }
                else if(button.getText().toString().equalsIgnoreCase(context.getString(R.string.lb_form_save_modify_infos))){
                    sendFormDataToServer(context, button.getText().toString(), user, CommonPresenter.getRetrofitInstance(context));
                }
                else if(button.getText().toString().equalsIgnoreCase(context.getString(R.string.lb_form_modify_infos))){
                    String buttonText = context.getString(R.string.lb_form_save_modify_infos);
                    iUser.showFormModifyUserInfos(buttonText);
                }
                else{}
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "UserPresenter-->sendFormUserData() : "+ex.getMessage());
        }
    }

    /**
     * Send form data to server
     * @param context
     * @param buttonText
     * @param user
     * @param retrofit
     */
    @Override
    public void sendFormDataToServer(final Context context, String buttonText, final User user, Retrofit retrofit){
        if(retrofit != null && context != null && iUser != null){
            iUser.progressBarVisibility(View.VISIBLE);
            final View view = CommonPresenter.getViewInTermsOfContext(context);
            // Create interface objet
            CoursModeProjetApi modeProjetApi = retrofit.create(CoursModeProjetApi.class);
            // Retrieve prototype method from api and send data
            // Registration
            if(buttonText.equalsIgnoreCase(context.getString(R.string.lb_form_register))){
                Call<User> call = modeProjetApi.createUser(user.getEmail(), user.getPassword());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        iUser.progressBarVisibility(View.GONE);
                        if(!response.isSuccessful()){
                            iUser.modifyToolbarTitle(context.getString(R.string.lb_traitement_error));
                            CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_traitement_error));
                            return;
                        }
                        User mUser = response.body();
                        mUser.setEmail(user.getEmail());
                        if(mUser.getError() == 204){
                            CommonPresenter.showSnackBarMessage(view, mUser.getMessage());
                            return;
                        }
                        //iUser.userRegisterIsOK(CommonPresenter.createGsonObject().toJson(mUser));
                        iUser.showFormLoginAutoConnect(user.getEmail(), user.getPassword());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        iUser.progressBarVisibility(View.GONE);
                        CommonPresenter.showSnackBarMessage(view, t.getMessage());
                    }
                });
            }
            // Connection
            else if(buttonText.equalsIgnoreCase(context.getString(R.string.lb_form_login))){
                Call<List<User>> call = modeProjetApi.connectUser(user.getEmail(), user.getPassword());
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        iUser.progressBarVisibility(View.GONE);
                        if(!response.isSuccessful()){
                            iUser.modifyToolbarTitle(context.getString(R.string.lb_traitement_error));
                            CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_traitement_error));
                            //Log.i("TAG_APP", "onResponse(NotSuccessFull) : "+context.getString(R.string.lb_traitement_error));
                            return;
                        }
                        User mUser = response.body().get(0);
                        iUser.userConnectionIsOK(CommonPresenter.createGsonObject().toJson(mUser));
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        iUser.progressBarVisibility(View.GONE);
                        CommonPresenter.showSnackBarMessage(view, t.getMessage());
                        //Log.i("TAG_APP", "onFailure() : "+t.getMessage());
                    }
                });
            }
            // Modify user data
            else if(buttonText.equalsIgnoreCase(context.getString(R.string.lb_form_save_modify_infos))){
                Call<User> call = modeProjetApi.modifyUser(user.getUserId(), user.getName(), user.getEmail(), user.getPassword());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(iUser != null){ iUser.progressBarVisibility(View.GONE); }
                        if(!response.isSuccessful()){
                            if(iUser != null){ iUser.modifyToolbarTitle(context.getString(R.string.lb_traitement_error)); }
                            CommonPresenter.showSnackBarMessage(view, context.getString(R.string.lb_traitement_error));
                            return;
                        }

                        int codeRetour = response.body().getSuccess();
                        String messageCode = response.body().getMessage();
                        CommonPresenter.showSnackBarMessage(view, messageCode);
                        Log.i("TAG_APP", "codeRetour = "+codeRetour);
                        if(codeRetour==0) {return;}
                        //Log.i("TAG_APP", "JSON-USER-MODIF-INFOS : "+messageCode);
                        String jsonUser = CommonPresenter.getDavaFromSharedPreferences(context, CommonPresenter.USER_CONNECTED_INFOS);
                        User mUser = CommonPresenter.getUserFromJSON(jsonUser);
                        mUser.setName(user.getName());
                        mUser.setEmail(user.getEmail());
                        CommonPresenter.saveDataInSharedPreferences(context, CommonPresenter.USER_CONNECTED_INFOS, CommonPresenter.createGsonObject().toJson(mUser));

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        if(iUser != null){ iUser.progressBarVisibility(View.GONE); }
                        CommonPresenter.showSnackBarMessage(view, t.getMessage());
                    }
                });
            }
            else{}
        }
    }
}
