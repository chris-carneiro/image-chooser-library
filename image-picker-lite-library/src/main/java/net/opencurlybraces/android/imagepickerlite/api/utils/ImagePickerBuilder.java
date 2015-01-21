package net.opencurlybraces.android.imagepickerlite.api.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.support.annotation.NonNull;

import net.opencurlybraces.android.imagepickerlite.api.PickerType;

public class ImagePickerBuilder extends Builder {
	private OnClickListener mListener;

	private Context mContext;

	private String mTitle;

	private String mTitleGalleryOption;

	private String mTitleTakePictureOption;

	@SuppressLint("NewApi")
	public ImagePickerBuilder(Context context, int theme,
                              OnClickListener listener) {
		super(context, theme);
		this.mListener = listener;
		this.mContext = context;
        setupDefaultData();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ImagePickerBuilder(Context context, OnClickListener listener) {
		super(context);
		this.mListener = listener;
		this.mContext = context;
        setupDefaultData();
	}

	public Builder setDialogTitle(String title) {
		this.mTitle = title;
        return this;
	}

	public Builder setDialogTitle(int resId) {
		this.mTitle = mContext.getString(resId);
        return this;
	}

	public Builder setTitleGalleryOption(String titleGalleryOption) {
		this.mTitleGalleryOption = titleGalleryOption;
        return this;
	}

	public Builder setTitleGalleryOption(int resId) {
		this.mTitleGalleryOption = mContext.getString(resId);
        return this;
	}

	public Builder setTitleTakePictureOption(String titleTakePictureOption) {
		this.mTitleTakePictureOption = titleTakePictureOption;
        return this;
	}

	public Builder setTitleTakePictureOption(int resId) {
		this.mTitleTakePictureOption = mContext.getString(resId);
        return this;
	}

    private void setupDefaultData(){
        mTitle = "Choose an option";
        mTitleGalleryOption = "Pick from Gallery";
        mTitleTakePictureOption = "Take a picture";
    }

    @NonNull
    @Override
    public AlertDialog create() {
        setTitle(mTitle);
        CharSequence[] titles = {mTitleGalleryOption, mTitleTakePictureOption};
        setItems(titles, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mListener.onClick(dialog, PickerType.REQUEST_PICK_PICTURE);
                } else if (which == 1) {
                    mListener.onClick(dialog,
                            PickerType.REQUEST_CAPTURE_PICTURE);
                }
            }
        });
        AlertDialog d = super.create();
        return d;
    }
}
