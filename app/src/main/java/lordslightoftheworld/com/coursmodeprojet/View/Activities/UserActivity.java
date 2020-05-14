package lordslightoftheworld.com.coursmodeprojet.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import lordslightoftheworld.com.coursmodeprojet.Model.User;
import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;
import lordslightoftheworld.com.coursmodeprojet.Presenter.UserPresenter;
import lordslightoftheworld.com.coursmodeprojet.R;
import lordslightoftheworld.com.coursmodeprojet.View.Interfaces.UserView;

public class UserActivity extends AppCompatActivity implements UserView.IUser {

    // Atributs
    private Intent intent;

    // Toolbar widgets
    private MaterialToolbar toolbar;
    private ImageView imageLeft;
    private MaterialTextView toolbarTitle;
    private ImageView imageRight;

    // Others widgets
    private ImageView imageHeader;
    private TextInputLayout userNameInputLayout;
    private TextInputLayout userEmailInputLayout;
    private TextInputLayout userPasswordInputLayout;
    private TextInputEditText userNameField;
    private TextInputEditText userEmailField;
    private TextInputEditText userPasswordField;
    private MaterialButton buttonValidate;
    private ProgressBar userProgress;

    // Presenter
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // Load data
        this.intent = this.getIntent();
        userPresenter = new UserPresenter(this);
        userPresenter.loadFormUserData(UserActivity.this, intent);
    }

    /**
     * Find widgets by id
     */
    @Override
    public void findWidgetsById() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLeft = findViewById(R.id.imageLeft);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageRight = findViewById(R.id.imageRight);
        imageHeader = findViewById(R.id.imageHeader);
        userNameInputLayout = findViewById(R.id.userNameInputLayout);
        userEmailInputLayout = findViewById(R.id.userEmailInputLayout);
        userPasswordInputLayout = findViewById(R.id.userPasswordInputLayout);

        userNameField = findViewById(R.id.userNameField);
        userEmailField = findViewById(R.id.userEmailField);
        userPasswordField = findViewById(R.id.userPasswordField);
        buttonValidate = findViewById(R.id.buttonValidate);
        userProgress = findViewById(R.id.userProgress);
    }

    @Override
    public void widgetsEvents() {
        // Image left (BackPress)
        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        // Button validate
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPresenter.sendFormUserData(v, userNameField, userEmailField, userPasswordField, buttonValidate);
            }
        });
    }

    @Override
    public void modifyToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public Intent getIntentData() {
        return this.intent;
    }

    @Override
    public void showFormUserInfos(String userName, String userEmail, String userPassword, String buttonText){
        imageLeft.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        imageHeader.setImageResource(R.drawable.ic_user_connected_32dp);
        imageHeader.setColorFilter(getResources().getColor(R.color.colorPrimary));
        userNameField.setText(userName);
        userEmailField.setText(userEmail);
        userPasswordField.setText(userPassword);
        buttonValidate.setText(buttonText);
        userNameInputLayout.setEnabled(false);
        userEmailInputLayout.setEnabled(false);
        userPasswordInputLayout.setEnabled(false);
    }

    @Override
    public void showFormModifyUserInfos(String buttonText){
        imageLeft.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        buttonValidate.setText(buttonText);
        userNameInputLayout.setEnabled(true);
        userEmailInputLayout.setEnabled(true);
        userPasswordInputLayout.setEnabled(true);
    }

    @Override
    public void showFormRegister(String buttonText){
        imageLeft.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        userNameInputLayout.setVisibility(View.GONE);
        imageHeader.setImageResource(R.drawable.ic_registration_32dp);
        imageHeader.setColorFilter(getResources().getColor(R.color.colorPrimary));
        userNameField.setText("");
        userEmailField.setText("");
        userPasswordField.setText("");
        buttonValidate.setText(buttonText);
    }

    @Override
    public void showFormLogin(String buttonText){
        imageLeft.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        userNameInputLayout.setVisibility(View.GONE);
        imageHeader.setImageResource(R.drawable.ic_user_lock_32dp);
        imageHeader.setColorFilter(getResources().getColor(R.color.colorPrimary));
        userNameField.setText("");
        userEmailField.setText("");
        userPasswordField.setText("");
        buttonValidate.setText(buttonText);
    }

    @Override
    public void showFormLoginAutoConnect(String email, String password){
        imageLeft.setVisibility(View.VISIBLE);
        imageRight.setVisibility(View.INVISIBLE);
        userNameInputLayout.setVisibility(View.GONE);
        imageHeader.setImageResource(R.drawable.ic_user_lock_32dp);
        imageHeader.setColorFilter(getResources().getColor(R.color.colorPrimary));
        userNameField.setText("");
        userEmailField.setText(email);
        userPasswordField.setText(password);
        buttonValidate.setText(getString(R.string.lb_form_login));
        buttonValidate.performClick();
    }

    @Override
    public void progressBarVisibility(int visibility) {
        userProgress.setVisibility(visibility);
    }

    @Override
    public void userRegisterIsOK(String jsonUser) {
        Intent mIntent = new Intent();
        mIntent.putExtra(CommonPresenter.USER_CONNECTED_INFOS, jsonUser);
        mIntent.putExtra(CommonPresenter.USER_ACTIVITY_METHOD, "userRegisterIsOK");
        setResult(CommonPresenter.USER_ACTIVITY_RETURN_CODE, mIntent);
        finish();
    }

    @Override
    public void userConnectionIsOK(String jsonUser) {
        Intent mIntent = new Intent();
        mIntent.putExtra(CommonPresenter.USER_CONNECTED_INFOS, jsonUser);
        mIntent.putExtra(CommonPresenter.USER_ACTIVITY_METHOD, "userConnectionIsOK");
        setResult(CommonPresenter.USER_ACTIVITY_RETURN_CODE, mIntent);
        finish();
    }

    @Override
    public void modifyFormEmailField(String email) {
        userEmailField.setText(email);
    }
}
