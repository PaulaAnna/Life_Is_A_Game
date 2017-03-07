package intlig.lifeisagame.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import intlig.lifeisagame.Models.User;
import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.AUTHENTICATION_POSSIBLE_TRIES;
import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;
import static intlig.lifeisagame.Classes.Constants.getAlertDialog;
import static intlig.lifeisagame.Classes.Constants.isEmptyEditText;
import static intlig.lifeisagame.Classes.Constants.isValidEmail;

public class RegisterActivity extends Activity {

    //Register user data
    EditText username;
    EditText firstname;
    EditText lastname;
    Spinner location;
    EditText emailAddress;
    EditText password;
    EditText passwordConfirmed;
    CheckBox termsOfUse;
    Button registerButton;

    Firebase firebaseRef;
    Context registerContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Register");

        username = (EditText) findViewById(R.id.username);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        emailAddress = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordConfirmed = (EditText) findViewById(R.id.confirm_password);
        registerButton = (Button) findViewById(R.id.register_button);

        location = (Spinner) findViewById(R.id.country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsOfUse = (CheckBox) findViewById(R.id.terms_of_use);

                if(isEmptyEditText(username)||isEmptyEditText(firstname)||isEmptyEditText(lastname)
                        ||isEmptyEditText(emailAddress)||isEmptyEditText(password)||isEmptyEditText(passwordConfirmed)){
                    getAlertDialog("Please fill in all fields", registerContext);
                }else if(!isValidEmail(emailAddress.getText().toString())){
                    getAlertDialog("Invalid email address", registerContext);
                }else if(!password.getText().toString().equals(passwordConfirmed.getText().toString())){
                    getAlertDialog("Passwords don't match", registerContext);
                }else if(!termsOfUse.isChecked()){
                    getAlertDialog("Please accept the terms of use", registerContext);
                }else{

                    onRegister(emailAddress.getText().toString(), password.getText().toString());

                    finish();
                    //switchToLoginActivity();
                }
            }
        });
    }

    public void onRegister(String emailAddress, String password) {
        firebaseRef = new Firebase(FIREBASE_URL);
        firebaseRef.createUser(emailAddress, password, new RegisterValueResultHandler());

        boolean authenticated;
        int count = 0;
        do {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try{
                firebaseRef.authWithPassword(emailAddress, password, new LoginAuthResultHandler());
                authenticated = true;
            }catch(Exception e){
                Log.e("onAuthForRegisterError", "AuthForRegisterError occured!");
                count++;
                authenticated = false;
            }
        }while(!authenticated && count<=AUTHENTICATION_POSSIBLE_TRIES);
    }

    private class LoginAuthResultHandler implements Firebase.AuthResultHandler{

        String uid;
        @Override
        public void onAuthenticated(AuthData authData) {

            uid = authData.getUid();

            User newUser = new User(location.getSelectedItem().toString(), firstname.getText().toString(),
                    lastname.getText().toString(), username.getText().toString(), uid);
            Firebase newUserRef = firebaseRef.child("users").child(newUser.getUid());
            newUserRef.setValue(newUser);
            firebaseRef.unauth();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.e("onRegisterError", "LoginError occured!");
        }
    }

    public void switchToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private class RegisterValueResultHandler implements Firebase.ValueResultHandler<Map<String, Object>> {

        @Override
        public void onSuccess(Map<String, Object> stringObjectMap) {
            switchToLoginActivity();
        }

        @Override
        public void onError(FirebaseError firebaseError) {
            Log.e("onRegisterError", "RegisterError occured!");
        }
    }
}
