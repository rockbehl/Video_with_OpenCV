import javax.swing.*;
import java.awt.*;

public class LoadingBarFrame {
    private final JFrame frame;
    private final JProgressBar progressBar;
    private final int max;

    public LoadingBarFrame(int max) {
        this.max = max;
        frame = new JFrame("Loading...");
        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        progressBar = new JProgressBar();
        progressBar.setMaximum(max);
        frame.add(progressBar);
        frame.setVisible(true);
    }

    public void updateProgress(int current) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(current);
            if (current == max) {
                frame.dispose();
            }
        });
    }
}
