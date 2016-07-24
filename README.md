# screen-recorder
Java screen recorder library

<img src="resources/images/java-icon.png" height="128" />
<img src="resources/images/play-icon.png" height="128" />

Description
-----------

Screen recorder is an Open Source library (under the [MIT Licence](LICENSE)) that allows you to record the screen from your Java code.

Nowadays the library "only" records the screen into a .mov format, creating videos without audio using the minimum resources.

Getting started
-----------

###Adding dependencies

--
 1. Add the repository:

   ```xml
  <repositories>
		<repository>
			<id>screen-recorder</id>
			<name>Java screen recorder library by agomezmoron</name>
			<url>https://raw.github.com/agomezmoron/screen-recorder/mvn-repo</url>
		</repository>
	</repositories>
    ```
 2. Adding the following maven dependency in you ```pom.xml``` file:


    ```xml 
    <dependency>
      <groupId>com.github.agomezmoron</groupId>
      <artifactId>screen-recorder</artifactId>
      <version>0.0.1</version>
    </dependency>
    ```
    
###How to use

It's very easy: 

 1. Configure the video interval in ms: by default it is 100ms (10 frames/sec).
 2. Configure the width/height or the full screen mode: by default it's full screen mode.
 3. Configure the video directoy where the video will be saved: by default is the temporal folder.
 4. Set the *keepFrames* option into true/false if you want to keep the frames as .jpeg files: by defaul the library doesn't keep the frames.
 5. Call the *start* method passing the video name (ex: "myVideo" -> output: myVideo.mov).
 6. Perform your actions...
 7. Call the *stop* method

Example:

 ```
  VideoRecorder.setCaptureInterval(50); // 20 frames/sec
  VideoRecorder.fullScreenMode(true);
  VideoRecorder.setVideoDirectory("~/"); // home
  VideoRecorder.keepFramesInTempDirectory(false);
  VideoRecorder.start("test");
  Thread.sleep(5000);
  String videoPath = VideoRecorder.stop(); // video created
  System.out.println(videoPath);
```

License
-----------
The MIT License (MIT)

Copyright (c) 2016 Alejandro Gómez Morón

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
