package chiahua.ukiosktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditPosterActivity extends AppCompatActivity {

    EditText nameField;
    EditText orgField;
    EditText locationField;
    EditText descriptionField;
    EditText monthField;
    EditText dateField;
    EditText yearField;
    String mmddyyyy;
    Poster poster;

    Menu menu;

    boolean addNew;
    int kioskID;

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
        monthField = (EditText) findViewById(R.id.edit_monthET);
        dateField = (EditText) findViewById(R.id.edit_dateET);
        yearField = (EditText) findViewById(R.id.edit_yearET);

        Intent receivedIntent = getIntent();
        addNew = receivedIntent.getBooleanExtra("addNew", true);
        kioskID = receivedIntent.getIntExtra("KioskID", -1);
        if (!addNew) {
            long posterID = receivedIntent.getLongExtra("PosterID", -1);
            poster = Poster.findById(Poster.class, posterID);
            nameField.setText(poster.title());
            orgField.setText(poster.organization());
            locationField.setText((poster.eventLocation()));
            descriptionField.setText(poster.details());
            getDateAndTime();
        }
        else {
            setTitle(R.string.add_new_poster);
        }

    }

    public void addEditButton() {
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
            //String time = "" + month ;
            String time = "";



            if (addNew) {
                // TODO: Set poster event time when time EditText is created
                // Save poster details to poster database
                Poster poster = new Poster(name, org, location, time.toString(), description);
                poster.save();
                if (kioskID > 0) {
                    Kiosk kiosk = Kiosk.findById(Kiosk.class, kioskID);
                    KioskPoster kp = new KioskPoster(kiosk, poster);
                    kp.save();
                    poster.increaseCount();
                    poster.save();
                }

                //List<Poster> allPosters = Poster.listAll(Poster.class);
                //Log.d("TAG", "allPosters size = " + allPosters.size());
                //allPosters.add(poster);
                //Poster.saveInTx(allPosters);
                //SugarRecord.saveInTx(allPosters);
                if (validateAndSetDate())
                    finish();
            }
            else {
                poster.modify(name, org, location, time.toString(), description);
                validateAndSetDate();
                poster.save();
                if (validateAndSetDate())
                    finish();
            }

        }
    }

    //Make sure Date is valid before calling this
    private void getDateAndTime() {
        String mmddyyyy = poster.getDetailArray()[3];
        if (mmddyyyy.length()>0) {
            monthField.setText(mmddyyyy.substring(0, 2));
            dateField.setText(mmddyyyy.substring(2, 4));
            yearField.setText(mmddyyyy.substring(4));
        }


    }

    private boolean validateAndSetDate() {
        String month = monthField.getText().toString();
        String date = dateField.getText().toString();
        String year = yearField.getText().toString();
        if (month.length()>0 || date.length()>0 || year.length()>0) {
            if (month.length()==0 || date.length()==0 || year.length()==0) {
                Toast.makeText(getApplicationContext(),
                        "Date not properly filled out", Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                int monthInt = integerVerifier(month);
                int dateInt = integerVerifier(date);
                int yearInt = integerVerifier(year);
                if (!(monthInt == -1 && dateInt == -1 && yearInt == -1)) {
                    if (monthInt > 12 || monthInt < 1) {
                        Toast.makeText(getApplicationContext(),
                                "Invalid Month", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else {
                        if (((dateInt <= 31) && (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10 || monthInt == 12)) ||
                                ((dateInt <= 30) && (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11)) ||
                                ((dateInt <= 29) && monthInt == 2)) {
                            String dateString = padDateInts(monthInt, 2) + padDateInts(dateInt, 2) + padDateInts(yearInt, 4);
                            poster.modify(null, null, null, dateString, null);
                            poster.save();
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid Date", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Date Entered is invalid", Toast.LENGTH_SHORT).show();
                    return false;
                }


            }
        }
        else {
            return true;
        }
    }

    private String padDateInts(int number, int length) {
        StringBuilder result = new StringBuilder("");
        result.append(number);
        while (result.length()<length) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    private int integerVerifier (String intString) {
        int result = -1;
        Scanner scanner = new Scanner(intString);
        if (scanner.hasNextInt()) {
            result = scanner.nextInt();
        }
        scanner.close();
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        if (addNew) {
            MenuItem deleteCancelMenu = (MenuItem) menu.findItem(R.id.delete_cancel);
            MenuItem editAddMenu = (MenuItem) menu.findItem(R.id.add_save);
            Log.d("TAG", "DeleteCancel is null " + (deleteCancelMenu==null));
            deleteCancelMenu.setTitle(R.string.cancel);
            editAddMenu.setTitle(R.string.add);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_save) {
            addEditButton();
            return true;
        }
        else if (id == R.id.delete_cancel) {
            deleteCancelButton();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void deleteCancelButton() {
        if (!addNew) {
            List<Poster> allPosters = Poster.listAll(Poster.class);
            Log.d("TAG", "allPosters size = " + allPosters.size());
            allPosters.remove(poster);
            List<KioskPoster> allKPs = KioskPoster.listAll(KioskPoster.class);
            for (KioskPoster kp : allKPs) {
                if (kp.matchPoster(poster))
                    kp.delete();
            }
            //Poster.saveInTx(allPosters);
            //SugarRecord.saveInTx(allPosters);
            //TODO: Deleting a poster causes endless crashes until uninstall. Suspect: Upon remove, need to also remove relevant KioskPosters
            poster.delete();
        }
        finish();
    }

}
