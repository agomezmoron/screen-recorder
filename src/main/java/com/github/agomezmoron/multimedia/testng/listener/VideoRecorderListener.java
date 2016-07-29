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
package com.github.agomezmoron.multimedia.testng.listener;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.github.agomezmoron.multimedia.recorder.VideoRecorder;
import com.github.agomezmoron.multimedia.testng.configuration.VideoRecorderTestNGConfiguration;

/**
 * 
 * Strategy to take an videos.
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
public class VideoRecorderListener extends TestListenerAdapter {

    /**
     * Log instance.
     */
    private final static Logger LOGGER = Logger.getLogger(VideoRecorderListener.class);

    /**
     * @see {@link TestListenerAdapter#onTestStart(ITestResult)}
     */
    @Override
    public void onTestStart(ITestResult result) {
        if (VideoRecorderTestNGConfiguration.wantToRecordTest()) {
            VideoRecorder.start(result.getMethod().getMethodName() + "_" + new Date().getTime());
        }
    }

    /**
     * @see {@link TestListenerAdapter#onTestSuccess(ITestResult)}
     */
    public void onTestSuccess(ITestResult result) {
        File createdVideo = endVideo(result);
        if (createdVideo != null && !VideoRecorderTestNGConfiguration.wantToKeepVideoOnSuccess()) {
            createdVideo.delete();
        }
    }

    /**
     * @see {@link TestListenerAdapter#onTestFailure(ITestResult)}
     */
    @Override
    public void onTestFailure(ITestResult result) {
        File createdVideo = endVideo(result);
        if (createdVideo != null && !VideoRecorderTestNGConfiguration.wantToKeepVideoOnFailure()) {
            createdVideo.delete();
        }
    }

    /**
     * @see {@link TestListenerAdapter#onTestSkipped(ITestResult)}
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        File createdVideo = endVideo(result);
        if (createdVideo != null) {
            createdVideo.delete();
        }
    }

    /**
     * It ends the video and move it if needed.
     * @param result {@link ITestResult}.
     * @return File with the path.
     */
    private File endVideo(ITestResult result) {
        File createdVideoFile = null;
        if (VideoRecorderTestNGConfiguration.wantToRecordTest()) {
            try {
                String createdVideo = VideoRecorder.stop();
                String videoName = "";
                if (createdVideo != null) {
                    String[] paths = createdVideo.split("" + File.separatorChar);
                    videoName = paths[paths.length - 1];
                }
                createdVideoFile = new File(createdVideo);
                if (VideoRecorderTestNGConfiguration.getDefaultDirectory().getAbsolutePath()
                        .equals(VideoRecorderTestNGConfiguration.getVideoDirectory().getAbsolutePath())) {
                    try {
                        Path newVideoPath = Paths.get(new File(".")
                                .getCanonicalPath(), "target", "screen-recorder", result.getTestClass().getName());
                        createdVideoFile = new File(newVideoPath.toFile().getCanonicalPath() + File.separatorChar);
                        if (!createdVideoFile.exists()) {
                            createdVideoFile.mkdirs();
                        }
                        newVideoPath = Paths.get(createdVideoFile.getCanonicalPath() + File.separatorChar + videoName);
                        Files.move(Paths.get(createdVideo), newVideoPath, StandardCopyOption.REPLACE_EXISTING);
                        LOGGER.info("Video created at " + newVideoPath.toFile().getCanonicalPath());
                    } catch (IOException e) {
                        LOGGER.error("An error occurred moving the video", e);
                    }
                } else {
                    LOGGER.info("Video created at " + createdVideo);
                }
            } catch (MalformedURLException e) {
                LOGGER.error("An error occurred creating the video", e);
            }
        }
        return createdVideoFile;
    }
}
