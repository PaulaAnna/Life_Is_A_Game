package intlig.lifeisagame.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import intlig.lifeisagame.Activities.LoginActivity;
import intlig.lifeisagame.Activities.MainActivity;
import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;

/**
 * Created by Elias on 23.04.2016.
 */
public class ChangePasswordFragment extends Fragment {

    Firebase mRootRef;
    Button cpButton;
    EditText oldPassword;
    EditText newPassword;
    AuthData currentSession;
    String emailUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_changepassword, container, false);
        MainActivity activity = (MainActivity) getActivity();
        cpButton = (Button) rootView.findViewById(R.id.change_button);
        oldPassword = (EditText) rootView.findViewById(R.id.old_password);
        newPassword = (EditText) rootView.findViewById(R.id.new_password);
        mRootRef = new Firebase(FIREBASE_URL);

        final String emailUser = activity.getUserEmail();

        cpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootRef.changePassword(emailUser, oldPassword.getText().toString(), newPassword.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Log.e("Sucess", "Changed PW! Congratulations");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Log.e("Failed", "Error!");
                    }
                });
            }
        });
        return rootView;
    }



}
