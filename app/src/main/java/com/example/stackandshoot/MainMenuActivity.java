package com.example.stackandshoot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.ar.sceneform.Scene;

import com.google.ar.sceneform.Camera;

import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {
    private Camera camera;
    private TextureView view;
    private Button exitBtn, shootGameBtn, stackGameBtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

//        Objects.requireNonNull(getSupportActionBar()).hide();

        view = (TextureView) findViewById(R.id.view);

        exitBtn = findViewById(R.id.exit_btn);
        shootGameBtn = findViewById(R.id.play_shoot_game_btn);
        stackGameBtn = findViewById(R.id.play_stack_game_btn);

        shootGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        stackGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        startCamera();
    }

    private void startCamera() {
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(view.getWidth(), view.getHeight());
        Size screen = new Size(view.getWidth(), view.getHeight());

        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
                parent.addView(view);

                view.setSurfaceTexture(output.getSurfaceTexture());
                update();
            }
        });

        ImageCaptureConfig  imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).
                setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imageCapture = new ImageCapture((imageCaptureConfig));

        CameraX.bindToLifecycle(this, preview, imageCapture);
    }

    private void update() {
        Matrix matrix = new Matrix();

        float w = view.getMeasuredWidth(), h = view.getMeasuredHeight();
        float cx = w / 2f, cy = h / 2f;

        int rotationD;
        int rotation = (int) view.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationD = 0;
                break;
            case Surface.ROTATION_90:
                rotationD = 90;
                break;
            case Surface.ROTATION_180:
                rotationD = 180;
                break;
            case Surface.ROTATION_270:
                rotationD = 270;
                break;
            default:return;
        }

        matrix.postRotate((float) rotationD, cx, cy);
        view.setTransform(matrix);
    }
}
