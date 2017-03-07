package intlig.lifeisagame.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;
import static intlig.lifeisagame.Classes.Constants.getAlertDialog;
import static intlig.lifeisagame.Classes.Constants.isEmptyEditText;
import static intlig.lifeisagame.Classes.Constants.isValidEmail;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button loginButton;
    TextView forgotPassword;
    Firebase firebase;
    Context loginContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_URL);

        if (firebase.getAuth() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
        }else{
            Intent intent = new Intent(this, MainActivity.class);
        }

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(email.getText().toString(), password.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmptyEditText(email)) {
                    if(isValidEmail(email.getText().toString())) {
                        firebase.resetPassword(email.getText().toString(), new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                getAlertDialog("Your password was successfully reset - A temporary " +
                                        "password was sent to your email address", loginContext);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Log.e("onResetPasswordError", "ResetPasswordError occured!");

                                getAlertDialog("Passwort reset failed", loginContext);
                            }
                        });
                    }else{
                        getAlertDialog("Please fill in a email valid address", loginContext);
                    }
                }else{
                    getAlertDialog("Please fill in a email address", loginContext);
                }
            }
        });
    }

    public void onLogin(String email, String password) {
        firebase = new Firebase(FIREBASE_URL);
        firebase.authWithPassword(email, password, new LoginAuthResultHandler());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void switchToMainActivity(String url, String email) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login", url);
        intent.putExtra("email", email);

        startActivity(intent);
    }

    private class LoginAuthResultHandler implements Firebase.AuthResultHandler {

        @Override
        public void onAuthenticated(AuthData authData) {
            switchToMainActivity(authData.getUid(), authData.getProviderData().get("email").toString());
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Log.e("onAuthenticationError", "AuthenticationError occured!");
        }
    }
}
