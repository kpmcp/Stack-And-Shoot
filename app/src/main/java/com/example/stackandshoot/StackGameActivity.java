package com.example.stackandshoot;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.BaseArFragment;

import java.util.function.Consumer;

public class StackGameActivity extends AppCompatActivity {
    StackFragment stackFragment;
    Dialog pauseMenu;

    private ImageButton pauseBtn;

    private boolean isPaused;

    private Scene scene;
    private Camera camera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack_fragment);

        stackFragment = (StackFragment) getSupportFragmentManager().findFragmentById(R.id.arStackFragment);

        scene = stackFragment.getArSceneView().getScene();
        camera = scene.getCamera();

        pauseBtn = findViewById(R.id.pause_btn);

        pauseMenu = new Dialog(StackGameActivity.this);
        pauseMenu.setContentView(R.layout.pause_menu);
        pauseMenu.setTitle("Pause");
        pauseMenu.getWindow().setBackgroundDrawable(getDrawable(R.drawable.pause_background));
        pauseMenu.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button resumeBtn = pauseMenu.findViewById(R.id.resume_btn);
        Button backToMenuBtn = pauseMenu.findViewById(R.id.back_to_menu_btn);

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = false;
                pauseMenu.dismiss();
            }
        });

        backToMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StackGameActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                pause();
            }
        });

        stackFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                placeCube(hitResult.createAnchor());
            }
        });
    }

    public void pause() {
        if (isPaused) {
            this.pauseMenu.show();
        } else if (!isPaused) {
            this.pauseMenu.hide();
        }
    }

    private void placeCube(Anchor anchor) {
        MaterialFactory
                .makeOpaqueWithColor(this, new Color(android.graphics.Color.YELLOW))
                .thenAccept(new Consumer<Material>() {
                    @Override
                    public void accept(Material material) {
                        ModelRenderable modelRenderable = ShapeFactory.makeCube(new Vector3(0.1f, 0.1f, 0.1f), new Vector3(0f, 0.1f, 0f), material);
                        placeModel(modelRenderable, anchor);
                    }
                });

    }

    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode =new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        stackFragment.getArSceneView().getScene().addChild(anchorNode);

    }
}
