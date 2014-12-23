package com.vgmoose.gummyblocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The object that manages the the falling of pieces and the entire game.
 *
 * @author Ricky
 *
 */
public class TetrisGame extends View implements View.OnTouchListener
{
    private static final long serialVersionUID = 1L;

    static int width = 10;
    static int height = 20;

    boolean swappedonce = false;
    boolean gameover = false;
    int interval = 500;

    Tetromino active;
    Tetromino swapped;

    int nextone;
    boolean noMove;
    int score;
    float touchDownX, touchDownY;

//    private Container c;
    //	ArrayList <Tetromino> pieces;
    int cap;

    Random tkind;
    Tetromino bgpiece;
    Timer timer;

    boolean adjusted = false, slamLocked = false;

    Context context;

    int [][] board;

    /**
     * The constructor for the TetrisGame that sets the active piece and next piece into motion.
     */
    public TetrisGame(Context c, AttributeSet attrs)
    {
        super(c, attrs);
//        super("Score: 0");
//        c = getContentPane();
        this.context = c;
        setOnTouchListener(this);

        this.
        tkind = new Random();
        //		tkind.setSeed(83249284);
        //		tkind.setSeed(Math.random());

        nextone = tkind.nextInt(7);
        bgpiece = new Tetromino(nextone, true);

        //		pieces = new ArrayList<Tetromino>();

//        tp.setPreferredSize(new Dimension(250, 500));

//        addKeyListener(this);	// I am my own key listener

        board = new int[width][height];

//        c.add(tp);
        popup();

        timer = new Timer();	// we will handle the timer
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                actionPerformed();
            }
        }, interval, interval);

//        startNewPiece();
    }

    /**
     * Displays the window onscreen once it's been consturcted.
     */
    public void popup(){
//        this.pack();
//        this.setVisible(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Get the size of the screen
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//
//        // Determine the new location of the window
//        int w = getSize().width;
//        int h = getSize().height;
//        int x = (dim.width-w)/2;
//        int y = (dim.height-h)/2;
//
//        // Move the window
//        setLocation(x, y);
//
//        if (timer!=null)
//        {
//            timer.stop();
//            timer = new Timer(interval, this);
//            timer.start();
//        }
//
//        setResizable(false);
    }


        public void onDraw(Canvas g)
        {
            super.onDraw(g);
//            super.paintComponent(g);
            Paint p = new Paint();

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            g.drawRect(0, 0, 0+width*Tetromino.size, 0+height*Tetromino.size, p);


            if (gameover)
            {
                p.setColor(Color.WHITE);
                g.drawText("GAME OVER", 85, 40, p);
                g.drawText("Try again next time!", 60, 100, p);

                g.drawText("Score: "+score, 80, 180, p);
                g.drawText("Press ENTER to play again.", 40, 250, p);

            }
            else
            {

                bgpiece.drawPiece(g);

                // Setup paint field
                //				for (Tetromino p : pieces)
                //					if (p!=null)
                //						p.drawPiece(g);

                if (swapped != null)
                    swapped.drawPiece(g);

                // draw splatters
                for (int x=0; x<10; x++)
                    for (int y=0; y<20; y++)
                        if (board[x][y] > 0)
                        {
                            int c;
                            switch(board[x][y]-1)
                            {
                                case 0:
                                    c = Color.CYAN;
                                    break;
                                case 1:
                                    c = Color.MAGENTA;
                                    break;
                                case 2:
                                    c = Color.YELLOW;
                                    break;
                                case 3:
                                    c = Color.rgb(255, 165, 0);
                                    break;
                                case 4:
                                    c = Color.BLUE;
                                    break;
                                case 5:
                                    c = Color.GREEN;
                                    break;
                                case 6:
                                    c = Color.RED;
                                    break;
                                default:
                                    c = Color.WHITE;
                            }
                            Tetromino.drawSquare(g,c,x,y);
                        }

                if (active!=null)
                {
                    active.drawShadow(g, board);
                    active.drawPiece(g);
                }
            }

        }

    /**
     * Detects any key presses and handles them accordingly
     */
//    @Override
//    public void keyPressed(KeyEvent arg0)
//    {
//        int keycode = arg0.getKeyCode();
//
//        //		System.out.println(keycode);
//
//        if (keycode == 10 && gameover)
//        {
//            TetrisHub.startNewGame();
//            dispose();
//        }
//
//        if ((keycode == 16 || keycode == 157) && !swappedonce)
//        {
//            if (swapped!=null)
//            {
//                Tetromino temp = active;
//                active = swapped;
//                swapped = temp;
//
//                active.markActive();
//                swapped.shadowify();
//            }
//            else
//            {
//                swapped = active;
//                swapped.shadowify();
//                startNewPiece();
//            }
//
//            swappedonce = true;
//
//        }
//
//        if ( keycode == 32)
//        {
//            active.slam(board);
//            actionPerformed(null);
//        }
//
//        if (keycode == 38)
//            try {
//                active.rotate(board);
//            } catch (CloneNotSupportedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        if (keycode == 40)
//            actionPerformed(null);
//
//        if (keycode == 37)
//            if (active.canMoveLeft(board))
//                active.moveLeft();
//
//        if (keycode == 39)
//            if (active.canMoveRight(board))
//                active.moveRight();
//
//        if (keycode == 49)
//            active.y = 0;
//
//        if (keycode == 50)
//            if (timer.isRunning())
//                timer.stop();
//            else
//                timer.start();
//
//        if (keycode == 51)
//            startNewPiece();
//
//        if (keycode == 52)
//            Tetromino.playwithshadows = !Tetromino.playwithshadows;
//
//        if (keycode == 55)
//            Tetromino.realshadow = !Tetromino.realshadow;
//
//        if (keycode == 56 )
//            Tetromino.wires = !Tetromino.wires;
//
//        if (keycode == 57)
//            Tetromino.borders = !Tetromino.borders;
//
//        if (keycode == 83) // S
//        {
//            TetrisHub.save(this);
//        }
//
////        if (keycode == 82) // R
////        {
////            if (TetrisHub.restore())
////                dispose();
////        }
//
//        invalidate();
//    }

//    @Override
//    public void keyReleased(KeyEvent arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void keyTyped(KeyEvent arg0) {
//        // TODO Auto-generated method stub
//
//    }

    /**
     * Starts a new piece falling, as well as checking for the end of the game.
     */
    public void startNewPiece()
    {
        if (board[4][0] > 0)
            gameover = true;
        else
        {
            active = new Tetromino(nextPiece());
            bgpiece = new Tetromino(nextone, true);
            //			pieces.add(active);
        }
    }

    /**
     * Cycles the next piece as well as taking care of the next one and the previous one.
     * @return	the next value that used to be in next one.
     */
    public int nextPiece()
    {
        int returnme = nextone;
        nextone = tkind.nextInt(7);
        return returnme;
    }

    /**
     * Checks any and all rows for any cleared rows, and clears them if they need to be
     */
    public void checkForClear()
    {
        int points = 0;
        swappedonce = false;

        for (int x=0; x<20; x++)
        {
            int count=0;

            for (int y=0; y<10; y++)
            {
                if (board[y][x] > 0) count++;
            }

            if (count==10)
                points+=clearRow(x);
        }

        if (points == 4) points = 10;
        score += points*100;
//        setTitle("Score: "+score);


    }

    /**
     * Clears the entire row that is specified, and moves everything down
     *
     * @param row	row to be cleared
     * @return
     */
    public int clearRow(int row)
    {
        for (int x=row; x>0; x--)
        {
            for (int y=0; y<10; y++)
                board[y][x] = board[y][x-1];
        }

        // clear the top row too
        for (int x=0; x<10; x++)
            board[x][0] = 0;

//        if (500 - (score/500)*50 != timer.getDelay())
//            timer.setDelay(500 - (score/500)*50);

        return 1;
    }

    /**
     * Is called when the timer fires.
     */
    public void actionPerformed()
    {
        if (getWidth()!=0 && !adjusted) {
            Tetromino.size *= Math.min(getWidth() / width, getHeight() / height);
            Log.v("size", ""+Tetromino.size);
            adjusted = true;
            startNewPiece();
        }

        if (active != null)
        {
            Log.v("Timer", "timer ping");
            if (!active.canMoveDown(board))
            {
                // board[active.getX()][active.getY()] = true;
                try {
                    active.reportHit(board);
                }catch (Exception e){
                    gameover = true;
                }
                checkForClear();
                startNewPiece();
                Log.v("GRID SIZE", ""+getHeight() + " " + getWidth());
            }
            else
                active.moveDown();
        }
        GBMainActivity.main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
        //		timer.start();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        int e = event.getAction();

        if (e == MotionEvent.ACTION_UP)
        {
            slamLocked = false;
            // a tap was made
            if (noMove) {
                try {
                    active.rotate(board);
                } catch (CloneNotSupportedException e2) {
                   // e2.printStackTrace();
                }
                GBMainActivity.main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }

        }
        if (e == MotionEvent.ACTION_MOVE)
        {
            float dist = event.getX() - touchDownX;
            float distY = event.getY() - touchDownY;

            if (distY <= -1*Tetromino.size*3 && !slamLocked && !swappedonce)
            {
                slamLocked = true;
                if (swapped!=null)
            {
                Tetromino temp = active;
                active = swapped;
                swapped = temp;

                active.markActive();
                swapped.shadowify();
            }
            else
            {
                swapped = active;
                swapped.shadowify();
                startNewPiece();
            }

            swappedonce = true;

                GBMainActivity.main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }

            if (distY >= Tetromino.size*3 && !slamLocked) {
                touchDownX = event.getX();
                touchDownY = event.getY();
                active.slam(board);
                noMove = false;
                slamLocked = true;
                actionPerformed();

                GBMainActivity.main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }

            if (Math.abs(dist) >= Tetromino.size)
            {
                // we moved
                noMove = false;

                // detect a move left
                if (dist <= -1 * Tetromino.size && active.canMoveLeft(board))
                    active.moveLeft();

                // detect a move right
                if (dist >= Tetromino.size && active.canMoveRight(board))
                    active.moveRight();

                GBMainActivity.main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });

                touchDownX = event.getX();
                touchDownY = event.getY();
            }

        }
        if (e == MotionEvent.ACTION_DOWN)
        {
            touchDownX = event.getX();
            touchDownY = event.getY();
            noMove = true;
        }

        return true;
    }
}