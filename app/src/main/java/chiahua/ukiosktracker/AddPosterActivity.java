package chiahua.ukiosktracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddPosterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poster);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addPoster(View view) {
        EditText nameField = (EditText) findViewById(R.id.add_nameET);
        EditText orgField = (EditText) findViewById(R.id.add_orgET);
        EditText locationField = (EditText) findViewById(R.id.add_locationET);
        EditText descriptionField = (EditText) findViewById(R.id.add_descriptionET);
        String name = nameField.getText().toString();
        String org = orgField.getText().toString();
        String location = locationField.getText().toString();
        String description = descriptionField.getText().toString();
        //Poster newPoster = new Poster(name, org, location, null, description, kiosks);
        if (name.length() < 1) {
            Snackbar.make(view, "Error: Poster Name is a required field", Snackbar.LENGTH_LONG)
                  .setAction("Action", null).show();
           // Toast.makeText(getApplicationContext(), "Poster Name is a required field", Toast.LENGTH_SHORT);
        }
        else {
            Intent resultIntent = new Intent();
            String[] newData = new String[] {name, org, location, description};
            resultIntent.putExtra("New_Poster_Info", newData);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }

    }

}
