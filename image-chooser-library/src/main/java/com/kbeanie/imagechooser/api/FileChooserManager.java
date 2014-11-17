package com.kbeanie.imagechooser.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.imagechooser.api.config.Config;
import com.kbeanie.imagechooser.threads.FileProcessorListener;
import com.kbeanie.imagechooser.threads.FileProcessorThread;
import com.kbeanie.imagechooser.threads.ImageProcessorThread;

/**
 * Created by kbibek on 14/11/14.
 */
public class FileChooserManager extends MediaChooserManager implements FileProcessorListener {

    private final static String TAG = "FileChooserManager";

    private FileChooserListener listener;

    private String mimeType;

    public FileChooserManager(Activity activity) {
        super(activity, ChooserType.REQUEST_PICK_FILE);
    }

    public FileChooserManager(Fragment fragment) {
        super(fragment, ChooserType.REQUEST_PICK_FILE);
    }

    public FileChooserManager(android.app.Fragment fragment) {
        super(fragment, ChooserType.REQUEST_PICK_FILE);
    }

    public FileChooserManager(Activity activity, String foldername) {
        super(activity, ChooserType.REQUEST_PICK_FILE, foldername);
    }

    public FileChooserManager(Fragment fragment, String foldername) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, foldername);
    }

    public FileChooserManager(android.app.Fragment fragment, String foldername) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, foldername);
    }

    public FileChooserManager(Activity activity, boolean shouldCreateThumbnails) {
        super(activity, ChooserType.REQUEST_PICK_FILE, shouldCreateThumbnails);
    }

    public FileChooserManager(Fragment fragment, boolean shouldCreateThumbnails) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, shouldCreateThumbnails);
    }

    public FileChooserManager(android.app.Fragment fragment, boolean shouldCreateThumbnails) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, shouldCreateThumbnails);
    }

    public FileChooserManager(Activity activity, String foldername, boolean shouldCreateThumbnails) {
        super(activity, ChooserType.REQUEST_PICK_FILE, foldername, shouldCreateThumbnails);
    }

    public FileChooserManager(Fragment fragment, String foldername, boolean shouldCreateThumbnails) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, foldername, shouldCreateThumbnails);
    }

    public FileChooserManager(android.app.Fragment fragment, String foldername, boolean shouldCreateThumbnails) {
        super(fragment, ChooserType.REQUEST_PICK_FILE, foldername, shouldCreateThumbnails);
    }

    public void setFileChooserListener(FileChooserListener listener) {
        this.listener = listener;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String choose() throws Exception {
        if (listener == null) {
            throw new IllegalArgumentException(
                    "FileChooserListener cannot be null. Forgot to set FileChooserListener???");
        }
        if (mimeType == null) {
            mimeType = "*/*";
        }
        String action = Intent.ACTION_GET_CONTENT;
        try {
            Intent intent = new Intent(action);
            intent.setType(mimeType);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            throw new Exception("Activity not Found");
        }
        return null;
    }

    @Override
    public void submit(int requestCode, Intent data) {
        if (requestCode != type) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
        } else {
            processFile(data);
        }
    }

    private void processFile(Intent data) {
        ContentResolver cr = getContext().getContentResolver();
        String type = cr.getType(data.getData());
        if (Config.DEBUG) {
            Log.i(TAG, "Document Type: " + type);
        }

        FileProcessorThread thread = new FileProcessorThread(data.getDataString(),
                foldername, shouldCreateThumbnails);
        thread.setListener(this);
        if (activity != null) {
            thread.setContext(activity.getApplicationContext());
        } else if (fragment != null) {
            thread.setContext(fragment.getActivity()
                    .getApplicationContext());
        } else if (appFragment != null) {
            thread.setContext(appFragment.getActivity()
                    .getApplicationContext());
        }
        thread.start();
    }

    @Override
    public void onProcessedFile(ChosenFile file) {
        if (listener != null) {
            listener.onFileChosen(file);
        }
    }

    @Override
    public void onError(String reason) {
        super.onError(reason);
        if (listener != null) {
            listener.onError(reason);
        }
    }
}