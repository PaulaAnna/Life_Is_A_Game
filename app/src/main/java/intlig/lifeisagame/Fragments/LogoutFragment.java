package intlig.lifeisagame.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;

import intlig.lifeisagame.Activities.LoginActivity;
import intlig.lifeisagame.Activities.MainActivity;
import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;

/**
 * Created by Elias on 23.04.2016.
 */
public class LogoutFragment extends Fragment {

    Firebase mRootRef;
    Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        MainActivity activity = (MainActivity) getActivity();
        logoutButton = (Button) rootView.findViewById(R.id.logout_Button);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        return rootView;
    }

    public void logOut(){
        mRootRef = new Firebase(FIREBASE_URL);
        MainActivity activity = (MainActivity) getActivity();
        String urlFromActivity = activity.getUserUrl();
        Firebase userRef = mRootRef.child("users/" + urlFromActivity);

        userRef.unauth();
        Intent logOutIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(logOutIntent);

    }

}
