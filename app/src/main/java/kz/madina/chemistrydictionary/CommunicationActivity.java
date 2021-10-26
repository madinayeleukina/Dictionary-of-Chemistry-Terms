package kz.madina.chemistrydictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        this.setTitle("Communication");
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
