package com.example.unrea.galleryapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import static android.media.ExifInterface.*;

public class MainActivity extends AppCompatActivity {

    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    GridViewAdapter adapter;
    File file;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loadPictures();
        if(bundle != null && bundle.getChar("type") == 'd') {
            dateFilter(bundle.getString("dateStart"),bundle.getString("dateEnd"));
        }

    }

    private void loadPictures() {
        File file = new File("/sdcard/Pictures");
        listFile = file.listFiles();
        // Create a String array for FilePathStrings
        FilePathStrings = new String[listFile.length];
        // Create a String array for FileNameStrings
        FileNameStrings = new String[listFile.length];

        for (int i = 0; i < listFile.length; i++) {
            // Get the path of the image file
            FilePathStrings[i] = listFile[i].getAbsolutePath();
            // Get the name image file
            FileNameStrings[i] = listFile[i].getName();
        }

        // Locate the GridView in gridview_main.xml
        grid = (GridView) findViewById(R.id.gridview);
        // Pass String arrays to LazyAdapter Class
        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        // Set the Adapter to the GridView
        grid.setAdapter(adapter);
    }

    private void dateFilter(String start, String end) {
        try {
            DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
            Date startDate = df.parse(start);
            Date endDate = df.parse(end);

            ArrayList<String> filesPath = new ArrayList<String>();
            ArrayList<String> filesName = new ArrayList<String>();

            for(int i = 0; i < listFile.length; i++) {
                ExifInterface exif = new ExifInterface(FilePathStrings[i]);
                String t = exif.getAttribute(ExifInterface.TAG_DATETIME);
                Date test = df.parse(t);
                if(!test.before (startDate) && !test.after (endDate)) {
                    filesPath.add(FilePathStrings[i]);
                    filesName.add(FileNameStrings[i]);
                }
            }
            FileNameStrings = (String[]) filesName.toArray();
            FilePathStrings = (String[]) filesPath.toArray();

            grid = (GridView) findViewById(R.id.gridview);
            // Pass String arrays to LazyAdapter Class
            adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
            // Set the Adapter to the GridView
            grid.setAdapter(adapter);

        } catch (Exception e) {
            String t = e.getMessage();

        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File("/sdcard/Pictures");
        //File storageDir =  getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }



    static final int REQUEST_TAKE_PHOTO = 1;

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //todo: handle the error.
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            loadPictures();
            try {
                ExifInterface exif = new ExifInterface(photoFile.getAbsolutePath());
                String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                exif.setAttribute(ExifInterface.TAG_DATETIME, date);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
                exif.saveAttributes();
            } catch (Exception e) {
                //herp
            }

        }




    }

    public void filter_Click(View v) {
        startActivity(new Intent(this, FilterActivity.class));
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
            if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


}