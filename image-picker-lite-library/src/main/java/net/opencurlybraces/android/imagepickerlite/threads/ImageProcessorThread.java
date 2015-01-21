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

package net.opencurlybraces.android.imagepickerlite.threads;

import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import net.opencurlybraces.android.imagepickerlite.api.PickedImage;
import net.opencurlybraces.android.imagepickerlite.api.config.Config;

public class ImageProcessorThread extends MediaProcessorThread {

	private final static String TAG = "ImageProcessorThread";

	private ImageProcessorListener mListener;

	private final static int MAX_DIRECTORY_SIZE = 5 * 1024 * 1024;

	private final static int MAX_THRESHOLD_DAYS = (int) (0.5 * 24 * 60 * 60 * 1000);

	public ImageProcessorThread(String filePath, String folderName,
			boolean shouldCreateThumbnails) {
		super(filePath, folderName, shouldCreateThumbnails);
		setMediaExtension("jpg");
	}

	public void setListener(ImageProcessorListener listener) {
		this.mListener = listener;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	@Override
	public void run() {
		try {
			manageDiretoryCache(MAX_DIRECTORY_SIZE, MAX_THRESHOLD_DAYS, "jpg");
			processImage();
		} catch (IOException e) {
			e.printStackTrace();
			if (mListener != null) {
				mListener.onError(e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mListener != null) {
				mListener.onError(e.getMessage());
			}
		}
	}

	private void processImage() throws Exception {

		if (Config.DEBUG) {
			Log.i(TAG, "Processing Image File: " + mFilePath);
		}

		// Picasa on Android >= 3.0
		if (mFilePath != null && mFilePath.startsWith("content:")) {
			mFilePath = getAbsoluteImagePathFromUri(Uri.parse(mFilePath));
		}
		if (mFilePath == null || TextUtils.isEmpty(mFilePath)) {
			if (mListener != null) {
				mListener.onError("Couldn't process a null file");
			}
		} else if (mFilePath.startsWith("http")) {
			downloadAndProcess(mFilePath);
		} else if (mFilePath
				.startsWith("content://com.google.android.gallery3d")
				|| mFilePath
						.startsWith("content://com.microsoft.skydrive.content.external")) {
			processPicasaMedia(mFilePath, ".jpg");
		} else if (mFilePath
				.startsWith("content://com.google.android.apps.photos.content")
				|| mFilePath
						.startsWith("content://com.android.providers.media.documents")
				|| mFilePath
						.startsWith("content://com.google.android.apps.docs.storage")) {
			processGooglePhotosMedia(mFilePath, ".jpg");
		} else {
			process();
		}
	}

	@Override
	protected void process() throws Exception {
		super.process();
		if (mShouldCreateThumbnails) {
			String[] thumbnails = createThumbnails(this.mFilePath);
			processingDone(this.mFilePath, thumbnails[0], thumbnails[1]);
		} else {
			processingDone(this.mFilePath, this.mFilePath, this.mFilePath);
		}
	}

	@Override
	protected void processingDone(String original, String thumbnail,
			String thumbnailSmall) {
		if (mListener != null) {
			PickedImage image = new PickedImage();
			image.setFilePathOriginal(original);
			image.setFileThumbnail(thumbnail);
			image.setFileThumbnailSmall(thumbnailSmall);
			mListener.onProcessedImage(image);
		}
	}
}
