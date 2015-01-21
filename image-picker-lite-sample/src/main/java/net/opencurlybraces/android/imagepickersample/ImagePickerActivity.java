/*******************************************************************************
 * Copyright 2013 Kumar Bibek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    
 * http://www.apache.org/licenses/LICENSE-2.0
 * 	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package net.opencurlybraces.android.imagepickersample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.opencurlybraces.android.imagepickerlite.api.PickerType;
import net.opencurlybraces.android.imagepickerlite.api.PickedImage;
import net.opencurlybraces.android.imagepickerlite.api.ImagePickerListener;
import net.opencurlybraces.android.imagepickerlite.api.ImagePickerManager;

import java.io.File;

public class ImagePickerActivity extends Activity implements
        ImagePickerListener {

	private ImageView mImageViewThumbnail;

	private ImageView mImageViewThumbSmall;

	private TextView mTextViewFile;

	private ImagePickerManager mImagePickerManager;

	private ProgressBar mProgressBar;

	private String mFilePath;

	private int mChooserType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_picker_activity);

		Button buttonTakePicture = (Button) findViewById(R.id.buttonTakePicture);
		buttonTakePicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
		Button buttonPickImage = (Button) findViewById(R.id.buttonPickImage);
		buttonPickImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

		mImageViewThumbnail = (ImageView) findViewById(R.id.imageViewThumb);
		mImageViewThumbSmall = (ImageView) findViewById(R.id.imageViewThumbSmall);
		mTextViewFile = (TextView) findViewById(R.id.textViewFile);

		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.GONE);
	}

	private void pickImage() {
		mChooserType = PickerType.REQUEST_PICK_PICTURE;
		mImagePickerManager = new ImagePickerManager(this,
				PickerType.REQUEST_PICK_PICTURE, "myfolder", true);
		mImagePickerManager.setImagePickerListener(this);
		try {
			mProgressBar.setVisibility(View.VISIBLE);
			mFilePath = mImagePickerManager.pick();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void takePicture() {
		mChooserType = PickerType.REQUEST_CAPTURE_PICTURE;
		mImagePickerManager = new ImagePickerManager(this,
				PickerType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
		mImagePickerManager.setImagePickerListener(this);
		try {
			mProgressBar.setVisibility(View.VISIBLE);
			mFilePath = mImagePickerManager.pick();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& (requestCode == PickerType.REQUEST_PICK_PICTURE || requestCode == PickerType.REQUEST_CAPTURE_PICTURE)) {
			if (mImagePickerManager == null) {
				reinitializeImageChooser();
			}
			mImagePickerManager.submit(requestCode, data);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void onImageChosen(final PickedImage image) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mProgressBar.setVisibility(View.GONE);
				if (image != null) {
					mTextViewFile.setText(image.getFilePathOriginal());
					mImageViewThumbnail.setImageURI(Uri.parse(new File(image
                            .getFileThumbnail()).toString()));
					mImageViewThumbSmall.setImageURI(Uri.parse(new File(image
                            .getFileThumbnailSmall()).toString()));
				}
			}
		});
	}

	@Override
	public void onError(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mProgressBar.setVisibility(View.GONE);
				Toast.makeText(ImagePickerActivity.this, reason,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// Should be called if for some reason the ImagePickerManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() {
		mImagePickerManager = new ImagePickerManager(this, mChooserType,
				"myfolder", true);
		mImagePickerManager.setImagePickerListener(this);
		mImagePickerManager.reinitialize(mFilePath);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", mChooserType);
		outState.putString("media_path", mFilePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("chooser_type")) {
				mChooserType = savedInstanceState.getInt("chooser_type");
			}

			if (savedInstanceState.containsKey("media_path")) {
				mFilePath = savedInstanceState.getString("media_path");
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
