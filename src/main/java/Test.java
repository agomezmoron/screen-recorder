import com.github.agomezmoron.multimedia.recorder.VideoRecorder;

public class Test {

    public static void main(String[] args) throws Exception {
        VideoRecorder.setCaptureInterval(100);
        VideoRecorder.fullScreenMode(true);
        VideoRecorder.setVideoDirectory("/home/amoron/Escritorio");
        VideoRecorder.start("test");
        Thread.sleep(5000);
        VideoRecorder.stop();

    }

}
