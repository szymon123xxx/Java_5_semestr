package com.example.todoapp;

import android.Manifest;
import android.annotation.SuppressLint;
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

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {

    String currentTaskDate;
    String currentTaskTitle;
    String currentTaskPicture;

    Button selectDate;
    int currentTaskId;
    boolean currentTaskCompleted;
    private DBHandler dbHandler;

    CheckBox checkBoxTask;
    Intent intent;
    Calendar date;
    ImageButton imageAdd;
    Button saveImage;

    EditText task_Title;
    private static final int CAMERA_INTENT = 2;
    private Uri savePicturePath = null;
    ImageView imageView;
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        task_Title = findViewById(R.id.title);
        selectDate = findViewById(R.id.button);
        checkBoxTask = findViewById(R.id.checkBox2);
        imageAdd = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView3);
        saveImage = findViewById(R.id.buttonSave);
        dbHandler = new DBHandler(this);

        intent = getIntent();
        currentTaskId = intent.getIntExtra("id", 0);
        currentTaskTitle = intent.getStringExtra("title");
        currentTaskCompleted = intent.getBooleanExtra("solved", false);
        currentTaskDate = intent.getStringExtra("date");
        currentTaskPicture = intent.getStringExtra("picture");


        task_Title.setText(currentTaskTitle);
        selectDate.setText(currentTaskDate);
        imageView.setImageURI(Uri.parse(currentTaskPicture));

        if (currentTaskCompleted)
            checkBoxTask.setChecked(true);

        checkBoxTask.setOnClickListener(view -> dbHandler.updateTask(currentTaskId, null, ((CompoundButton) view).isChecked(), null, null));

        selectDate.setOnClickListener(v -> {
            final Calendar currentDate = Calendar.getInstance();
            date = Calendar.getInstance();
            new DatePickerDialog(DetailActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(DetailActivity.this, (view1, hourOfDay, minute) -> {
                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date.set(Calendar.MINUTE, minute);
                    dbHandler.updateTask(currentTaskId, null, currentTaskCompleted, date.getTime(), null);
                    selectDate.setText(date.getTime().toString());

                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        });


        task_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dbHandler.updateTask(currentTaskId, String.valueOf(s), currentTaskCompleted, null, null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageAdd.setOnClickListener(v -> {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Please select action");

            String[] dialogItems = {"Take Picture"};

            pictureDialog.setItems(dialogItems, (dialog, which) -> {
                if (which == 0) {
                    takePhoto();
                }
            });
            pictureDialog.show();
        });

        saveImage.setOnClickListener(v -> {
            if (savePicturePath == null){
                Toast.makeText(this, "Take picture", Toast.LENGTH_SHORT).show();
            } else  {
                dbHandler.updateTask(currentTaskId, null, currentTaskCompleted, null, savePicturePath.toString());
            }
        });


    }

    private void showRationalDialog() {
        new AlertDialog.Builder(this)
                .setMessage("This feature need permission")
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
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_INTENT){
                assert data != null;
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(thumbnail);
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
    }

}
