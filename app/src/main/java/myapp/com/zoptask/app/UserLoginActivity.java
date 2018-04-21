package myapp.com.zoptask.app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import myapp.com.zoptask.R;
import myapp.com.zoptask.application.AppApplication;
import myapp.com.zoptask.databasehelper.DatabaseHelper;
import myapp.com.zoptask.model.UserDetail;
import myapp.com.zoptask.mylibrary.StringUtils;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout input_layout_email, input_layout_password;
    private TextInputEditText input_Email, input_password;
    private AppCompatButton btn_login;
    private TextView regLabTxtTv;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_register);

        //set Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.statusbarcolor));
        }

        init();

        //get database instance
        databaseHelper=DatabaseHelper.getInstance(getBaseContext());
        sqLiteDatabase=databaseHelper.getReadableDatabase();

    }


    //initilize view
    private void init() {
        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) findViewById(R.id.input_layout_password);
        input_Email = (TextInputEditText) findViewById(R.id.input_Email);
        input_password = (TextInputEditText) findViewById(R.id.input_password);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        regLabTxtTv = (TextView) findViewById(R.id.regLabTxtTv);
        regLabTxtTv.setOnClickListener(this);
        btn_login.setOnClickListener(this);


        //input email text watcher
        input_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input_layout_email.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //input password text watcher
        input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input_layout_password.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                    if(!StringUtils.emailIdValid(input_Email.getText().toString().trim())){
                        input_layout_email.setError("Enter valid email id");
                    }else if(input_password.getText().toString().trim().equals("")){
                        input_layout_password.setError("password can not be blank");
                    }else{
                        userDetail=databaseHelper.getUserDetails(input_Email.getText().toString().trim(),input_password.getText().toString().trim(),sqLiteDatabase);
                        if(userDetail!=null){
                            //open product cart page
                            AppApplication.getInstance().setUser_id(userDetail.getId());
                            AppApplication.getInstance().setUserName(userDetail.getUserName());
                            startActivity(new Intent(getBaseContext(), ProductActivity.class));
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            finish();
                        }else{
                            Snackbar.make(view, "Username and password not found", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                break;
            case R.id.regLabTxtTv:
                startActivity(new Intent(getBaseContext(), UserSignUpActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }
}
