package com.example.stackandshoot;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Scene scene;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoot_hot_air_baloons_fragment);

        BaloonsFragment baloonsFragment = (BaloonsFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        scene = baloonsFragment.getArSceneView().getScene();
        camera = scene.getCamera();

//        addEnemies();
    }

//    private void addEnemies() {
//        ModelRenderable
//                .builder()
//                .setSource(this, Uri.parse("hotAirBaloon.fbx"))
//                .build()
//                .thenAccept(renderable -> {
//
//                    for (int i = 0;i < 20;i++) {
//
//                        Node node = new Node();
//                        node.setRenderable(renderable);
//                        scene.addChild(node);
//
//
//                        Random random = new Random();
//                        int x = random.nextInt(10);
//                        int z = random.nextInt(10);
//                        int y = random.nextInt(20);
//
//                        z = -z;
//
//                        node.setWorldPosition(new Vector3(
//                                (float) x,
//                                y / 10f,
//                                (float) z
//                        ));
//
//
//                    }
//
//                });
//
//    }
}
