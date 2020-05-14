package lordslightoftheworld.com.coursmodeprojet.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import lordslightoftheworld.com.coursmodeprojet.Model.User;
import retrofit2.Retrofit;

public class UserView {

    // UserActivity's interface
    public interface IUser{
        public void findWidgetsById();
        public void widgetsEvents();
        public void modifyToolbarTitle(String title);
        public Intent getIntentData();
        public void showFormUserInfos(String userName, String userEmail, String userPassword, String buttonText);
        public void showFormModifyUserInfos(String buttonText);
        public void showFormRegister(String buttonText);
        public void showFormLogin(String buttonText);
        public void showFormLoginAutoConnect(String email, String password);
        public void progressBarVisibility(int visibility);
        public void userRegisterIsOK(String jsonReturnCode);
        public void userConnectionIsOK(String jsonUser);
        public void modifyFormEmailField(String email);
    }

    // User presenter's interface
    public interface IPresenter{
        public void loadFormUserData(Context context, Intent intent);
        public void sendFormUserData(View view, TextInputEditText userName, TextInputEditText userEmail, TextInputEditText userPassword, MaterialButton button);
        public void sendFormDataToServer(Context context, String buttonText, User user, Retrofit retrofit);
    }
}
