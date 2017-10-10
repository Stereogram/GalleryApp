package com.example.unrea.galleryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void dateFilter()
    {
        String start = ((EditText)findViewById(R.id.dateText1)).getText().toString();
        String end = ((EditText)findViewById(R.id.dateText2)).getText().toString();

        System.out.println(start + " | " + end);
    }


}
