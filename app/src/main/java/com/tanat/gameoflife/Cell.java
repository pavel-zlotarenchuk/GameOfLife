package com.tanat.gameoflife;

public class Cell {
    private int x, y;
    private boolean alive;

    public Cell(int x, int y, boolean alive) {
        this.x = x;
        this.y = y;
        this.alive = alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public void reborn() {
        alive = true;
    }

    public void invert() {
        alive = !alive;
    }
}