package com.sardari.videotrimmersample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.sardari.mediatrimmer.VideoTrimmer;
import com.sardari.mediatrimmer.interfaces.OnTrimVideoListener;

import java.io.File;

//import com.sardari.mediatrimmer.interfaces.OnVideoListener;

public class TrimmerActivity extends AppCompatActivity {
    private VideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);

        Intent extraIntent = getIntent();
        String path = "";

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(MainActivity.EXTRA_VIDEO_PATH);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        mVideoTrimmer = findViewById(R.id.timeLine);
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(8);
            mVideoTrimmer.setMinDuration(3);
            mVideoTrimmer.setDestinationPath(getTempFile());
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
            mVideoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
                @Override
                public void onTrimStarted() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressDialog.show();
//                        }
//                    });
                }

                @Override
                public void onSuccess(final Uri uri) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProgressDialog.cancel();

//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(TrimmerActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            intent.setDataAndType(uri, "video/mp4");
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                }

                @Override
                public void onCancel() {
//                    mProgressDialog.cancel();
//                    mVideoTrimmer.destroy();
//                    finish();
                }

                @Override
                public void onError(final String message) {
//                    mProgressDialog.cancel();
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            });
        }
    }

    public String getTempFile() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "MediaTrimmer");

        if (!file.exists()) {
            file.mkdirs();
        }

        String fileName = String.valueOf(System.currentTimeMillis()) + ".mp4";
        return file.getAbsolutePath() + File.separator + fileName;
    }
}
