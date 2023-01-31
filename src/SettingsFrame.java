import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SettingsFrame extends JFrame {
    private final JSlider compressionRatioSlider;
    private final JTextField namingConventionField;
    private final JButton saveButton;
    private double compressionRatio;
    private String namingConvention;
    private final JLabel compressionRatioLabel;



    public SettingsFrame() {
        setTitle("Settings");
        setSize(200, 200);
        setResizable(false);
        setLayout(new FlowLayout());

        compressionRatioSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
        compressionRatioSlider.setMinorTickSpacing(1);
        compressionRatioSlider.setMajorTickSpacing(2);
        compressionRatioSlider.setPaintTicks(true);
        compressionRatioSlider.setPaintLabels(true);
        compressionRatioSlider.setSnapToTicks(true);
        compressionRatioSlider.setValue((int) getCompressionRatio()*10);

        compressionRatioLabel = new JLabel("Compression Ratio: 0.5");

        JLabel textBeforeNameC = new JLabel("Naming Convention: ");
        namingConventionField = new JTextField(10);

        compressionRatioSlider.addChangeListener(e -> compressionRatioLabel.setText("Compression Ratio: " + (double) compressionRatioSlider.getValue() / 10));

        add(compressionRatioSlider, BorderLayout.NORTH);
        add(compressionRatioLabel, BorderLayout.CENTER);
        add(textBeforeNameC, BorderLayout.SOUTH);
        add(namingConventionField, BorderLayout.SOUTH);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);
    }

    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            compressionRatio = (double) compressionRatioSlider.getValue() / 10;
            namingConvention = namingConventionField.getText();
            hideFrame();
        }
    }
    public double getCompressionRatio() {
        return compressionRatio;
    }
    public String getNamingConvention() {
        return namingConvention;
    }
    public void showFrame() {
        setVisible(true);
    }
    public void hideFrame() {
        setVisible(false);
        saveSettingsToFile();
        readFromFile();
    }
    private void saveSettingsToFile() {
        FileWriter writer = null;
        try {
            writer = new FileWriter("settings.txt");
            writer.write("Compression Ratio: " + compressionRatio + "\n");
            writer.write("Naming Convention: " + namingConventionField.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving settings to file", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error closing settings file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void readFromFile() {
        File file = new File("settings.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String compressionRatioLine = reader.readLine();
                String namingConventionLine = reader.readLine();

                compressionRatio = Double.parseDouble(compressionRatioLine.split(": ")[1]);
                namingConvention = namingConventionLine.split(": ")[1];

                compressionRatioSlider.setValue((int) compressionRatio * 10);
                namingConventionField.setText(namingConvention);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}