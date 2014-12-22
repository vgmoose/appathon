package com.vgmoose.colorshade;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.*;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.*;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class ColorService extends Service {
    HUDView mView;
    private WindowManager windowManager;
    WindowManager.LayoutParams lp;
    int counter = 0;
    int dimen;
    static ColorService me;


    java.lang.Process thisProcess;
    RunningAppProcessInfo pausedProcess;

    final Runtime runtime = Runtime.getRuntime();

    boolean paused = false;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        mView = new HUDView(this);

        int nFlags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                nFlags,
                PixelFormat.TRANSLUCENT);

        CSMainActivity.that.reCalcWidthAndHeight();

        dimen = Math.max(CSMainActivity.that.height, CSMainActivity.that.width);

        lp.height = dimen;
        lp.width = dimen;

//        Log.v("aaaaa", "hi there" + lp.height +" " + lp.width);

        lp.gravity = Gravity.TOP | Gravity.LEFT;

//        mView.setOnTouchListener(this);
        windowManager.addView(mView, lp);

        Builder builder = new Builder(this);
        builder.setContentTitle("Color Shade is running");
        builder.setContentText("Tap here to adjust color or disable.");
        builder.setSmallIcon(R.drawable.ic_launcher);

        Intent mainscreen = new Intent(this, CSMainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, mainscreen, PendingIntent.FLAG_UPDATE_CURRENT));

        Notification n = builder.build();
        startForeground(1, n);

//        loginRoot();

    }


    static public void writeShade()
    {
        // get color and brightness from picker and slider
        int c = CSMainActivity.that.cp.getColor();
        int a = CSMainActivity.that.sb.getProgress();

        int r = Color.red(c);
        int g = Color.green(c);
        int b = Color.blue(c);

        SharedPreferences.Editor e = CSMainActivity.that.prefs.edit();

        // save these colors for the next time
        e.putInt("red", r);
        e.putInt("green", g);
        e.putInt("blue", b);
        e.putInt("alpha", a);

        // save em
        e.commit();


    }

//    public boolean loginRoot()
//    {
//        try {
//            thisProcess = Runtime.getRuntime().exec("su");
//            return true;
//        } catch (IOException e1) {
//            Toast.makeText(this,"Got root?", Toast.LENGTH_LONG).show();
//            return false;
//        }
//    }

//    public boolean RunAsRoot(String[] cmds){
//
//        DataOutputStream os = new DataOutputStream(thisProcess.getOutputStream());
//        for (String tmpCmd : cmds) {
//            try {
//                os.writeBytes(tmpCmd+"\n");
//                os.flush();
//            } catch (Exception e) {
//                Toast.makeText(this,"Got root?", Toast.LENGTH_LONG).show();
//            }
//        }
//        return true;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
//        String[] aa = {"exit"};
//        RunAsRoot(aa);
        me = null;
        if(mView != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView = null;
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP)
//        {
//            if (counter > 10)
//            {
//                counter = 0;
//                return true;
//            }
//            counter = 0;
//            String key;
//
//            RunningAppProcessInfo current;
//
//            if (!paused)
//            {
//                key = "STOP";
//                current = getForegroundProcess();
//                pausedProcess = current;
//            }
//            else
//            {
//                key = "CONT";
//                current = pausedProcess;
//            }
//
//
//            String[] ss = {"kill -"+key+" "+current.pid};
//            if (RunAsRoot(ss))
//            {
//                paused = !paused;
//                mView.invalidate();
//            }
//
//            Log.v("Tap",""+event.getX()  + "," + event.getY());
//        }
//        if (event.getAction() == MotionEvent.ACTION_MOVE)
//        {
//            counter ++;
//            if (counter%2 == 0)
//            {
//                lp.x += (int) event.getX() - 50;
//                lp.y += (int) event.getY() - 80;
//                windowManager.updateViewLayout(mView, lp);
//
//                Log.v("Move",""+(int)(event.getX())  + "," +(int) event.getY());
//            }
//        }
//        return true;
//    }

//    public RunningAppProcessInfo getForegroundProcess()
//    {
//        ActivityManager  manager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
//        for (ActivityManager.RunningAppProcessInfo process : listOfProcesses)
//        {
//            if (process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.pid != Process.myPid())
//            {
//                Log.v("Foreground: ", ""+ (process.processName));
//                return process;
//            }
//        }
//        return null;
//    }

}

class HUDView extends View {
    private Paint mLoadPaint;
    ColorService ps;

    public HUDView(Context context) {
        super(context);
        this.ps = (ColorService) context;
        Toast.makeText(getContext(),"Color Shade Started", Toast.LENGTH_SHORT).show();

        mLoadPaint = new Paint();
        mLoadPaint.setAntiAlias(true);
        mLoadPaint.setTextSize(10);
        mLoadPaint.setARGB(255, 255, 0, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //        mLoadPaint.setColor(Color.argb(100, 255, 255, 255));
        //        canvas.drawRect(10, 10, 130, 150, mLoadPaint);

        if (!ps.paused)
        {

            // get color and brightness from picker and slider
            int c = CSMainActivity.that.cp.getColor();
            int a = CSMainActivity.that.sb.getProgress();

            int r = Color.red(c);
            int g = Color.green(c);
            int b = Color.blue(c);

            // set color
            mLoadPaint.setColor(Color.argb(a, r, g, b));

            canvas.drawRect(-20, -20, ps.dimen+100, ps.dimen+100, mLoadPaint);
//            mLoadPaint.setColor(Color.argb(200, 255, 0, 0));
//            mLoadPaint.setStyle(Style.FILL);
//            mLoadPaint.setStrokeWidth(0);
//
//            canvas.drawRect(30, 30, 60, 130, mLoadPaint);
//            canvas.drawRect(80, 30, 110, 130, mLoadPaint);
//
//            mLoadPaint.setColor(Color.BLACK);
//            mLoadPaint.setStyle(Style.STROKE);
//            mLoadPaint.setStrokeWidth(5);
//
//            canvas.drawRect(30, 30, 60, 130, mLoadPaint);
//            canvas.drawRect(80, 30, 110, 130, mLoadPaint);
        }
        else
        {
            Path path = new Path();
            path.moveTo(30, 30);
            path.lineTo(110, 80);
            path.lineTo(30, 130);
            path.lineTo(30, 30);
            path.close();

            mLoadPaint.setColor(255 - Color.argb(200, 255, 0, 0));
            mLoadPaint.setStyle(Style.FILL);
            mLoadPaint.setStrokeWidth(0);

            canvas.drawPath(path, mLoadPaint);

            mLoadPaint.setColor(Color.BLACK);
            mLoadPaint.setStyle(Style.STROKE);
            mLoadPaint.setStrokeWidth(5);

            canvas.drawPath(path, mLoadPaint);
        }

    }

    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }



}