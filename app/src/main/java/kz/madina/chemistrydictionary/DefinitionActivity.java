package kz.madina.chemistrydictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DefinitionActivity extends AppCompatActivity {

    TextView fw, sw, fd, sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        fw = (TextView) findViewById(R.id.fwText);
        fd = (TextView) findViewById(R.id.fdText);
        sw = (TextView) findViewById(R.id.swText);
        sd = (TextView) findViewById(R.id.sdText);

        Intent intent = this.getIntent();
        fw.setText(intent.getStringExtra("fw"));
        fd.setText(intent.getStringExtra("fd"));
        sw.setText(intent.getStringExtra("sw"));
        sd.setText(intent.getStringExtra("sd"));

        this.setTitle(intent.getStringExtra("fw"));
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
