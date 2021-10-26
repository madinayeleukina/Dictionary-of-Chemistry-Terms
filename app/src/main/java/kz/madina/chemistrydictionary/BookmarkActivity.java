package kz.madina.chemistrydictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private DatabaseRepository databaseRepository;
    private String firstLang;
    private String secondLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        ListView listView = findViewById(R.id.bookmarkedListView);

        databaseHelper = new DatabaseHelper(this);
        databaseRepository = new DatabaseRepository(databaseHelper);

        firstLang = getIntent().getStringExtra(MainActivity.FIRST_LANGUAGE);
        secondLang = getIntent().getStringExtra(MainActivity.SECOND_LANGUAGE);

        ArrayList<Term> termsList = databaseRepository.getBookmarkedData(firstLang,secondLang);
        ListAdapter adapter = new ListAdapter(this, databaseRepository, termsList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
