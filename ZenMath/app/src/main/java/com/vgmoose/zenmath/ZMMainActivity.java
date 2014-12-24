package com.vgmoose.zenmath;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.*;
import android.widget.TextView;

public class ZMMainActivity extends Activity implements OnClickListener
{
    Button[] buttons = new Button[12];
    static TextView text, equation;

    static int score=0;

    static int upper = 99;
    static int lower = 10;

    int[] keypad = {1,2,3,4,5,6,7,8,9};
//	int[] keypad = {7,8,9,4,5,6,1,2,3};

    boolean answered = false;
    int answer;

    static String str;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int startid = R.id.button1;

        SeekBar sb1 = (SeekBar) (findViewById(R.id.seekBar1));
        SeekBar sb2 = (SeekBar) (findViewById(R.id.SeekBar2));


        text = (TextView) (findViewById(R.id.textView1));
        equation = (TextView)(findViewById(R.id.textView2));
        text.setText("");


        for (int x=0;x<12;x++)
        {
            buttons[x] = (Button)(findViewById(startid+x));
            buttons[x].setHeight(50);
            buttons[x].setWidth(50);

            String str;
            switch (x)
            {
                case (11):
                    str = "-";
                    break;
                case (9):
                    str = ""+(0);
                    break;
                case (10):
                    str = "c";
                    break;
                default:
                    str = ""+keypad[x];
            }

            buttons[x].setText(str);
            buttons[x].setOnClickListener(this);
//			buttons[x].setHeight(100);

        }
        final int defheight = 50;//buttons[0].getHeight();
        final int defwidth = 50; //buttons[0].getWidth();

        OnSeekBarChangeListener sb1l = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                for (Button b : buttons)
                    b.setWidth(defwidth+progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        OnSeekBarChangeListener sb2l = new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                for (Button b : buttons)
                    b.setHeight(defheight+progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        sb1.setOnSeekBarChangeListener(sb1l);
        sb2.setOnSeekBarChangeListener(sb2l);

        //		text.setText(buttons[0].getText());
        //        equation.setText("Tap any button to begin");

        new Thread()
        {
            @Override
            public void run()
            {
                startGame(20);
            }
        }.start();

    }

    @SuppressWarnings("static-access")
    public void startGame(int x)
    {
        score = 0;
        long start = System.currentTimeMillis();

        while (score<x)
        {
            question();
            score++;
        }
        int time = (int)((System.currentTimeMillis()-start)/1000);

        int min=0;
        String str = "";
        while (true)
        {
            if (time<60)
            {
                this.str = str+time+"s";
                handler.sendEmptyMessage(0);
                break;
            }

            time-=60;
            min++;

            str =""+min+"m ";
        }
    }

    public void question()
    {
        int operation = (int)(Math.random()*4);

        str = "";
        answer = 0;
        int begin, end;

        do
        {
            begin = (int)(Math.random()*upper+lower);
            end = (int)(Math.random()*upper+lower);
        }while (end==0);

        switch (operation)
        {
            case (0):
                str="+";
                answer = begin+end;
                break;
            case (1):
                str="-";
                answer = begin-end;
                break;
            case (2):
                str="*";
                answer = begin*end;
                break;
            case (3):
                str="/";
                answer = begin*end;

                int temp = answer;
                answer = begin;
                begin = temp;
                break;
        }

        str=""+begin+str+end;

        answered = false;
        handler.sendEmptyMessage(0);

        while (!answered)
        {

        }


    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what==0) // Scan button ending
            {
                equation.setText(str);
            }

        }
    };

    public void onClick(View v)
    {
        if (v==buttons[10])
            text.setText("");
            //		else if (v==buttons[11])
            //		{
            //
            //			if (text.getText().length()!=0)
            //				text.setText(""+(text.getText()+"").substring(0, (text.getText()+"").length()-1));
            //		}
        else if (v==buttons[11])
        {
            if ((""+text.getText()).length()==0)
                text.setText("-");
            else if ((""+text.getText()).charAt(0)!='-')
                text.setText("-"+text.getText());
            else
                text.setText((""+text.getText()).substring(1,(""+text.getText()).length()));

        }
        else if (v==buttons[9])
            text.setText(text.getText()+""+buttons[9].getText());
        else
        {
            for (int x=0;x<9;x++)
                if (v==buttons[x])
                {
                    text.setText(text.getText()+""+buttons[x].getText());
                    break;
                }
        }

        try
        {
            if (((""+text.getText()).length()!=0) && (((""+text.getText()).charAt(0)!='-' || (""+text.getText()).length()!=1)) && answer==Integer.parseInt(""+text.getText()))
            {
                answered=true;
                text.setText("");
            }
        }
        catch (Exception e)
        {
            text.setText("");
        }
    }
}