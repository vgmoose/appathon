package com.vgmoose.colorshade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.View;
import android.graphics.*;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class CSMainActivity extends Activity {

    AdView adView;
    static boolean activated = false;
    static CSMainActivity that;
    static Intent serviceIntent;
    int height, width;

    SharedPreferences prefs;

    ColorPicker cp;
    SeekBar sb;

    public void reCalcWidthAndHeight()
    {
        Display display = getWindowManager().getDefaultDisplay();

        width = display.getWidth();
        height = display.getHeight();
    }

    public String getText()
    {
        if (activated)
            return "Stop Color Shade";
        else
            return "Start Color Shade";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csmain);
//        Log.v("whwhiiwWEWewewewe", "you are a liar");

        cp = (ColorPicker) findViewById(R.id.colorPicker);
        sb = (SeekBar) findViewById(R.id.seekBar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ColorService.writeShade();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // repaint
                if (ColorService.me != null)
                    ColorService.me.mView.invalidate();
            }
        });

        that = this;
        prefs = this.getSharedPreferences("com.vgmoose.colorshade", Context.MODE_PRIVATE);

        final Button b = (Button) findViewById(R.id.button);

        // load old colors
        int r = prefs.getInt("red", 0);
        int b2 = prefs.getInt("blue", 0);
        int g = prefs.getInt("green", 0);
        int a = prefs.getInt("alpha", 180);

        sb.setMax(255);

        // set em
        cp.setColor(Color.rgb(r, g, b2));
        sb.setProgress(a);

        b.setText(getText());
        b.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!activated)
                {
                    serviceIntent = new Intent(that, ColorService.class);
                    startService(serviceIntent);
                    activated = true;
                    b.setText(getText());
                    ColorService.writeShade();
                    onBackPressed();
                }
                else
                {
                    stopService(serviceIntent);
                    activated = false;
                    b.setText(getText());
                }
            }
        });


        // Create the adView.
        adView = new AdView(this);

        adView.setAdUnitId("ca-app-pub-8148658375496745/9113824509");
        adView.setAdSize(AdSize.BANNER);

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout".
        LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);

        // Add the adView to it.
        layout.addView(adView);

        // Initiate a generic request.
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load the adView with the ad request.
        adView.loadAd(adRequest);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_csmain, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
