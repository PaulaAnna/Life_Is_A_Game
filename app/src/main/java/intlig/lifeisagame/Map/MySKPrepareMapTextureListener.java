package intlig.lifeisagame.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.SKMapsInitSettings;
import com.skobbler.ngx.SKPrepareMapTextureListener;
import com.skobbler.ngx.map.SKMapFragment;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKAdvisorSettings;

import intlig.lifeisagame.Classes.Constants;
import intlig.lifeisagame.R;

/**
 * Created by Dominik on 25.04.2016.
 */
public class MySKPrepareMapTextureListener implements SKPrepareMapTextureListener {

    private AppCompatActivity activity;

    public MySKPrepareMapTextureListener(AppCompatActivity activity) {

        this.activity=activity;
    }

    @Override
    public void onMapTexturesPrepared(boolean b) {
        Log.d("Map1","onMapTexuturesPrepared "+b);

    }
    public Fragment getMapFragment()
    {
        SKMapsInitSettings initMapSettings = new SKMapsInitSettings();

        // set path to map resources and initial map style
        initMapSettings.setMapResourcesPaths(Constants.MAP_RES_DIR_PATH, new SKMapViewStyle(Constants.MAP_RES_DIR_PATH+ "daystyle/", "daystyle.json"));
        initMapSettings.setMapResourcesPath(Constants.MAP_RES_DIR_PATH);
        final SKAdvisorSettings advisorSettings = initMapSettings.getAdvisorSettings();
        advisorSettings.setAdvisorConfigPath(Constants.MAP_RES_DIR_PATH +"/Advisor");
        advisorSettings.setResourcePath(Constants.MAP_RES_DIR_PATH +"/Advisor/Languages");
        advisorSettings.setLanguage(SKAdvisorSettings.SKAdvisorLanguage.LANGUAGE_DE);
        advisorSettings.setAdvisorVoice("de");
        initMapSettings.setAdvisorSettings(advisorSettings);

        SKMaps.getInstance().initializeSKMaps(activity.getBaseContext(), initMapSettings);

        SKMapFragment mapFragment=new SKMapFragment();
        mapFragment.initialise();


        mapFragment.setMapSurfaceListener(new MySKMapSurfaceListener(activity.getBaseContext()));

        return mapFragment;
    }
}
