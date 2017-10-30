package com.example.unrea.galleryapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void dateFilter(View view)
    {
        String start = ((EditText)findViewById(R.id.dateText1)).getText().toString();
        String end = ((EditText)findViewById(R.id.dateText2)).getText().toString();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("MESSAGE", start+ " "+end);
        startActivity(intent);
    }


}
