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
        Bundle dataMap = new Bundle();
        dataMap.putChar("type",'d');
        dataMap.putString("dateStart", start);
        dataMap.putString("dateEnd", end);
        intent.putExtras(dataMap);
        startActivity(intent);
    }

    public void locationFilter(View view)
    {
        String lat1 = ((EditText)findViewById(R.id.lat1)).getText().toString();
        String long1 = ((EditText)findViewById(R.id.long1)).getText().toString();
        String lat2 = ((EditText)findViewById(R.id.lat2)).getText().toString();
        String long2 = ((EditText)findViewById(R.id.long2)).getText().toString();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, MainActivity.class);
        Bundle dataMap = new Bundle();
        dataMap.putChar("type",'l');
        dataMap.putString("lat1", lat1);
        dataMap.putString("long1", long1);
        dataMap.putString("lat2", lat2);
        dataMap.putString("long2", long2);
        intent.putExtras(dataMap);
        startActivity(intent);
    }

    public void captionFilter(View view)
    {
        String caption = ((EditText)findViewById(R.id.captionText)).getText().toString();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, MainActivity.class);
        Bundle dataMap = new Bundle();
        dataMap.putChar("type",'c');
        dataMap.putString("lat1", caption);
        intent.putExtras(dataMap);
        startActivity(intent);
    }


}
