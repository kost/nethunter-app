package com.offsec.nethunter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.ActionBar;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class AppNavHomeActivity extends FragmentActivity
        implements SideMenu.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    private SideMenu mNavigationDrawerFragment;
    private String[] activityNames;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.base_layout);
        //set kali wallpaper as background
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() +"/kali-nh/wallpaper/kali-nh-2183x1200.png";
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        ImageView myImageView = (ImageView)findViewById(R.id.bgHome);
        myImageView.setImageBitmap(bitmap);


        if (Build.VERSION.SDK_INT >= 21) {
            // detail for android 5 devices
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkTitle));
        }

        mNavigationDrawerFragment = (SideMenu)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        String[][] activitiesInfo = mNavigationDrawerFragment.getMenuInfo();
        activityNames = activitiesInfo[0];
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, String activity) {
        //Log.d("POSI", String.valueOf(position));
        // This is called from the sidemenu as callback when a item  is clickled

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (position == 0) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, NetHunterFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();
        } else if (position == 1) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, KaliLauncherFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();
        } else if (position == 2) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, KaliServicesFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();

        } else if (position == 3) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, HidFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();
        } else if (position == 4) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, BadusbFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();

        } else if (position == 5) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ManaFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();

        } else if (position == 6) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, DnsmasqFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();

//        } else if (position == 7) {
//
//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.container, HostapdFragment.newInstance(position))
//                    .addToBackStack(null)
//                    .commit();

        } else if (position == 7) {

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, IptablesFragment.newInstance(position))
                    .addToBackStack(null)
                    .commit();

        } else {
            // Start activity as usually // REMOVE THIS SOON no needed
            Intent target = new Intent();
            target.setClassName(getApplicationContext(), activity);
            startActivity(target);
        }
    }

    public void onSectionAttached(int position) {
        // restore title
        mTitle = activityNames[position];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onBackPressed() {
        //Handle back button for fragments && menu
        //FragmentManager fragmentManager = getFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawner();
        }
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            finish();

            return;
        }
        super.onBackPressed();
    }

    public void showMessage(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }


    public String readConfigFile(String configFilePath) {
        
    	
    	File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, configFilePath);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Log.e("Nethunter", "exception", e);
        	Logger Logger = new Logger();
        	Logger.appendLog(e.getMessage());
        }
        return text.toString();
    }

    public boolean updateConfigFile(String configFilePath, String source) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File myFile = new File(sdcard, configFilePath);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(source);
            myOutWriter.close();
            fOut.close();
            return true;
        } catch (Exception e) {
            showMessage(e.getMessage());
            Logger Logger = new Logger();
        	Logger.appendLog(e.getMessage());
            return false;
        }
    }


}

