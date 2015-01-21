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

package net.opencurlybraces.android.imagepickerlite.api;

import java.io.File;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import net.opencurlybraces.android.imagepickerlite.api.config.Config;
import net.opencurlybraces.android.imagepickerlite.threads.ImageProcessorListener;
import net.opencurlybraces.android.imagepickerlite.threads.ImageProcessorThread;

/**
 * Easy Image Chooser Library for Android Apps. Forget about coding workarounds
 * for different devices, OSes and folders.
 * 
 * @author Beanie
 * Modified by Chris Carneiro
 */
public class ImagePickerManager extends AbstractPicker implements
		ImageProcessorListener {
	private final static String TAG = "ImagePickerManager";

	private final static String DIRECTORY = "bimagechooser";

	private ImagePickerListener listener;

	/**
	 * Simplest constructor. Specify the type
	 * {@link PickerType.REQUEST_PICK_PICTURE} or
	 * {@link PickerType.REQUEST_CAPTURE_PICTURE}
	 * 
	 * @param activity
	 * @param type
	 */
	public ImagePickerManager(Activity activity, int type) {
		super(activity, type, DIRECTORY, true);
	}

	public ImagePickerManager(Fragment fragment, int type) {
		super(fragment, type, DIRECTORY, true);
	}

	public ImagePickerManager(android.app.Fragment fragment, int type) {
		super(fragment, type, DIRECTORY, true);
	}

	/**
	 * Specify the type {@link PickerType.REQUEST_PICK_PICTURE} or
	 * {@link PickerType.REQUEST_CAPTURE_PICTURE}
	 * <p>
	 * Optionally, you can control where the exported images with their
	 * thumbnails would be stored.
	 * </p>
	 * 
	 * @param activity
	 * @param type
	 * @param folderName
	 */
	public ImagePickerManager(Activity activity, int type, String folderName) {
		super(activity, type, folderName, true);
	}

	public ImagePickerManager(Fragment fragment, int type, String folderName) {
		super(fragment, type, folderName, true);
	}

	public ImagePickerManager(android.app.Fragment fragment, int type,
                              String folderName) {
		super(fragment, type, folderName, true);
	}

	/**
	 * Specify the type {@link PickerType.REQUEST_PICK_PICTURE} or
	 * {@link PickerType.REQUEST_CAPTURE_PICTURE}
	 * <p>
	 * Optionally, you can set whether you need thumbnail generation or not. If
	 * not, you would get the original image for the thumbnails as well
	 * </p>
	 * 
	 * @param activity
	 * @param type
	 * @param shouldCreateThumbnails
	 */
	public ImagePickerManager(Activity activity, int type,
                              boolean shouldCreateThumbnails) {
		super(activity, type, DIRECTORY, shouldCreateThumbnails);
	}

	public ImagePickerManager(Fragment fragment, int type,
                              boolean shouldCreateThumbnails) {
		super(fragment, type, DIRECTORY, shouldCreateThumbnails);
	}

	public ImagePickerManager(android.app.Fragment fragment, int type,
                              boolean shouldCreateThumbnails) {
		super(fragment, type, DIRECTORY, shouldCreateThumbnails);
	}

	/**
	 * Specify the type {@link PickerType.REQUEST_PICK_PICTURE} or
	 * {@link PickerType.REQUEST_CAPTURE_PICTURE}
	 * <p>
	 * Specify your own mFolderName and whether you want the generated thumbnails
	 * or not
	 * </p>
	 * 
	 * @param activity
	 * @param type
	 * @param folderName
	 * @param shouldCreateThumbnails
	 */
	public ImagePickerManager(Activity activity, int type, String folderName,
                              boolean shouldCreateThumbnails) {
		super(activity, type, folderName, shouldCreateThumbnails);
	}

	public ImagePickerManager(Fragment fragment, int type, String folderName,
                              boolean shouldCreateThumbnails) {
		super(fragment, type, folderName, shouldCreateThumbnails);
	}

	public ImagePickerManager(android.app.Fragment fragment, int type,
                              String folderName, boolean shouldCreateThumbnails) {
		super(fragment, type, folderName, shouldCreateThumbnails);
	}

	/**
	 * Set a listener, to get callbacks when the images and the thumbnails are
	 * processed
	 * 
	 * @param listener
	 */
	public void setImagePickerListener(ImagePickerListener listener) {
		this.listener = listener;
	}

	@Override
	public String pick() throws Exception {
		String path = null;
		if (listener == null) {
			throw new IllegalArgumentException(
					"ImagePickerListener cannot be null. Forgot to set ImagePickerListener???");
		}
		switch (type) {
		case PickerType.REQUEST_PICK_PICTURE:
			pickPicture();
			break;
		case PickerType.REQUEST_CAPTURE_PICTURE:
			path = takePicture();
			break;
		default:
			throw new IllegalArgumentException(
					"Cannot pick a video in ImagePickerManager");
		}
		return path;
	}

	private void pickPicture() throws Exception {
		checkDirectory();
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			if (mExtras != null) {
				intent.putExtras(mExtras);
			}
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			throw new Exception("Activity not found");
		}
	}

	private String takePicture() throws Exception {
		checkDirectory();
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mOriginalFilePath = FileUtils.getDirectory(mFolderName)
					+ File.separator + Calendar.getInstance().getTimeInMillis()
					+ ".jpg";
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(mOriginalFilePath)));
			if (mExtras != null) {
				intent.putExtras(mExtras);
			}
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			throw new Exception("Activity not found");
		}
		return mOriginalFilePath;
	}

	@Override
	public void submit(int requestCode, Intent data) {
		if (requestCode != type) {
			onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
		} else {
			switch (requestCode) {
			case PickerType.REQUEST_PICK_PICTURE:
				processImageFromGallery(data);
				break;
			case PickerType.REQUEST_CAPTURE_PICTURE:
				processCameraImage();
				break;
			}
		}
	}

	@SuppressLint("NewApi")
	private void processImageFromGallery(Intent data) {
		if (data != null && data.getDataString() != null) {
			String uri = data.getData().toString();
			sanitizeURI(uri);
			if (mOriginalFilePath == null || TextUtils.isEmpty(mOriginalFilePath)) {
				onError("File path was null");
			} else {
				if (Config.DEBUG) {
					Log.i(TAG, "File: " + mOriginalFilePath);
				}
				String path = mOriginalFilePath;
				ImageProcessorThread thread = new ImageProcessorThread(path,
                        mFolderName, mShouldCreateThumbnails);
				thread.setListener(this);
				if (activity != null) {
					thread.setContext(activity.getApplicationContext());
				} else if (mFragment != null) {
					thread.setContext(mFragment.getActivity()
							.getApplicationContext());
				} else if (mAppFragment != null) {
					thread.setContext(mAppFragment.getActivity()
							.getApplicationContext());
				}
				thread.start();
			}
		} else {
			onError("Image Uri was null!");
		}

	}

	private void processCameraImage() {
		String path = mOriginalFilePath;
		ImageProcessorThread thread = new ImageProcessorThread(path,
                mFolderName, mShouldCreateThumbnails);
		thread.setListener(this);
		thread.start();
	}

	@Override
	public void onProcessedImage(PickedImage image) {
		if (listener != null) {
			listener.onImageChosen(image);
		}
	}

	@Override
	public void onError(String reason) {
		if (listener != null) {
			listener.onError(reason);
		}
	}
}
