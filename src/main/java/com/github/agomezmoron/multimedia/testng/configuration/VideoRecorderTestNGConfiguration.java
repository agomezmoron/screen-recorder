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
package com.github.agomezmoron.multimedia.testng.configuration;

import com.github.agomezmoron.multimedia.recorder.configuration.VideoRecorderConfiguration;
import com.github.agomezmoron.multimedia.testng.listener.VideoRecorderListener;

/**
 * Video recorder configuration to be used in the TestNG tests through the {@link VideoRecorderListener}.
 * @author Alejandro Gomez <agommor@gmail.com>
 *
 */
public final class VideoRecorderTestNGConfiguration extends VideoRecorderConfiguration {

    /**
     * Flag to check if the test should be recorder.
     */
    private static boolean recordTest = true;

    /**
     * Flag to know if the video should be kept on success.
     */
    private static boolean keepVideoOnSuccess = false;

    /**
     * Flag to know if the video should be kept on failure.
     */
    private static boolean keepVideoOnFailure = true;

    /**
     * @return the recordTest
     */
    public static boolean wantToRecordTest() {
        return recordTest;
    }

    /**
     * @param recordTest the recordTest to set
     */
    public static void wantToRecordTest(boolean recordTest) {
        VideoRecorderTestNGConfiguration.recordTest = recordTest;
    }

    /**
     * @return the keepVideoOnSuccess
     */
    public static boolean wantToKeepVideoOnSuccess() {
        return keepVideoOnSuccess;
    }

    /**
     * @param keepVideoOnSuccess the keepVideoOnSuccess to set
     */
    public static void wantToKeepVideoOnSuccess(boolean keepVideoOnSuccess) {
        VideoRecorderTestNGConfiguration.keepVideoOnSuccess = keepVideoOnSuccess;
    }

    /**
     * @return the keepVideoOnFailure
     */
    public static boolean wantToKeepVideoOnFailure() {
        return keepVideoOnFailure;
    }

    /**
     * @param keepVideoOnFailure the keepVideoOnFailure to set
     */
    public static void wantToKeepVideoOnFailure(boolean keepVideoOnFailure) {
        VideoRecorderTestNGConfiguration.keepVideoOnFailure = keepVideoOnFailure;
    }

}
