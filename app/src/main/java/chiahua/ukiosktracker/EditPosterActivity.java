package chiahua.ukiosktracker;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditPosterActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 15;
    EditText nameField;
    EditText orgField;
    EditText locationField;
    EditText descriptionField;
    Poster poster;
    private String mCurrentPhotoPath;
    private Bitmap mImageBitmap;
    private ImageView mImageView;

    boolean addNew;
    private boolean cameraAccess = true;
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
            if (cameraAccess) {
                ImageButton ib = (ImageButton) findViewById(R.id.camera_icon);
                ImageView iv = (ImageView) findViewById(R.id.poster_image);
                Log.d("TAG", "Creating on click listener.");
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int permissionCheck = ContextCompat.checkSelfPermission(EditPosterActivity.this,
                                Manifest.permission.CAMERA);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            invokeCamera();
                        } else {
                            ActivityCompat.requestPermissions(EditPosterActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST_CODE);
                        }
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
                    invokeCamera();
                }
                else {
                    cameraAccess = false;
                }
                return;
            }
        }
    }

    private void invokeCamera() {
        Log.d("TAG", "Camera icon clicked.");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
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


    private File createImageFile() throws IOException {
        // Create an image file name
        Log.d("TAG", "Creating Image File");
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
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "Starting onActivityResult Method");
        Log.d("TAG", "Request Code is: " + requestCode);
        Log.d("TAG", "Result code is: " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("TAG", "Request to take photo OK");
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        Uri.parse(mCurrentPhotoPath));
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            //Poster.saveInTx(allPosters);
            //SugarRecord.saveInTx(allPosters);
            //TODO: Deleting a poster causes endless crashes until uninstall. Suspect: Upon remove, need to also remove relevant KioskPosters
            poster.delete();
        }
        finish();
    }

}
