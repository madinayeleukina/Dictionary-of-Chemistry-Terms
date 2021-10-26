package kz.madina.chemistrydictionary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public final static String ENG = "eng";
    public final static String KAZ = "kaz";
    public final static String RUS = "rus";
    public final static String FIRST_LANGUAGE = "firstlang";
    public final static String SECOND_LANGUAGE = "secondlang";
    private ArrayList<Term> termsList;
    private ListView listView;
    private DatabaseHelper dbHelper;
    private DatabaseRepository dbRepository;
    private ListAdapter adapter;
    private EditText searchText;
    private String firstLang;
    private String secondLang;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        firstLang = sharedPreferences.getString(FIRST_LANGUAGE, ENG);
        secondLang = sharedPreferences.getString(SECOND_LANGUAGE, RUS);

        dbHelper = new DatabaseHelper(this);
        dbHelper.initialise();//(Only for the first time or to totally upgrade)
        dbHelper.close();

        dbRepository = new DatabaseRepository(dbHelper);

        termsList = new ArrayList<Term>();
        termsList = dbRepository.getData(firstLang,secondLang,"");

        adapter = new ListAdapter(this, dbRepository, termsList);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DefinitionActivity.class);
                intent.putExtra("fw",termsList.get(position).getFrst().getWord());
                intent.putExtra("fd",termsList.get(position).getFrst().getDefinition());
                intent.putExtra("sw",termsList.get(position).getScnd().getWord());
                intent.putExtra("sd",termsList.get(position).getScnd().getDefinition());
                startActivity(intent);
            }
        });

        searchText = (EditText) findViewById(R.id.et_search);
        ImageView clearImage = (ImageView) findViewById(R.id.clearImage);
        final TextView langText = (TextView) findViewById(R.id.langText);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(searchText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        langText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,langText);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String lang = langText.getText().toString();
                        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                        Editor editor = sharedPref.edit();
                        switch (item.getItemId()) {
                            case R.id.firstLang_kaz:
                                editor.putString(FIRST_LANGUAGE, KAZ);
                                editor.apply();
                                firstLang = KAZ;
                                langText.setText("K" + lang.charAt(1));
                                onResume();
                                return true;
                            case R.id.firstLang_rus:
                                editor.putString(FIRST_LANGUAGE, RUS);
                                editor.apply();
                                firstLang = RUS;
                                langText.setText("R" + lang.charAt(1));
                                onResume();
                                return true;
                            case R.id.firstLang_eng:
                                editor.putString(FIRST_LANGUAGE, ENG);
                                editor.apply();
                                firstLang = ENG;
                                langText.setText("E" + lang.charAt(1));
                                onResume();
                                return true;
                            case R.id.secondLang_kaz:
                                editor.putString(SECOND_LANGUAGE,KAZ);
                                editor.apply();
                                secondLang = KAZ;
                                langText.setText(lang.charAt(0) + "K");
                                onResume();
                                return true;
                            case R.id.secondLang_rus:
                                editor.putString(SECOND_LANGUAGE,RUS);
                                editor.apply();
                                secondLang = RUS;
                                langText.setText(lang.charAt(0) + "R");
                                onResume();
                                return true;
                            case R.id.secondLang_eng:
                                editor.putString(SECOND_LANGUAGE,ENG);
                                editor.apply();
                                secondLang = ENG;
                                langText.setText(lang.charAt(0) + "E");
                                onResume();
                                return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void onResume() {
        super.onResume();
        loadData(searchText.getText().toString());
        termsList = dbRepository.getData(firstLang, secondLang,"");
    }

    public void loadData(String word) {
        termsList = dbRepository.getData(firstLang, secondLang, word);
        adapter.updateList(termsList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Editor editor = preferences.edit();
        switch (item.getItemId()) {
            case R.id.firstLang_kaz:
                editor.putString(FIRST_LANGUAGE, KAZ);
                editor.apply();
                firstLang = KAZ;
                onResume();
                return true;
            case R.id.firstLang_rus:
                editor.putString(FIRST_LANGUAGE, RUS);
                editor.apply();
                firstLang = RUS;
                onResume();
                return true;
            case R.id.firstLang_eng:
                editor.putString(FIRST_LANGUAGE, ENG);
                editor.apply();
                firstLang = ENG;
                onResume();
                return true;
            case R.id.secondLang_kaz:
                editor.putString(SECOND_LANGUAGE,KAZ);
                editor.apply();
                secondLang = KAZ;
                onResume();
                return true;
            case R.id.secondLang_rus:
                editor.putString(SECOND_LANGUAGE,RUS);
                editor.apply();
                secondLang = RUS;
                onResume();
                return true;
            case R.id.secondLang_eng:
                editor.putString(SECOND_LANGUAGE,ENG);
                editor.apply();
                secondLang = ENG;
                onResume();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dictionary) {
            Intent dictionaryIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(dictionaryIntent);
        }
        else if (id == R.id.nav_bookmark) {
            Intent bookmarkIntent = new Intent(MainActivity.this, BookmarkActivity.class);
            bookmarkIntent.putExtra(FIRST_LANGUAGE, firstLang);
            bookmarkIntent.putExtra(SECOND_LANGUAGE, secondLang);
            startActivity(bookmarkIntent);
        } else if (id == R.id.nav_information) {
            Intent informationIntent = new Intent(MainActivity.this, InformationActivity.class);
            startActivity(informationIntent);
        } else if (id == R.id.nav_communication) {
            Intent communicationIntent = new Intent(MainActivity.this, CommunicationActivity.class);
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
