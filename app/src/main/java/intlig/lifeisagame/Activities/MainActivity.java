package intlig.lifeisagame.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.skobbler.ngx.SKPrepareMapTextureThread;

import java.io.IOException;
import java.util.List;

import intlig.lifeisagame.Fragments.ChangePasswordFragment;
import intlig.lifeisagame.Fragments.ProfileFragment;
import intlig.lifeisagame.Fragments.SettingsFragment;
import intlig.lifeisagame.Map.MySKPrepareMapTextureListener;
import intlig.lifeisagame.R;

import static intlig.lifeisagame.Classes.Constants.FIREBASE_URL;
import static intlig.lifeisagame.Classes.Constants.MAP_RES_DIR_PATH;


public class MainActivity extends AppCompatActivity  {

    String[] navTitles = {"Home", "Test", "Profile", "Logout", "Change Password"};
    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    String url;
    Firebase mRootRef;
    String email;
    FloatingActionButton fab;

    Button profilePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Überschrift
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        tb.setTitle("Life is a game");
        setSupportActionBar(tb);

        //Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, navTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_closed  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        Intent loginIntent = getIntent();
        url = loginIntent.getExtras().getString("login");
        email = loginIntent.getExtras().getString("email");
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*profilePage = (Button) findViewById(R.id.btnProfile);

        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ProfileFragment profFrag = new ProfileFragment();
                ft.add(R.id.profileFragment, profFrag);
                ft.commit();
            }
        });*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NavigationDialog dialog = new NavigationDialog();
                dialog.show(fm, "NavigationDialog");
            }
        });
        fab.hide();

    }

    public String getUserUrl(){
        return url;
    }

    public String getUserEmail(){
        return email;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }


        public void selectItem(int position) {
            FragmentManager fm =getFragmentManager();
            Fragment fragment;
            switch (position) {
                case 1:
                    fragment=initMap();
                    break;
                case 2:
                    fragment = new ProfileFragment();
                    break;

                case 3:
                    drawerLayout.closeDrawers();
                    LogoutDialog dialog = new LogoutDialog();
                    dialog.show(fm, "LogoutDialog");
                return;

                case 4:
                    fragment = new ChangePasswordFragment();
                    break;

                default:
                    fragment = new SettingsFragment();
                    break;
            }
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerList.setItemChecked(position, true);
            drawerLayout.closeDrawers();
            if(position == 1) {
                fab.show();
            } else {
                fab.hide();
            }

        }
    }

    private Fragment initMap()
    {
        MySKPrepareMapTextureListener mySKPrepareMapTextureListener = new MySKPrepareMapTextureListener(MainActivity.this);
        final SKPrepareMapTextureThread prepareMapTextureThread = new SKPrepareMapTextureThread(this, MAP_RES_DIR_PATH, "SKMaps.zip", mySKPrepareMapTextureListener);
        prepareMapTextureThread.start();
        return mySKPrepareMapTextureListener.getMapFragment();
    }


    @SuppressLint("ValidFragment")
    public class LogoutDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Add the buttons
            builder.setTitle(R.string.log_out);
            builder.setMessage(R.string.log_out_text);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mRootRef = new Firebase(FIREBASE_URL);
                    MainActivity activity = (MainActivity) getActivity();
                    String urlFromActivity = activity.getUserUrl();
                    Firebase userRef = mRootRef.child("users/" + urlFromActivity);

                    userRef.unauth();
                    Intent logOutIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(logOutIntent);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            return builder.create();
        }
    }

    @SuppressLint("ValidFragment")
    public class NavigationDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Add the buttons
            builder.setTitle("Plan Route");
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText fromET = new EditText(getActivity());
            fromET.setHint("Start From...");

            final AutoCompleteTextView fromETauto= new AutoCompleteTextView(getActivity());
            fromET.setHint("Start From..");

            layout.addView(fromET);
            layout.addView(fromETauto);
            final EditText toET = new EditText(getActivity());
            toET.setHint("Destination address");

            layout.addView(toET);

            builder.setView(layout);

            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //TODO LOGIK FÜRS ROUTE PLANEN

                    String from = fromET.getText().toString();
                    String to= toET.getText().toString();
                    // Creating an instance of Geocoder class
                    Geocoder geocoder = new Geocoder(getBaseContext());
                    List<Address> addresses = null;

                    try {
                        // Getting a maximum of 3 Address that matches the input text
                        addresses=geocoder.getFromLocationName(from,5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Address a: addresses)
                    {
                        Log.d("Map","Geocoder: Lat: "+a.getLatitude()+" Long: "+a.getLongitude());
                    }
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            return builder.create();
        }
    }


}
