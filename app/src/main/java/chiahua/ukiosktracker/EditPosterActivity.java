package chiahua.ukiosktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditPosterActivity extends AppCompatActivity {

    EditText nameField;
    EditText orgField;
    EditText locationField;
    EditText descriptionField;
    Poster poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poster);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameField = (EditText) findViewById(R.id.edit_nameET);
        orgField = (EditText) findViewById(R.id.edit_orgET);
        locationField = (EditText) findViewById(R.id.edit_locationET);
        descriptionField = (EditText) findViewById(R.id.edit_descriptionET);
        Intent receivedIntent = getIntent();
        long posterID = receivedIntent.getLongExtra("PosterID", -1);
        poster = Poster.findById(Poster.class, posterID);
        nameField.setText(poster.title());
        orgField.setText(poster.organization());
        locationField.setText((poster.eventLocation()));
        descriptionField.setText(poster.details());
    }

    public void editPoster(View view) {
        String name = nameField.getText().toString();
        String org = orgField.getText().toString();
        String location = locationField.getText().toString();
        String description = descriptionField.getText().toString();

        //Poster newPoster = new Poster(name, org, location, null, description, kiosks);
        if (name.trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Poster Name is a required field", Toast.LENGTH_SHORT).show();
        }
        else {
            // TODO: Set poster event time when time EditText is created
            // Save poster details to poster database
            poster.modify(name, org, location, "", description);
            poster.save();
            finish();
        }
    }

    public void deletePoster(View view) {
        poster.delete();
        finish();
    }

}
