package com.sardari.videotrimmersample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sardari.mediatrimmer.utils.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO_TRIMMER = 0x01;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton galleryButton = findViewById(R.id.galleryButton);
        if (galleryButton != null) {
            galleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickFromGallery();
                }
            });
        }

    }

    private void pickFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale), REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setTypeAndNormalize("video/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_video)), REQUEST_VIDEO_TRIMMER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_TRIMMER) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
//                    TrimmerDialog trimmerDialog = new TrimmerDialog(MainActivity.this);
//                    trimmerDialog.setMaxDuration(8);
//                    trimmerDialog.setMinDuration(3);
//                    trimmerDialog.setOrigPath(FileUtils.getPath(this, selectedUri));
//                    trimmerDialog.setDestPath(getTempFile());
//                    trimmerDialog.setOnTrimVideoListener(new OnTrimVideoListener() {
//                        @Override
//                        public void onTrimStarted() {
//
//                        }
//
//                        @Override
//                        public void onSuccess(final Uri uri) {
//                            Toast.makeText(MainActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            Log.w("TAG", "MainActivity_onCancel_84-> :" );
//                            finish();
//                        }
//
//                        @Override
//                        public void onError(final String message) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });
//                    trimmerDialog.showDialog();

                    startTrimActivity(selectedUri);
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_cannot_retrieve_selected_video, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
//        Log.w("TAG", "MainActivity_startTrimActivity_104-> :" +FileUtils.getPath(this, uri) );

        Intent intent = new Intent(this, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivity(intent);
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.permission_title_rationale));
            builder.setMessage(rationale);
            builder.setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
                }
            });
            builder.setNegativeButton(getString(R.string.label_cancel), null);
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
