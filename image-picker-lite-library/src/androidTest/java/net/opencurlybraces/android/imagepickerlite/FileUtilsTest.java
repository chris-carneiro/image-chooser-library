package net.opencurlybraces.android.imagepickerlite;

import android.test.AndroidTestCase;

import net.opencurlybraces.android.imagepickerlite.api.FileUtils;

/**
 * Created by chris on 22/01/15.
 */
public class FileUtilsTest extends AndroidTestCase {
    static final String MAIN_STORAGE = "/storage/emulated/0";
    static final String EMPTY = "";
    static final String EXTENSION = ".file";
    static final String NORMAL_FILE = "normal.file";
    static final String NO_EXTENSION = "no";
    static final String LEADING_DOT = ".file";
    static final String DOT = ".";
    static final String WEIRD = " 36789 2çééà )$ùd. dhue;\"éç)mde,zjo.weird ";
    static final String WEIRD_EXTENSION = ".weird";

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testDirectoryGetter() {
        String dir = FileUtils.getDirectory(EMPTY);
        assertEquals(MAIN_STORAGE + EMPTY, dir);
    }


    public void testFileExtensionGetter() {
        String extension = FileUtils.getFileExtension(NORMAL_FILE);
        assertEquals(EXTENSION, extension);
        extension = FileUtils.getFileExtension(NO_EXTENSION);
        assertEquals(EMPTY,extension);
        extension = FileUtils.getFileExtension(LEADING_DOT);
        assertEquals(EXTENSION,extension);
        extension = FileUtils.getFileExtension(DOT);
        assertNotSame(DOT,extension);
        extension = FileUtils.getFileExtension(WEIRD);
        assertEquals(WEIRD_EXTENSION, extension);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}

