package com.example.onlinetutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UploadAndSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_and_send);

        // Open dialog to confirm success of video.
        openVideoSuccessDialog();
    }

    /*** Method for opening the video success dialog. ***/
    public void openVideoSuccessDialog(){
        VideoSuccessDialog videoSuccessDialog = new VideoSuccessDialog();
        videoSuccessDialog.show(getSupportFragmentManager(), "Example_Dialog");
    }
}
