/**
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Alejandro Gómez Morón
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.agomezmoron.multimedia.recorder;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.media.MediaLocator;

import com.github.agomezmoron.multimedia.capture.ScreenCapture;
import com.github.agomezmoron.multimedia.external.JpegImagesToMovie;

/**
 * It models the video recorder.
 * 
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
public class VideoRecorder {

    /**
     * Status of the recorder.
     */
    private static boolean recording = false;

    /**
     * Interval where the images will be capture (in milliseconds).
     */
    private static int captureInterval = 100;

    /**
     * Screen Width.
     */
    private static int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    /**
     * Screen Height.
     */
    private static int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    /**
     * Associated frames.
     */
    private static List<String> frames;

    /**
     * Temporal directory to be used.
     */
    private static File tempDirectory = (System.getProperty("java.io.tmpdir") != null)
            ? new File(System.getProperty("java.io.tmpdir")) : new File(".");

    /**
     * Flag to know if the user want to keep the frames.
     */
    private static boolean keepFrames = false;

    /**
     * Flag to know if the video will be in full screen or not.
     */
    private static boolean useFullScreen = true;

    /**
     * Video name.
     */
    private static String videoName = "output.mov";

    /**
     * X coordinate.
     */
    private static int x = 0;

    /**
     * y coordinate.
     */
    private static int y = 0;

    /**
     * Video path where the video will be saved.
     */
    private static File videoPath = (System.getProperty("java.io.tmpdir") != null)
            ? new File(System.getProperty("java.io.tmpdir")) : new File(".");

    /**
     * Strategy to record using {@link Thread}.
     */
    private static final Thread recordThread = new Thread() {
        @Override
        public void run() {
            Robot rt;
            ScreenCapture capture;
            try {
                rt = new Robot();
                do {
                    capture = new ScreenCapture(rt.createScreenCapture(new Rectangle(x, y, width, height)));
                    frames.add(VideoRecorderUtil.saveIntoDirectory(capture, new File(
                            tempDirectory.getAbsolutePath() + File.separatorChar + videoName.replace(".mov", ""))));
                    Thread.sleep(captureInterval);
                } while (recording);
            } catch (Exception e) {
                recording = false;
            }
        }
    };

    /**
     * We don't allow to create objects for this class.
     */
    private VideoRecorder() {

    }

    /**
     * It stops the recording and creates the video.
     * @return a {@link String} with the path where the video was created or null if the video couldn't be created.
     * @throws MalformedURLException
     */
    public static String stop() throws MalformedURLException {
        String videoPathString = null;
        if (recording) {
            recording = false;
            if (recordThread.isAlive()) {
                recordThread.interrupt();
            }
            videoPathString = createVideo();
            frames.clear();
            if (!keepFrames) {
                deleteDirectory(new File(
                        tempDirectory.getAbsolutePath() + File.separatorChar + videoName.replace(".mov", "")));
            }
        }
        return videoPathString;
    }

    /**
     * It starts recording (if it wasn't started before).
     * @param newVideoName with the output of the video.
     */
    public static void start(String newVideoName) {
        if (!recording) {
            calculateScreenshotSize();
            videoName = newVideoName;
            if (!videoName.endsWith(".mov")) {
                videoName += ".mov";
            }
            recording = true;
            frames = new ArrayList<String>();
            recordThread.start();
        }
    }

    /**
     * @return the captureInterval.
     */
    public static int getCaptureInterval() {
        return captureInterval;
    }

    /**
     * @param newCaptureInterval the captureInterval to set.
     */
    public static void setCaptureInterval(int newCaptureInterval) {
        captureInterval = newCaptureInterval;
    }

    /**
     * @return the width.
     */
    public static int getWidth() {
        return width;
    }

    /**
     * @param newWidth the width to set.
     */
    public static void setWidth(int newWidth) {
        if (newWidth >= 0 && newWidth <= Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
            width = newWidth;
            if (width < Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
                useFullScreen = false;
            }
        }
    }

    /**
     * @return the height.
     */
    public static int getHeight() {
        return height;
    }

    /**
     * @param newHeight the height to set.
     */
    public static void setHeight(int newHeight) {
        if (newHeight >= 0 && newHeight <= Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
            height = newHeight;
            if (height < Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
                useFullScreen = false;
            }
        }
    }

    /**
     * It sets the directory where the video will be created.
     * @param path where the video will be created.
     */
    public static void setVideoDirectory(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        if (f.exists() && f.canWrite()) {
            videoPath = f;
        }
    }

    /**
     * It sets the temporal directory to be use.
     * @param path to be use as temporal directory.
     */
    public static void setTempDirectory(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        if (f.exists() && f.canWrite()) {
            tempDirectory = f;
        }
    }

    /**
     * It enables or disables keeping the frames after making the video.
     * @param keepFrames yes or no.
     */
    public static void keepFramesInTempDirectory(boolean keep) {
        keepFrames = keep;
    }

    /**
     * It sets the width and height value to the full screen size, ignoring previous setWidth and setHeight calls.
     * @param newUseFullScreen if you want to record the full screen or not. If false, the recorder will record the 
     */
    public static void fullScreenMode(boolean newUseFullScreen) {
        useFullScreen = newUseFullScreen;
    }

    /**
     * It sets the x coordinate.
     * @param newX to be used.
     * @param newY to be used.
     */
    public static void setCoordinates(int newX, int newY) {
        if (newX >= 0 && newX <= Toolkit.getDefaultToolkit().getScreenSize().getWidth() && newY >= 0
                && newY <= Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
            x = newX;
            y = newY;
        }
    }

    /**
     * It calculates the screenshot size before recording. If the useFullScreen was defined, the width, height or x
     */
    private static void calculateScreenshotSize() {
        // if fullScreen was set, all the configuration will be changed back.
        if (useFullScreen) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            width = (int) size.getWidth();
            height = (int) size.getHeight();
            x = 0;
            y = 0;
        } else {
            // we have to check if x+width <= Toolkit.getDefaultToolkit().getScreenSize().getWidth() and the same for
            // the height
            if (x + width > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
                width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - x);
            }
            if (y + height > Toolkit.getDefaultToolkit().getScreenSize().getHeight()) {
                height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - y);
            }
        }
    }

    /**
     * It deletes recursively a directory.
     * @param directory to be deleted.
     * @return true if the directory was deleted successfully.
     */
    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    /**
     * It creates the video.
     * @return a {@link String} with the path where the video was created or null if the video couldn't be created.
     * @throws MalformedURLException
     */
    private static String createVideo() throws MalformedURLException {
        String videoPathString = null;
        JpegImagesToMovie jpegImaveToMovie = new JpegImagesToMovie();
        MediaLocator oml;
        if ((oml = JpegImagesToMovie
                .createMediaLocator(videoPath.getAbsolutePath() + File.separatorChar + videoName)) == null) {
            System.exit(0);
        }
        if (jpegImaveToMovie.doIt(width, height, (1000 / captureInterval), new Vector<String>(frames), oml)) {
            videoPathString = videoPath.getAbsolutePath() + File.separatorChar + videoName;
        }
        return videoPathString;
    }

}
