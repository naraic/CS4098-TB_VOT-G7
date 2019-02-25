package com.example.onlinetutorial;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);    // Create our Preview view and set it as the content of our activity.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // Attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // Returns null if camera is unavailable
    }

    /*** Responsible for the storage of the media. ***/
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                camera.startPreview();
                openImageSuccessDialog();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /*** Video Recorder Setup ***/
    private boolean prepareVideoRecorder(){
        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // This fixes the camera orientation so that videos are saved in portrait mode
        mediaRecorder.setOrientationHint(90);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // If you are using MediaRecorder, release it first.
        releaseCamera();              // Release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // Clear recorder configuration
            mediaRecorder.release(); // Release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // Lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // Release the camera for other applications
            mCamera = null;
        }
    }

    /*** Start Recording - Called when button_start is pressed. ***/
    public void startRecording(View v){
        if (isRecording == false){
            if (prepareVideoRecorder()) {
                mediaRecorder.start();
                isRecording = true;
            } else {
                releaseMediaRecorder();
                // TODO: Inform user that prepareVideoRecorder() failed.
            }
        }
        else {
            // TODO: Inform user that the camera is already recording.
        }
    }

    /*** Stop Recording - Called when button_stop is pressed. ***/
    public void stopRecording(View v){
        if (isRecording == true){
            mediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();
            isRecording = false;
            // TODO: Inform user that the recording has stopped.
            openVideoSuccessDialog();

        }
        else {
            // TODO: Inform user that the camera has not started recording yet.
        }
    }

    /*** Capture Image - Called when button_capture is pressed. ***/
    public void captureImage(View v) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mPicture);
        }
    }

    /*** Method for opening the video success dialog. ***/
    public void openVideoSuccessDialog(){
        VideoSuccessDialog videoSuccessDialog = new VideoSuccessDialog();
        videoSuccessDialog.show(getSupportFragmentManager(), "Example_Dialog");
    }

    /*** Method for opening the image success dialog. ***/
    public void openImageSuccessDialog(){
        ImageSuccessDialog imageSuccessDialog = new ImageSuccessDialog();
        imageSuccessDialog.show(getSupportFragmentManager(), "Example_Dialog");
    }

    /*** Create a File for saving an image or video ***/
    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ ts + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ ts + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
