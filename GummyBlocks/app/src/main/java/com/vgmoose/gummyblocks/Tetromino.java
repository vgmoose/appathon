package com.vgmoose.gummyblocks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * A Tetromino is the actual Tetris collection of blocks that falls down. They come
 * in different shapes and sizes (7 different kinds), and are responsible for the main
 * content of the gameplay.
 *
 * This class manages checking if a piece can move, as well as rotate. It also manages shadows,
 * and the nextpiece/heldpiece are stored as Tetrominos as well.
 *
 * @author Ricky and Phil
 *
 **/
public class Tetromino implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;

    static int size = 1;

    Tetromino shadow;

    // user preferences
    static boolean wires = false;
    static boolean borders = true;
    static boolean realshadow = true;
    static boolean playwithshadows = true;

    int x = 100;
    int y = 0;
    int c;

    boolean isshadow = false;

    int kind;
    boolean[][] blocks;

    /**
     * This constructor is to be used when a regular shape is needed for falling.
     *
     * @param kind	value between 0 and 6 (inclusive) that represents a shape.
     */
    Tetromino(int kind)
    {
        this.kind = kind;

        figureOutShape();
        //		c = Color.cyan;

        shadow = new Tetromino(kind, false);
        shadow.x = this.x;
    }

    /**
     * This constructor is to be used when the backdropped version of a piece needs
     * to be drawn, and not played with.
     *
     * @param kind	value between 0 and 6 (inclusive) that represents a shape.
     * @param isnextpiece  should the piece be drawn giant and in the "Next Piece" spot?
     */
    public Tetromino(int kind, boolean isnextpiece)
    {
        this.kind = kind;
        figureOutShape();

        if (realshadow)
            c = Color.rgb(45, 45, 45);
        //			c = c.brighter().brighter().brighter().brighter().brighter();

        if (isnextpiece)	// alternative is shadow
        {
            rotate();

            x = 185;
            y = 80;

            if (kind == 5)
                x -= size*1.4;
        }
        else
        {
            isshadow=true;
        }

        if (!playwithshadows && isshadow)
            c = Color.BLACK;
    }

    /**
     * Sets up the local mini-grid boolean matrix with the appropriate values for the kind
     * field variable that responds to this piece.
     */
    public void figureOutShape()
    {
        //		kind = (int)(Math.random()*6);

        if (kind == 0)		// Line piece
        {
            boolean[][] t =
                    {{ false, false, false, false},
                            {  true,  true,  true,  true},
                            { false, false, false, false},
                            { false, false, false, false}};  // pivot: 1,1
            blocks = t;
            c = Color.CYAN;
        }
        else if (kind == 1)		// T piece
        {
            boolean[][] t =
                    {{ false, false, false},
                            {  true,  true,  true},
                            { false,  true, false}};  // pivot: 1,1
            blocks = t;
            c = Color.MAGENTA;
        }
        else if (kind == 2)		// square piece
        {
            boolean[][] t =
                    {{  true,  true},
                            {  true,  true}};  // pivot: none
            blocks = t;
            c = Color.YELLOW;
        }
        else if (kind == 3) 	// L piece
        {
            boolean[][] t =
                    {{ false, false, false},
                            {  true,  true,  true},
                            {  true, false, false}};  // pivot: 1,1
            blocks = t;
            c = Color.rgb(255, 165, 0);
        }
        else if (kind == 4) 	// reverse L piece
        {
            boolean[][] t =
                    {{ false, false, false},
                            {  true,  true,  true},
                            { false, false,  true}};  // pivot: 1,1
            blocks = t;
            c = Color.BLUE;
        }
        else if (kind == 5)
        {
            boolean[][] t =
                    {{ false,  true,  true},
                            {  true,  true, false},
                            { false, false, false}}; // pivot: 1,1
            blocks = t;
            c = Color.GREEN;
        }
        else if (kind == 6)
        {
            boolean[][] t =
                    {{ true,  true,  false},
                            {  false,  true, true},
                            { false, false, false}}; // pivot: 1,1
            blocks = t;
            c = Color.RED;
        }
    }

    /**
     * Draws the Tetromino onscreen at the appropriate coordinates.
     *
     * @param g
     */
    public void drawPiece(Canvas g)
    {
        Paint p = new Paint();
        p.setColor(c);
//        g.setColor(c);

        for (int x=0; x<blocks.length; x++)
        {
            for (int y=0; y<blocks.length; y++)
                if (blocks[y][x])
                {
                    if ((size==35 || isshadow) && !realshadow)
                        g.drawRect(x*size+this.x-size, y*size+this.y-size, x*size+this.x-size+size, y*size+this.y-size+size, p);
                    else
                    {
                        if (wires && (!realshadow && (isshadow || size==35)))
                            g.drawRect(x*size+this.x-size, y*size+this.y-size, x*size+this.x-size+size, y*size+this.y-size+size, p);
                        else
                        {
                            p.setStyle(Paint.Style.FILL_AND_STROKE);
                            g.drawRect(x * size + this.x - size, y * size + this.y - size, x * size + this.x - size+size, y * size + this.y - size+size, p);
                            p.setStyle(Paint.Style.STROKE);
                        }

                        if (borders)
                        {
                            p.setColor(Color.BLACK);
                            g.drawRect(x*size+this.x-size, y*size+this.y-size, x*size+this.x-size+size, y*size+this.y-size+size, p);
                            p.setColor(c);
                        }
                    }

                }
        }
        // Draw a single square
        //		g.fillRect(x, y, size, size);

    }

    /**
     * Draws the shadow of the Tetromino below, as well as updating its position
     * @param g
     * @param board	the board of the entire game, to figure out the position
     */
    public void drawShadow(Canvas g, int[][] board)
    {
        // Draw the shadow
        if (!isshadow)
        {
            updateShadow(board);
            shadow.drawPiece(g);
        }

    }

    /**
     * Updates the position of the shadow on screen. Should be called to sync the shadow with the
     * Tetromino main piece.
     * @param board board	the board of the entire game, to figure out the position
     */
    public void updateShadow(int[][] board)
    {
        shadow.x = this.x;
        shadow.y = this.y;
        shadow.slam(board);
    }

    /**
     * Draws a single square onscreen, used when drawing the entire board that has already fallen.
     * @param g
     * @param c		The color to draw the square
     * @param x		The X value
     * @param y		The Y value
     */
    public static void drawSquare(Canvas g, int c, int x, int y)
    {
        Paint p = new Paint();
        p.setColor(c);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        g.drawRect(x*size, y*size, x*size+size, y*size+size, p);

        if (borders)
        {
            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            g.drawRect(x*size, y*size, x*size+size, y*size+size, p);
        }

    }

    /**
     * Move the internal variable representing the piece's location down one
     */
    public void moveDown()
    {
        y += size;
    }

    /**
     * Move the internal variable representing the piece's location left one
     */
    public void moveLeft()
    {
        if (getX() != 0)
            x -= size;
    }

    /**
     * Move the internal variable representing the piece's location right one
     */
    public void moveRight()
    {
        if (getX() != 9)
            x += size;
    }

    /**
     * Get the actual Y value of the piece (fits into the matrix, not the actual coordinates)
     */
    public int getY()
    {
        return y/size;
    }

    /**
     * Check if the entire piece is able to move right. This checks every single point on the piece.
     * @param board		The entire game board.
     * @return
     */
    public boolean canMoveRight(int[][] board)
    {
        if (getY() == 0)
            return true;

        for (int x=0; x<blocks.length; x++)
            for (int y=0; y<blocks.length; y++)
                if (blocks[x][y])
                    if (y+getX()-1 == 9 || board[getX()+y][getY()+x-1]>0)
                        return false;
        return true;
    }

    /**
     * Check if the entire piece is able to move left. This checks every single point on the piece.
     * @param board		The entire game board.
     * @return
     */
    public boolean canMoveLeft(int[][] board)
    {
        if (getY() == 0)
            return true;

        for (int x=0; x<blocks.length; x++)
            for (int y=0; y<blocks.length; y++)
                if (blocks[x][y])
                    if (y+getX()-1 == 0 || board[getX()+y-2][getY()+x-1]>0)
                        return false;

        return true;
    }

    /**
     * Check if the entire piece is able to move down. This checks every single point on the piece.
     * @param board		The entire game board.
     * @return
     */
    public boolean canMoveDown(int[][] board)
    {
        for (int x=0; x<blocks.length; x++)
            for (int y=0; y<blocks.length; y++)
                if (blocks[x][y])
                    if (getY()+x-1 == 19 || board[getX()+y-1][getY()+x] >0)
                        return false;

        return true;
    }

    /**
     * Gets the actual X value
     * @return
     */
    public int getX()
    {
        return x/size;
    }

    /**
     * Moves the piece as far down as it can go, slamming it down.
     * @param board
     */
    public void slam(int[][] board)
    {
        while (canMoveDown(board))
            moveDown();
    }

    /**
     * Checks if the current piece is able to exist on the board
     * @param board
     * @return
     */
    public boolean canExist(int[][] board)
    {
        if (getY() == 0)
            return true;

        try
        {
            for (int x=0; x<blocks.length; x++)
                for (int y=0; y<blocks.length; y++)
                    if (blocks[x][y])
                        if (getY()+x-1 < 0 || board[getX()+y-1][getY()+x-1] >0)
                            return false;
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * Rotates a piece with respect to a given board, preventing it from rotating if it cannot
     * @param board
     * @throws CloneNotSupportedException
     */
    public void rotate(int[][] board) throws CloneNotSupportedException
    {
        Tetromino a = (Tetromino) this.clone();
        a.shadow = null;
        a.rotate();

        if (a.canExist(board))
            rotate();
        //		shadow.blocks = blocks;
    }

    /**
     * Relatively rotates a piece in its spot
     */
    public void rotate()
    {
        boolean[][] newblocks = new boolean[blocks.length][blocks.length];

        if (kind != 2)
        {
            newblocks[0][2] = blocks[0][0];
            newblocks[1][2] = blocks[0][1];

            newblocks[2][2] = blocks[0][2];
            newblocks[2][1] = blocks[1][2];

            newblocks[2][0] = blocks[2][2];
            newblocks[1][0] = blocks[2][1];

            newblocks[0][0] = blocks[2][0];
            newblocks[0][1] = blocks[1][0];

            newblocks[1][1] = true;

            if (kind == 0)
            {
                newblocks[1][2] = blocks[2][1];
                newblocks[1][3] = blocks[3][1];

                newblocks[2][1] = blocks[1][2];
                newblocks[3][1] = blocks[1][3];
            }

            blocks = newblocks;

            if (!isshadow && shadow!=null)
                shadow.rotate();
        }
    }

    /**
     * To be called when the piece actually hits the ground (can no longer move down).
     *
     * This sets all the values in the board to the appropriate colors/values.
     * @param board
     */
    public void reportHit(int[][] board)
    {
        for (int x=0; x<blocks.length; x++)
            for (int y=0; y<blocks.length; y++)
                if (blocks[x][y])
                    board[getX()+y-1][getY()+x-1] = kind+1;
    }

    /**
     * Returns the width of the blocks array
     * @return
     */
    public int getWidth()
    {
        return blocks.length;
    }

    /**
     * Transforms the piece into the hold item piece, "shadowifying" it.
     */
    public void shadowify()
    {
        isshadow = true;

        figureOutShape();
        rotate();

        if (realshadow)
            c = Color.rgb(45, 45, 45);

        // stow shadow
        shadow = null;

//        size = 20;
        x = 35;

        if (kind == 5)
            x -= 20;

        y = 50;
    }

    /**
     * Marks the shape active when returning from its shadowfied shape.
     */
    public void markActive()
    {
        isshadow = false;

        // restore shadow
        shadow = new Tetromino(kind, false);

//        size = 25;

        x = 100;
        y = 0;

        shadow.x = this.x;

        figureOutShape();


    }
}