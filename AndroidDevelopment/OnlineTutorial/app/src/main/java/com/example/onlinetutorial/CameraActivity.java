package com.example.onlinetutorial;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = "CameraActivity";

    // Timer Setup
    private CountDownTimer countDownTimer = null;
    private long timeLeftInMilliseconds;
    private TextView countdown_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        countdown_text = (TextView) findViewById(R.id.countdownText);

    }

    public static Camera getCameraInstance(){
        int cameraCount = 0;
        Camera c = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    c = Camera.open(camIdx);
                } catch (Exception e) {
                    // Camera is not available (in use or does not exist)
                }
            }
        }
        return c;
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
        mediaRecorder.setOrientationHint(270);

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

    @Override
    public void onResume(){
        super.onResume();
        if(mCamera==null){
            setContentView(R.layout.activity_camera);
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
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
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();        // Release the camera for other applications
            mCamera = null;
        }
    }

    /*** Start Recording - Called when button_start is pressed. ***/
    public void startRecording(View v){
        TextView recording = (TextView)findViewById(R.id.recroding_text);
        TextView recording_instructions = (TextView) findViewById(R.id.recording_instructions);
        startTimer();

        if (isRecording == false){
            recording.setVisibility(View.VISIBLE);
            recording_instructions.setText("New Recording Instructions!");

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
        TextView recording = (TextView)findViewById(R.id.recroding_text);
        TextView recording_instructions = (TextView) findViewById(R.id.recording_instructions);
        stopTimer();

        if (isRecording == true){

            recording.setVisibility(View.INVISIBLE);
            recording_instructions.setText("Recording Instructions");

            mediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.stopPreview();
            mCamera.release();
            isRecording = false;

            // Open UploadAndSendActivity once recording has stopped
            Intent intent = new Intent(this, UploadAndSendActivity.class);
            startActivity(intent);
        }
        else {
            // TODO: Inform user that the camera has not started recording yet.
        }
    }

    /*** Function for starting the countdown timer ***/
    public void startTimer(){
        timeLeftInMilliseconds = 10000;
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    /*** Function for stopping the countdown timer ***/
    public void stopTimer(){
        countDownTimer.cancel();
    }

    /*** Function for updating the countdown timer text ***/
    public void updateTimer(){
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        //String x = Long.toString(timeLeftInMilliseconds);
        Log.d("Milliseconds", timeLeftText);

        countdown_text.setText(timeLeftText);
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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HHmmss");
        String ts = format.format(date);

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