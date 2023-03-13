import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.time.LocalDate;

import static org.opencv.videoio.Videoio.CAP_PROP_FPS;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_COUNT;

public class ocvInterface {

    ocvInterface() {
    }

    public void compressInputHandeler(File input, int pos, double cR, String nC) {
        System.out.println("file in");

        StringBuilder outName = new StringBuilder("testFiles/");

        File outFile = compressVideo(input.toString(), cR);
        String[] nCarr = nC.split("$");
        for (int i = 0; i < nCarr.length; i++) {
            if (!nCarr[i].equals("$")) {
                switch (nCarr[i].toLowerCase()) {
                    case "milisec" -> outName.append(System.currentTimeMillis());
                    case "date" -> outName.append(LocalDate.now());
                    case "action" -> outName.append("comp");
                }
            }
            outName.append(System.currentTimeMillis()+'_');
        }
        outName.append('_');
        outName.append(pos);
        outName.append(".mov");
        outFile.renameTo(new File(outName.toString()));

    }

    public void cutInputHandeler(File input, double sT, double eT, String nC){
        System.out.println("file in");

        StringBuilder outName = new StringBuilder("testFiles/");

        File outFile = cutFrom(input.toString(), sT, eT);
        String[] nCarr = nC.split("//$");
        for (int i = 0; i < nCarr.length; i++) {
            switch (nCarr[i].toLowerCase()){
                case "milisec" -> outName.append(System.currentTimeMillis());
                case "date" -> outName.append(LocalDate.now());
                case "action" -> outName.append("cutAt"+sT+eT);
            }
            outName.append(System.currentTimeMillis()+'_');
        }
        outName.append(".mov");
        outFile.renameTo(new File(outName.toString()));
    }

    public int getVideolength(File input){
        String videoFile = input.getPath();
        VideoCapture capture = new VideoCapture(videoFile);
        double fps = capture.get(CAP_PROP_FPS);
        int frameCount = (int) capture.get(CAP_PROP_FRAME_COUNT);
        return (int) (frameCount / fps);
    }

    private File compressVideo(String inputFile, double compressionRatio){

        File outputFile = new File("workingFile/compressing.mov");
        System.out.println("files set");

        // Create a VideoCapture object to read the input video
        VideoCapture capture = new VideoCapture(inputFile);

        // Get the video frames per second
        double fps = capture.get(CAP_PROP_FPS);
        double framesMax = capture.get(CAP_PROP_FRAME_COUNT);

        // Get the video frame size
        int frameWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        System.out.println("check 1");

        // Create a VideoWriter object to write the output video
        VideoWriter writer = new VideoWriter(outputFile.getPath(), (int) capture.get(Videoio.CAP_PROP_FOURCC), fps, new Size(frameWidth, frameHeight), true);
        System.out.println("check 2");

        // Set the compression ratio
        writer.set(Videoio.VIDEOWRITER_PROP_QUALITY, compressionRatio);

        // Read and write the video frames
        //LoadingBarFrame progressReport = new LoadingBarFrame(((int) framesMax));

        Mat frame = new Mat();
        int state = 0;
        while (capture.read(frame)) {
            writer.write(frame);
            state += 1;
            System.out.println("check in: " + state);
            if (state % 20 == 0) {
                System.out.println("banna");
                //progressReport.updateProgress(state);
            }
        }

        // Release the resources
        capture.release();
        writer.release();
        System.out.println("check 3");

        return outputFile;
    }

    private File cutFrom(String inputFile, double startTime, double endTime) {

        System.out.println("check in 1");
        File outputFile = new File("workingFile/cutting.mov");

        // Create a VideoCapture object to read the input video
        VideoCapture capture = new VideoCapture(inputFile);

        // Get the video frames per second
        double fps = capture.get(CAP_PROP_FPS);

        // Get the video frame size
        int frameWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int frameHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        // Create a VideoWriter object to write the output video
        VideoWriter writer = new VideoWriter(outputFile.getPath(), (int) capture.get(Videoio.CAP_PROP_FOURCC), fps, new Size(frameWidth, frameHeight), true);
        System.out.println("check in 2");

        // Set the starting position
        capture.set(Videoio.CAP_PROP_POS_MSEC, startTime * 1000);

        // Read and write the video frames
        Mat frame = new Mat();
        while (capture.read(frame)) {
            double currentTime = capture.get(Videoio.CAP_PROP_POS_MSEC) / 1000.0;
            if (currentTime > endTime) {
                break;
            }
            writer.write(frame);
            System.out.println("check in 3");
        }

        // Release the resources
        capture.release();
        writer.release();

                System.out.println("check in 4");

        return outputFile;
    }



}
