package chiahua.ukiosktracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class EditPosterActivity extends AppCompatActivity {

    Calendar date = Calendar.getInstance();
    private EditText nameField;
    private EditText orgField;
    private EditText locationField;
    private EditText descriptionField;
    private Button datePicker;
    private Button clearDate;
    private Poster poster;
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
        datePicker = (Button) findViewById(R.id.datePickerButton);
        clearDate = (Button) findViewById(R.id.clearDate);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setText(R.string.date_picker_btn);
            }
        });
        Intent receivedIntent = getIntent();
        addNew = receivedIntent.getBooleanExtra("addNew", true);
        kioskID = receivedIntent.getIntExtra("KioskID", -1);
        Log.d("TAG", "KioskID is: " + kioskID);
        Log.d("TAG", "addNew is: " + addNew);
        if (!addNew) {
            long posterID = receivedIntent.getLongExtra("PosterID", -1);
            poster = Poster.findById(Poster.class, posterID);
            nameField.setText(poster.title());
            orgField.setText(poster.organization());
            locationField.setText((poster.eventLocation()));
            descriptionField.setText(poster.details());
            if (!poster.eventTime().equals("")) {
                restoreDateInfo();
                updateDateText();
            }
//            getDateAndTime();
        }
        else {
            setTitle(R.string.add_new_poster);
        }
    }

    public void updateDate() {
        new DatePickerDialog(this, dateSetListener,
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateText();
        }
    };

    private void updateDateText() {
        datePicker.setText((date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.YEAR));
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
        if (alreadyContains(name) && addNew) {
            Toast.makeText(getApplicationContext(),
                    "There is already a poster titled \""+name+"\". Please choose a different title.",
                    Toast.LENGTH_LONG).show();
        }
        else {
//            String time = "";
            if (addNew) {
                //Add new poster
                if (kioskID == -1) {
                    poster = new Poster(name, org, location, getDateString(), description);
//                    if (validateAndSetDate()) {
                        poster.save();
                        finish();
//                    }
                }
                //Add new poster and autoconnect it to a kiosk
                else {
                    poster = new Poster(name, org, location, getDateString(), description);
//                    if (validateAndSetDate()) {
                        poster.increaseCount();
                        poster.save();
                        Kiosk kiosk = Kiosk.findById(Kiosk.class, kioskID);
                        KioskPoster kp = new KioskPoster(kiosk, poster);
                        kp.save();
                        finish();
//                    }
                }
            }
            else {
                //validateAndSetDate();
//                if (validateAndSetDate()) {
                    poster.modify(name, org, location, getDateString(), description);
                    poster.save();
                    finish();
//                }
            }
        }
    }

    private boolean alreadyContains(String title) {
        List<Poster> allPosters = Poster.listAll(Poster.class);
        for (Poster poster : allPosters) {
            if (poster.title().equals(title))
                return true;
        }
        return false;
    }

    //
    private void restoreDateInfo() {
        String yyyymmdd = poster.getDetailArray()[3];
        if (yyyymmdd.length()>0) {
            date.set(Calendar.MONTH, Integer.parseInt(yyyymmdd.substring(4, 6))-1);
            date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(yyyymmdd.substring(6)));
            date.set(Calendar.YEAR, Integer.parseInt(yyyymmdd.substring(0, 4)));
        }
    }

//    private boolean validateAndSetDate() {
//        String month = monthField.getText().toString();
//        String date = dateField.getText().toString();
//        String year = yearField.getText().toString();
//        if (month.length()>0 || date.length()>0 || year.length()>0) {
//            if (month.length()==0 || date.length()==0 || year.length()==0) {
//                Toast.makeText(getApplicationContext(),
//                        "Date not properly filled out", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            else {
//                int monthInt = integerVerifier(month);
//                int dateInt = integerVerifier(date);
//                int yearInt = integerVerifier(year);
//                if (!(monthInt == -1 && dateInt == -1 && yearInt == -1)) {
//                    if (monthInt > 12 || monthInt < 1) {
//                        Toast.makeText(getApplicationContext(),
//                                "Invalid Month", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                    else {
//                        if (((dateInt <= 31) && (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10 || monthInt == 12)) ||
//                                ((dateInt <= 30) && (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11)) ||
//                                ((dateInt <= 29) && monthInt == 2)) {
//                            String dateString = padDateInts(yearInt, 4) + padDateInts(monthInt, 2) + padDateInts(dateInt, 2);
//                            poster.modify(null, null, null, dateString, null);
//                            poster.save();
//                            return true;
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    "Invalid Date", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }
//                    }
//                }
//                else {
//                    Toast.makeText(getApplicationContext(),
//                            "Date Entered is invalid", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
//        }
//        else {
//            return true;
//        }
//    }

    private String getDateString() {
        String result = "";
        if (!datePicker.getText().equals(getString(R.string.date_picker_btn))) {
            Log.d("getDateString", "In if statement.");
            result += padDateInts(date.get(Calendar.YEAR), 4);
            result += padDateInts(date.get(Calendar.MONTH)+1, 2);
            result += padDateInts(date.get(Calendar.DAY_OF_MONTH), 2);
        }
        Log.d("getDateString", "datePicker.getText = "+datePicker.getText());

        return result;
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
            if (addNew) {
                //deleteCancelButton();
                cancel();
            }
            else {
                ConfirmDeleteFragment confirmDelete = new ConfirmDeleteFragment(this);
                confirmDelete.show(getFragmentManager(), "Delete");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cancel() {
        finish();
    }

    public void delete() {
        List<Poster> allPosters = Poster.listAll(Poster.class);
        Log.d("TAG", "allPosters size = " + allPosters.size());
        allPosters.remove(poster);
        List<KioskPoster> allKPs = KioskPoster.listAll(KioskPoster.class);
        for (KioskPoster kp : allKPs) {
            if (kp.matchPoster(poster))
                kp.delete();
        }
        poster.delete();
        finish();
    }

}
