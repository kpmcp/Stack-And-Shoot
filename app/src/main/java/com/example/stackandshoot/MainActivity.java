package com.example.stackandshoot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final int COUNT_OF_ENEMIES = 30;
    private int enemiesLeft = 30;
    TextView enemiesLeftTxt;
    private ModelRenderable bullet;
    private ImageButton pauseBtn, shootBtn;
    private Point point;
    private Scene scene;
    private Camera camera;
    private boolean isPaused;

    Dialog pauseMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getRealSize(point);

        setContentView(R.layout.shoot_hot_air_baloons_fragment);

        BaloonsFragment baloonsFragment = (BaloonsFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene = baloonsFragment.getArSceneView().getScene();
        camera = scene.getCamera();

        enemiesLeftTxt = findViewById(R.id.EnemiesLeftTxt);

        pauseBtn = findViewById(R.id.pause_btn);
        shootBtn = findViewById(R.id.shoot_btn);

        pauseMenu = new Dialog(MainActivity.this);
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
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
//        pauseMenu.setCancelable(false);

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = true;
                pause();
            }
        });

        shootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoot();
            }
        });

        addEnemies();
        buildBulletModel();
    }

    public void pause() {
        if (isPaused) {
            this.pauseMenu.show();
        } else if (!isPaused) {
            this.pauseMenu.hide();
        }
    }

    private void shoot() {

        Ray ray = camera.screenPointToRay(point.x / 2f, point.y / 2f);
        Node node = new Node();
        node.setRenderable(bullet);
        scene.addChild(node);

        new Thread(() -> {

            for (int i = 0;i < 200;i++) {

                int finalI = i;
                runOnUiThread(() -> {

                    Vector3 vector3 = ray.getPoint(finalI * 0.1f);
                    node.setWorldPosition(vector3);

                    Node nodeInContact = scene.overlapTest(node);

                    if (nodeInContact != null) {

                        enemiesLeft--;
                        enemiesLeftTxt.setText("Enemies Left: " + enemiesLeft);
                        scene.removeChild(nodeInContact);


                    }

                });

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            runOnUiThread(() -> scene.removeChild(node));

        }).start();

    }

    private void buildBulletModel() {

        Texture
                .builder()
                .setSource(this, R.drawable.texture)
                .build()
                .thenAccept(texture -> {


                    MaterialFactory
                            .makeOpaqueWithTexture(this, texture)
                            .thenAccept(material -> {

                                bullet = ShapeFactory
                                        .makeSphere(0.01f,
                                                new Vector3(0f, 0f, 0f),
                                                material);

                            });


                });

    }

    private void addEnemies() {
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("balloon.sfb"))
                .build()
                .thenAccept(renderable -> {

                    for (int i = 0;i < COUNT_OF_ENEMIES;i++) {

                        Node node = new Node();
                        node.setRenderable(renderable);
                        scene.addChild(node);


                        Random random = new Random();
                        int x = random.nextInt(10);
                        int z = random.nextInt(10);
                        int y = random.nextInt(20);

                        z = -z;

                        node.setWorldPosition(new Vector3(
                                (float) x,
                                y / 10f,
                                (float) z
                        ));


                    }

                });

    }
}
