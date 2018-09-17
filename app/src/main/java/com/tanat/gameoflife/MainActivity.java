package com.tanat.gameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GameOfLifeView gameOfLifeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameOfLifeView = (GameOfLifeView) findViewById(R.id.game_of_life);
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameOfLifeView.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        gameOfLifeView.stop();
    }
}