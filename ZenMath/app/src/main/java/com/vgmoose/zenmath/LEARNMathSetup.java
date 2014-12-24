package com.vgmoose.zenmath;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class LEARNMathSetup extends Activity implements OnClickListener
{
    static Spinner probs, base;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        probs = (Spinner) (findViewById(R.id.spinner1));
        base = (Spinner) (findViewById(R.id.spinner2));



        String[] problems = new String[15];
        String[] maths = {"Binary (2)","Octal (8)","Decimal (10)","Hexadecimal (16)"};

        for (int x=0; x<15; x++)
            problems[x] = ""+((x+1)*10);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, problems);
        probs.setAdapter(spinnerArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, maths);
        base.setAdapter(spinnerArrayAdapter);


    }



    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what==0) // Scan button ending
            {
            }

        }
    };

    public void onClick(View v)
    {

    }
}