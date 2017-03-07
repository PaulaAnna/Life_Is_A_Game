package intlig.lifeisagame.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import intlig.lifeisagame.Activities.MainActivity;
import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;

public class ProfileFragment extends Fragment {

    TextView Username;
    TextView Firstname;
    TextView Lastname;
    TextView Location;

    Firebase mRootRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mRootRef = new Firebase(FIREBASE_URL);


        Username = (TextView) view.findViewById(R.id.username);
        Firstname = (TextView) view.findViewById(R.id.firstname);
        Lastname = (TextView) view.findViewById(R.id.lastname);
        Location = (TextView) view.findViewById(R.id.location);





        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        MainActivity activity = (MainActivity) getActivity();
        String urlFromActivity = activity.getUserUrl();
        Firebase userRef = mRootRef.child("users/" + urlFromActivity);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String username = map.get("username");
                String firstname = map.get("firstname");
                String secondname = map.get("lastname");
                String location = map.get("location");
                Username.setText(username);
                Firstname.setText(firstname);
                Lastname.setText(secondname);
                Location.setText(location);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Username.setText(firebaseError.getMessage());
            }
        });


    }




}
