package com.example.list6_crime;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DetailActivity extends AppCompatActivity {

    Button delete;
    CheckBox checkBox;
    Button selectDate;
    Calendar date;
    EditText crime_Title;
    private DBHandler dbHandler;
    Intent intent;
    int currentCrimeId;
    String currentCrimeTitle;
    boolean currentCrimeIsSolved;
    String currentCrimeDate;
    ImageButton addImage;
    ImageView viewImage;
    Button saveImage;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_INTENT = 2;
    private Uri savePicturePath = null;
    String currentCrimePicture;



    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        crime_Title =findViewById(R.id.crime_title);;
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        selectDate = findViewById(R.id.btnDate);
        delete = findViewById(R.id.btnDelete);
        addImage = findViewById(R.id.imageButton);
        viewImage = findViewById(R.id.show_photo);
        saveImage = findViewById(R.id.save_taken_image);

        dbHandler = new DBHandler(this);

        intent = getIntent();
        currentCrimeId = intent.getIntExtra("id", 0);
        currentCrimeTitle = intent.getStringExtra("title");
        currentCrimeIsSolved = intent.getBooleanExtra("solved", false);
        currentCrimeDate = intent.getStringExtra("date");
        currentCrimePicture = intent.getStringExtra("picture");


        crime_Title.setText(currentCrimeTitle);
        selectDate.setText(currentCrimeDate);

        if (currentCrimeIsSolved)
            checkBox.setChecked(true);

        viewImage.setImageURI(Uri.parse(currentCrimePicture));

        addImage.setOnClickListener(v -> {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Please select action");

            String[] dialogItems = {"Capture Picture"};

            pictureDialog.setItems(dialogItems, (dialog, which) -> {
               switch (which) {
                   case 0:
                       takePhoto();
                       break;
               }
            });
            pictureDialog.show();
        });

        saveImage.setOnClickListener(v -> {
            if (savePicturePath == null){
                Toast.makeText(this, "First take picture", Toast.LENGTH_SHORT).show();
            } else  {
                dbHandler.updateStudent(currentCrimeId, null, currentCrimeIsSolved, null, savePicturePath.toString());
            }
        });


        crime_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbHandler.updateStudent(currentCrimeId, String.valueOf(s), currentCrimeIsSolved, null, null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.updateStudent(currentCrimeId, null, ((CompoundButton) view).isChecked(), null, null);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteStudent(currentCrimeId);
                finish();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                date = Calendar.getInstance();
                new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                dbHandler.updateStudent(currentCrimeId, null, currentCrimeIsSolved, date.getTime(), null);
                                selectDate.setText(date.getTime().toString());

                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_INTENT){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                viewImage.setImageBitmap(thumbnail);
                savePicturePath = savePicture(thumbnail);

            }
        }
    }

    private void takePhoto() {
        Dexter.withContext(this).withPermission(
                Manifest.permission.CAMERA
        ).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_INTENT);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showRationalDialog();
            }
        }).onSameThread().check();
    }

    private void showRationalDialog() {
        new AlertDialog.Builder(this)
                .setMessage("This feature requires permission")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .show();

    }

    private Uri savePicture(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("Gallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");
        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Uri.parse(file.getAbsolutePath());
    };

}
