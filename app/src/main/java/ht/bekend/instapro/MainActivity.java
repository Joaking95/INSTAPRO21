package ht.bekend.instapro;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;



import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.parse.ParseUser;


import ht.bekend.instapro.Fragments.ComposeFragment;
import ht.bekend.instapro.Fragments.PostFragment;
import ht.bekend.instapro.Fragments.ProfileFragment;


public class MainActivity extends AppCompatActivity {


    String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        //queryposts();
        

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                       fragment = new PostFragment();
                       break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;

                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.action_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.logoout_menu,menu);
        return  true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_logout){

            //setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //add the buttons
            builder.setPositiveButton("LOGOOUT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            //create and show the alert dialog
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            //alert.setTitle("AlertDialogExample");
            alert . show ();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}