package com.zhongzilu.tuner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by zhongzilu on 16-5-18.
 */
public class MainActivity extends AppCompatActivity {

    DialView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new DialView(this, null);
        setContentView(view);

    }

}
