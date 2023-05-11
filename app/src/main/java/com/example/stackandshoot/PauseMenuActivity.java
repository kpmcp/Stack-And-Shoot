package com.example.stackandshoot;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PauseMenuActivity extends AppCompatActivity {
    Thread mainThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_menu);

        mainThread = new Thread((Runnable) this);
    }
}
