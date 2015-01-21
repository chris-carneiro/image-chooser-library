package net.opencurlybraces.android.imagepickersample.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import net.opencurlybraces.android.imagepickerlite.api.PickerType;
import net.opencurlybraces.android.imagepickerlite.api.PickedImage;
import net.opencurlybraces.android.imagepickerlite.api.ImagePickerListener;
import net.opencurlybraces.android.imagepickerlite.api.ImagePickerManager;
import net.opencurlybraces.android.imagepickersample.R;

@SuppressLint("NewApi")
public class ImagePickerFragment extends Fragment implements
        ImagePickerListener {
	private ImagePickerManager mImagePickerManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.image_picker_activity, null);
		Button buttonPickImage = (Button) view
				.findViewById(R.id.buttonPickImage);
		buttonPickImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
		return view;
	}

	private void pickImage() {
		int chooserType = PickerType.REQUEST_PICK_PICTURE;
		mImagePickerManager = new ImagePickerManager(this,
				PickerType.REQUEST_PICK_PICTURE, "myfolder", true);
		mImagePickerManager.setImagePickerListener(this);
		try {
			String filePath = mImagePickerManager.pick();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("On Activity Result", requestCode + "");
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onImageChosen(PickedImage image) {

	}

	@Override
	public void onError(String reason) {

	}
}
