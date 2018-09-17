package com.tanat.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameOfLifeView extends SurfaceView implements Runnable {
    // Default size of a cell
    public static final int DEFAULT_SIZE = 30;
    public static final int DEFAULT_ALIVE_COLOR = Color.WHITE;
    public static final int DEFAULT_DEAD_COLOR = Color.BLACK;

    private Thread thread;
    private boolean isRunning = false;
    private int columnWidth = 1;
    private int rowHeight = 1;
    private int nbColumns = 1;
    private int nbRows = 1;

    private World world;
    private Rect r = new Rect();
    private Paint p = new Paint();

    public GameOfLifeView(Context context) {
        super(context);
        initWorld();
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWorld();
    }

    @Override
    public void run() {
        // while the world is evolving
        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;
            // Pause of 300 ms to better visualize the evolution of the world
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            Canvas canvas = getHolder().lockCanvas();
            world.nextGeneration();
            drawCells(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);

        thread.start();
    }

    public void stop() {
        isRunning = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            break;
        }
    }

    private void initWorld() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        nbColumns = point.x / DEFAULT_SIZE;
        nbRows = point.y / DEFAULT_SIZE;

        columnWidth = point.x / nbColumns;
        rowHeight = point.y / nbRows;
        world = new World(nbColumns, nbRows);
    }

    // Method to draw each cell of the world on the canvas
    private void drawCells(Canvas canvas) {
        for (int i = 0; i < nbColumns; i++) {
            for (int j = 0; j < nbRows; j++) {
                Cell cell = world.get(i, j);
                r.set((cell.getX() * columnWidth) - 1, (cell.getY() * rowHeight) - 1,
                        (cell.getX() * columnWidth + columnWidth) - 1,
                        (cell.getY() * rowHeight + rowHeight) - 1);
                // we change the color according the alive status of the cell
                p.setColor(cell.isAlive() ? DEFAULT_ALIVE_COLOR : DEFAULT_DEAD_COLOR);
                canvas.drawRect(r, p);
            }
        }
    }

    // We let the user to interact with the Cells of the World
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // we get the coordinates of the touch and we convert it in coordinates for the board
            int i = (int) (event.getX() / columnWidth);
            int j = (int) (event.getY() / rowHeight);
            // we get the cell associated to these positions
            Cell cell = world.get(i, j);
            // we call the invert method of the cell got to change its state
            cell.invert();
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
