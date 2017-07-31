package chiahua.ukiosktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class EditPosterActivity extends AppCompatActivity {

    EditText nameField;
    EditText orgField;
    EditText locationField;
    EditText descriptionField;
    Poster poster;

    boolean addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "IN EDIT POSTER ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poster);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameField = (EditText) findViewById(R.id.edit_nameET);
        orgField = (EditText) findViewById(R.id.edit_orgET);
        locationField = (EditText) findViewById(R.id.edit_locationET);
        descriptionField = (EditText) findViewById(R.id.edit_descriptionET);
        Intent receivedIntent = getIntent();
        addNew = receivedIntent.getBooleanExtra("addNew", true);
        if (!addNew) {
            long posterID = receivedIntent.getLongExtra("PosterID", -1);
            poster = Poster.findById(Poster.class, posterID);
            nameField.setText(poster.title());
            orgField.setText(poster.organization());
            locationField.setText((poster.eventLocation()));
            descriptionField.setText(poster.details());
        }
        else {
            Button delete_cancel = (Button) findViewById(R.id.delete_cancel_button);
            Button edit_add = (Button) findViewById(R.id.edit_add_button);
            edit_add.setText(R.string.add);
            delete_cancel.setText(R.string.cancel);
            setTitle(R.string.add_new_poster);
        }

    }

    public void editPoster(View view) {
        String name = nameField.getText().toString();
        String org = orgField.getText().toString();
        String location = locationField.getText().toString();
        String description = descriptionField.getText().toString();


        if (name.trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Poster Name is a required field", Toast.LENGTH_SHORT).show();
        }
        else {
            // TODO: Set poster event time when time EditText is created
            // Save poster details to poster database
            if (addNew) {
                // TODO: Set poster event time when time EditText is created
                // Save poster details to poster database
                Poster poster = new Poster(name, org, location, "", description);
                poster.save();

                //List<Poster> allPosters = Poster.listAll(Poster.class);
                //Log.d("TAG", "allPosters size = " + allPosters.size());
                //allPosters.add(poster);
                //Poster.saveInTx(allPosters);
                //SugarRecord.saveInTx(allPosters);
                finish();
            }
            else {
                poster.modify(name, org, location, "", description);
                poster.save();
                finish();
            }

        }
    }

    public void deleteCancelButton(View view) {
        if (!addNew) {
            List<Poster> allPosters = Poster.listAll(Poster.class);
            Log.d("TAG", "allPosters size = " + allPosters.size());
            allPosters.remove(poster);
            List<KioskPoster> allKPs = KioskPoster.listAll(KioskPoster.class);
            for (KioskPoster kp : allKPs) {
                if (kp.matchPoster(poster))
                    kp.delete();
            }
            poster.delete();
        }
        finish();
    }

}
