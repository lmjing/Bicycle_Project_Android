package com.example.lmjin_000.pedarro;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
;

/**
 * Created by lmjin_000 on 2015-12-08.
 */
public class BikeTrouble extends Activity implements CompoundButton.OnCheckedChangeListener{
    TextView title,text;
    Button btn_report;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.troble_report);

        init();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font_L.otf");
        title.setTypeface(tf);
        btn_report.setTypeface(tf);
    }

    private void init() {
        title = (TextView)findViewById(R.id.title);
        btn_report = (Button)findViewById(R.id.btn_report);
        edit = (EditText)findViewById(R.id.detail);
        text = (TextView)findViewById(R.id.text);
    }

    int jumsu = 0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox1:
                if(isChecked)
                    jumsu+=5;   break;
            case R.id.checkbox2:
                if(isChecked)
                    jumsu+=3;   break;
            case R.id.checkbox3:
                if(isChecked)
                    jumsu+=1;   break;
            case R.id.checkbox4:
                if(isChecked)
                    jumsu+=1;   break;
            case R.id.checkbox5:
                if(isChecked)
                    jumsu+=5;   break;
            case R.id.checkbox6:
                if(isChecked)
                    jumsu+=1;   break;
            case R.id.checkbox7:
                if(isChecked)
                    jumsu+=2;   break;
            case R.id.checkbox8:
                if(isChecked)
                    jumsu+=4;   break;
            case R.id.checkbox9:
                if(isChecked)
                    jumsu+=1;   break;
        }
        text.setText("점수 : "+Integer.toString(jumsu));
    }
}
