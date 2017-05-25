package com.example.lmjin_000.pedarro;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by lmjin_000 on 2015-12-08.
 */
public class BikeSearch extends Activity {
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_state);

        init();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font_L.otf");
        title.setTypeface(tf);
    }

    private void init() {
        title = (TextView)findViewById(R.id.title);
    }
}
