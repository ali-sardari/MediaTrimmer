package com.sardari.mediatrimmer.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.sardari.mediatrimmer.R;
import com.sardari.mediatrimmer.VideoTrimmer;
import com.sardari.mediatrimmer.interfaces.OnTrimVideoListener;

public class TrimmerDialog extends Dialog {
    //region Fields
    private static android.os.Handler handler;
    private VideoTrimmer videoTrimmer;
    //endregion

    public TrimmerDialog(Context context) {
        super(context, R.style.FullScreenDialogTheme);
        handler = new android.os.Handler(context.getMainLooper());

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getWindow() != null)
            getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_trimmer);

        videoTrimmer = new VideoTrimmer(context);
        videoTrimmer.setVideoInformationVisibility(true);
    }

    public void showDialog() {
        ViewGroup insertPoint = findViewById(R.id.content);
        insertPoint.addView(videoTrimmer);

//        if(tv.getParent()!=null)
//            ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
//        layout.addView(tv); //  <==========  ERROR IN THIS LINE DURING 2ND RUN


        show();
    }

    public void setOrigPath(String origPath) {
        videoTrimmer.setVideoURI(Uri.parse(origPath));
    }

    public void setDestPath(String destPath) {
        videoTrimmer.setDestinationPath(destPath);
    }

    public void setMaxDuration(int maxDuration) {
        if (maxDuration > -1) {
            videoTrimmer.setMaxDuration(maxDuration);
        }
    }

    public void setMinDuration(int minDuration) {
        if (minDuration > -1) {
            videoTrimmer.setMinDuration(minDuration);
        }
    }

    public void setOnTrimVideoListener(final OnTrimVideoListener onTrimVideoListener) {
        videoTrimmer.setOnTrimVideoListener(new OnTrimVideoListener() {
            @Override
            public void onTrimStarted() {
//                runOnUi(new Runnable() {
//                    @Override
//                    public void run() {
                        onTrimVideoListener.onTrimStarted();
//                    }
//                });
            }

            @Override
            public void onSuccess(final Uri uri) {
//                runOnUi(new Runnable() {
//                    @Override
//                    public void run() {
                        onTrimVideoListener.onSuccess(uri);
                        dismiss();

//                    }
//                });
            }

            @Override
            public void onCancel() {
//                runOnUi(new Runnable() {
//                    @Override
//                    public void run() {
                        videoTrimmer.destroy();
                        dismiss();
//                    }
//                });
            }

            @Override
            public void onError(final String message) {
//                runOnUi(new Runnable() {
//                    @Override
//                    public void run() {
                        onTrimVideoListener.onError(message);
                        dismiss();
//                    }
//                });
            }
        });
    }

//    private void runOnUi(Runnable runnable) {
//        handler.post(runnable);
//    }
}
