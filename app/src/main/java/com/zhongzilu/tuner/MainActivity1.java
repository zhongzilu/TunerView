package com.zhongzilu.tuner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity1 extends AppCompatActivity {

    private CheckBox mStartButton;
    private DialView dialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialView = new DialView(this, null);
        mStartButton = (CheckBox) findViewById(R.id.startButton);
        mStartButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mStartButton.setText(isChecked ? "STOP" : "START");
//                dialView.setValue(30);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Can I help you?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
