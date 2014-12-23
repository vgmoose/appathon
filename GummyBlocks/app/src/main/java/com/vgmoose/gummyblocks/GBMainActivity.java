package com.vgmoose.gummyblocks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class GBMainActivity extends Activity {

    static GBMainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbmain);
        main = this;
        startNewGame(this);
    }

    /**
     * Starts a single new TetrisGame. The window will popup and display when popup() is called.
     */
    public static void startNewGame(Context c)
    {
//        TetrisGame tg = new TetrisGame(c);
//        tg.popup();

        //		TetrisGame tg2 = new TetrisGame();
        //		tg2.popup();
        //
        //		TetrisGame tg3 = new TetrisGame();
        //		tg3.popup();
    }

    /**
     * Saves the TetrisGame object to a file using Serialization.
     * @param t
     */
    public static void save(TetrisGame t)
    {
        try
        {
            FileOutputStream fileOut =
                    new FileOutputStream("tetris.sav");
            ObjectOutputStream out =
                    new ObjectOutputStream(fileOut);
            out.writeObject(t);
            out.close();
            fileOut.close();
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }

    /**
     * Restores a TetrisGame file that was saved.
     * @return
     */
    public static boolean restore()
    {
        TetrisGame t = null;
        try
        {
            FileInputStream fileIn =
                    new FileInputStream("tetris.sav");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            t = (TetrisGame) in.readObject();
            in.close();
            fileIn.close();

            t.popup();
            return true;
        }catch(Exception i)
        {
            //           i.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gbmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
