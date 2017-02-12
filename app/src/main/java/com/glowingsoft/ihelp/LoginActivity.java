package com.glowingsoft.ihelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import glowingsoft.com.ihelp.R;

public class LoginActivity extends MainActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = LoginActivity.this;
        mRoot = (LinearLayout) findViewById(R.id.mRoot);

        //region references

        loginBtnJoin = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        mPb = (ProgressBar) findViewById(R.id.mPb);

        //region listenr
        loginBtnJoin.setOnClickListener(this);
        signupBtn.setOnClickListener(this);
        //endregion


        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupBtn:
                Intent intent = new Intent(mContext,SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.loginBtn:
                checkStatusLogin = "mannual";
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                if (editTextEmail.getText().toString().equals("")) {
                    showToast("Please enter email","short");
                } else if (!emailValidator(editTextEmail.getText().toString())) {
                    showToast("Please enter correct email","short");
                } else if (editTextPassword.getText().toString().equals("")) {
                    showToast("Please enter password","short");
                } else if (editTextPassword.getText().toString().length() < 6) {
                    showToast("Password should be atleast 6 character","short");
                } else {
                    email = editTextEmail.getText().toString();
                    password = editTextPassword.getText().toString();
                    loginRequest();
                }
                break;
        }
    }
}
