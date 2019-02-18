package com.example.camera_api;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        // Open the camera.
        camera = Camera.open(0);    // Back Camera
        //camera = Camera.open(1);    // Front Camera

        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }
}
