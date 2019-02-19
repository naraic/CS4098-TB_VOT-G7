package com.example.camera_api;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;


public class CameraActivity extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        // Open the camera.
        camera = Camera.open(0);    // Back Camera
        showCamera = new ShowCamera(this, camera);
        frameLayout.addView(showCamera);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();

            if (picture_file == null){
                return;
            }
            else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    fos.close();

                    camera.startPreview();

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile(){
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");

            if (!folder_gui.exists()){
                folder_gui.mkdir();
            }

            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            String filename = "temp_" + ts + ".jpg";

            File outputFile = new File(folder_gui, filename);
            return outputFile;
        }
    }

    public void captureImage(View v){
        if(camera != null){
            camera.takePicture(null, null, mPictureCallback);

        }
    }
}
