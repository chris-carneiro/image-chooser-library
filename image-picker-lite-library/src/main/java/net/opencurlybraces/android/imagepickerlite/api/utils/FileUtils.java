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
 * 
 * Modified by Chris Carneiro
 *******************************************************************************/

package net.opencurlybraces.android.imagepickerlite.api.utils;

import java.io.File;

import android.os.Environment;

public class FileUtils {

    /**
     * @param foldername
     * @return
     */
    public static String getDirectory(String foldername) {
        File directory = null;
        directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + foldername);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory.getAbsolutePath();
    }

    /**
     * Try to return a file extension i.e. "file.ext"
     *
     * @param filename
     * @return Extension including "." if any, an empty string if no extension and null if {@code
     * filename} is null
     */
    public static String getFileExtension(String filename) {
        if (filename == null)
            return null;
        String trimmed = filename.trim();
        int lastDotIndex = trimmed.lastIndexOf(".");

        return ((lastDotIndex >= 0 &&
                trimmed.substring(lastDotIndex + 1).length() > 0 ) ?
                trimmed.substring(lastDotIndex) : "");
    }
}