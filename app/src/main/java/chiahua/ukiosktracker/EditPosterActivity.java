package chiahua.ukiosktracker;


import android.Manifest;
import android.app.DatePickerDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
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

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 15;
    public static final int FILE_WRITE_PERMISSION_REQUEST_CODE = 16;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    private boolean cameraAccess = true;
    private boolean fileAccess = false;
    static final int REQUEST_TAKE_PHOTO = 1;


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

        mImageView = (ImageView) findViewById(R.id.poster_image);

        Intent receivedIntent = getIntent();
        addNew = receivedIntent.getBooleanExtra("addNew", true);
        kioskID = receivedIntent.getIntExtra("KioskID", -1);
        Log.d("TAG", "KioskID is: " + kioskID);
        Log.d("TAG", "addNew is: " + addNew);
        if (!addNew) {
            Log.d("TAG", "Editing Poster");
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

            mCurrentPhotoPath = poster.getImagePath();
            if (mCurrentPhotoPath == null){
                Log.d("TAG", "mCurrentPhotoPath is null.");
                mImageView.setImageResource(R.drawable.noimageavailable);
            } else {
                Log.d("TAG", "Setting Image Bitmap to mImageView");
                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                            Uri.parse(mCurrentPhotoPath));
                    mImageView.setImageBitmap(mImageBitmap);
                } catch (IOException e) {
                    Log.d("TAG", "IOException in poster load");
                    mImageView.setImageResource(R.drawable.noimageavailable);
                }
            }

        }
        else {
            setTitle(R.string.add_new_poster);
            Log.d("TAG", "Adding New Poster.");
            if (cameraAccess) {
                ImageButton ib = (ImageButton) findViewById(R.id.camera_icon);
                Log.d("TAG", "Creating on click listener.");
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int permissionCheck = ContextCompat.checkSelfPermission(EditPosterActivity.this,
                                Manifest.permission.CAMERA);
                        int permissionCheck2 = ContextCompat.checkSelfPermission(EditPosterActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if ((permissionCheck == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED)
                                ||(fileAccess && cameraAccess)) {
                            Log.d("TAG", "Permissions Granted, Invoking Camera");
                            invokeCamera();
                            //return;
                        } else {
                            Log.d("TAG", "Requesting Camera Permissions.");
                            ActivityCompat.requestPermissions(EditPosterActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST_CODE);
                            Log.d("TAG", "Requesting Write External Storage Permissions.");
                            ActivityCompat.requestPermissions(EditPosterActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    FILE_WRITE_PERMISSION_REQUEST_CODE);
                        }
                        /*if (fileAccess && cameraAccess) {
                            Log.d("TAG", "fileAccess and cameraAccess are true.");
                            invokeCamera();
                            return;
                        }*/
                    }
                });
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cameraAccess = true;
                }
                else {
                    cameraAccess = false;
                }
                Log.d("TAG", "cameraAccess is: " + cameraAccess);
            }
            case FILE_WRITE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fileAccess = true;
                }
                else {
                    fileAccess = false;
                }
                Log.d("TAG", "fileAccess is: " + fileAccess);
                return;
            }
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
        datePicker.setText((date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.YEAR));}


    private void invokeCamera() {
        Log.d("TAG", "Camera icon clicked.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                Log.d("TAG", "Creating Image file");
                photoFile = createImageFile();
            }
            catch (IOException ex){
                // Error occurred while creating the file.
                Log.d("TAG", "IOException for createImageFile method.");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("TAG", "Accessing Camera...");
                Uri photoURI = FileProvider.getUriForFile(EditPosterActivity.this, "chiahua.ukiosktracker.provider",
                        photoFile);
                Log.d("TAG", "Photo URI is: " + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.d("TAG", "Placed Extra in Picture Intent");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
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
        } else if (id == R.id.delete_cancel) {
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


    private File createImageFile() throws IOException {
        // Create an image file name
        Log.d("TAG", "Creating Image File in createImageFile Method");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("TAG", "Current Photo Path: " + mCurrentPhotoPath);
        if(poster != null) {
            Log.d("TAG", "Poster is not null.");
            poster.setImagePath(mCurrentPhotoPath);
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG", "Starting onActivityResult Method");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "Request Code is: " + requestCode);
        Log.d("TAG", "Result code is: " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("TAG", "Request to take photo OK");
            try {
                Log.d("TAG", "Getting Image Bitmap");
                Log.d("TAG", "mCurrentPhotoPath is: " + mCurrentPhotoPath);
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        Uri.parse(mCurrentPhotoPath));
                Log.d("TAG", "Setting Image Bitmap to mImageView");
                mImageView.setImageBitmap(mImageBitmap);
                poster.setImagePath(mCurrentPhotoPath);
            } catch (IOException e) {
                Log.d("TAG", "IOException in onActivityResult");
                e.printStackTrace();
            }
            Log.d("TAG", "Hitting return on onActivityResult");
            return;
        }
        //Log.d("TAG", "Using super.onActivityResult");
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
        }
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
