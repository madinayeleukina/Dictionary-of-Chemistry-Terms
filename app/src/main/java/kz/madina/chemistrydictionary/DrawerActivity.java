package kz.madina.chemistrydictionary;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;


    protected void onCreateDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dictionary) {
            Intent dictionaryIntent = new Intent(DrawerActivity.this, MainActivity.class);
            startActivity(dictionaryIntent);
        }
        else if (id == R.id.nav_bookmark) {
            Intent bookmarkIntent = new Intent(DrawerActivity.this, BookmarkActivity.class);
            bookmarkIntent.putExtra(MainActivity.FIRST_LANGUAGE, "");
            bookmarkIntent.putExtra(MainActivity.SECOND_LANGUAGE, "");
            startActivity(bookmarkIntent);
        } else if (id == R.id.nav_information) {
            Intent informationIntent = new Intent(DrawerActivity.this, InformationActivity.class);
            startActivity(informationIntent);
        } else if (id == R.id.nav_communication) {
            Intent communicationIntent = new Intent(DrawerActivity.this, CommunicationActivity.class);
            startActivity(communicationIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
