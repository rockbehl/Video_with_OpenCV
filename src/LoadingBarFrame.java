import javax.swing.*;
import java.awt.*;

public class LoadingBarFrame {
    private JFrame frame = new JFrame("Loading...");
    private JProgressBar progressBar = new JProgressBar();
    private int max;

    public LoadingBarFrame(int max) {
        this.max = max;
        progressBar.setValue(10);
        progressBar.setBounds(0,0,420,50);
        progressBar.setStringPainted(true);

        frame.add(progressBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
        fill();
    }

    public void fill()
    {
        int counter =0;
        while(counter <= 100)
        {
            try
            {
                Thread.sleep(1000);
                progressBar.setValue(counter);
            }
            catch(Exception e)
            {

            }
        }
    }

    public void updateProgress(int current) {
        progressBar.setValue(current);
        if (current == max) {
            //frame.dispose();
            frame.setVisible(false);
        }
    }
}



