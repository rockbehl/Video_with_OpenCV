import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileChooserUI extends JFrame {

    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private ArrayList<File> files;
    private final SettingsFrame settFrame = new SettingsFrame();

    public static void main(String[] args){

        // Load the OpenCV library
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        System.out.println("lib load/");

        FileChooserUI ui = new FileChooserUI();
        ui.setVisible(true);
    }

    public FileChooserUI() {
        super("File Chooser UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245,245,220)); //Beige color

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245,245,220));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245,245,220));
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(252, 252, 220));
        listPanel.setFont(new Font("Arial", Font.ITALIC, 14));

        settFrame.readFromFile();

        //open file chooser
        JButton openButton = new JButton("Open");
        openButton.setPreferredSize(new Dimension(120, 50));
        openButton.setFont(new Font("Arial", Font.BOLD, 20));
        openButton.addActionListener(e -> {
            //file chooser setup
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("MOV File","mov");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(true);
            //record selected files to arraylist
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                ArrayList<File> tempSelection = new ArrayList<>();
                tempSelection.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
                files.addAll(tempSelection);
                for (File f: tempSelection) {
                    listModel.addElement(f.getName());
                }
            }
        });
        buttonPanel.add(openButton);

        JButton settingsButton = new JButton("Settings");
        settingsButton.setPreferredSize(new Dimension(120, 50));
        settingsButton.setFont(new Font("Arial", Font.BOLD, 20));
        settingsButton.addActionListener(e -> {
            settFrame.showFrame();
        });
        buttonPanel.add(settingsButton);


        JButton cutButton = new JButton("Cut");
        cutButton.setPreferredSize(new Dimension(120, 50));
        cutButton.setFont(new Font("Arial", Font.BOLD, 20));
        cutButton.addActionListener(e -> {

            int[] selectedIndices = fileList.getSelectedIndices();
            ArrayList<File> selectedFiles = new ArrayList<>();
            for (int index : selectedIndices) {
                selectedFiles.add(files.get(index));
            }

            files.retainAll(selectedFiles);

            for (File f: files) {
                // Open a dialog box to get the cut string
                String cutString = JOptionPane.showInputDialog(null, "Enter timecodes ('start:Time-end:Time, Repeat for multiple')");

                cutString = cutString.replaceAll("\\s", "");
                String[] timingSplitCommas = cutString.split(",");
                ArrayList<String> timingSplitHyphen = new ArrayList<>();

                for (String s: timingSplitCommas) {
                    String[] temp = s.split("-");
                    timingSplitHyphen.addAll(Arrays.asList(temp));
                }

                new ocvInterface().cutInputHandeler(f, convertToSeconds(timingSplitHyphen.get(0)), convertToSeconds(timingSplitHyphen.get(1)), settFrame.getNamingConvention());
                timingSplitHyphen.remove(0);
                timingSplitHyphen.remove(0);
            }

            System.out.println("Cut: " + selectedFiles.get(0));
            System.out.println("at: ");
        });
        buttonPanel.add(cutButton);

            //to compress selected files
        JButton compressButton = new JButton("Compress");
        compressButton.setPreferredSize(new Dimension(120, 50));
        compressButton.setFont(new Font("Arial", Font.BOLD, 20));
        compressButton.addActionListener(e -> {
            try {
                // Get the selected files from the list
                int[] selectedIndices = fileList.getSelectedIndices();
                ArrayList<File> selectedFiles = new ArrayList<>();
                for (int index : selectedIndices) {
                    selectedFiles.add(files.get(index));
                }

                files.retainAll(selectedFiles);
                int pos = 0;
                for (File f: files) {
                    new ocvInterface().compressInputHandeler(f, pos, settFrame.getCompressionRatio(), settFrame.getNamingConvention());
                    pos++;
                }

                listModel.clear();

            } catch (Exception exemp){
                System.out.println("no files selected");
            }

        });
        buttonPanel.add(compressButton);


        files = new ArrayList<File>();
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        fileList.setSize(500,500);
        JScrollPane scrollPane = new JScrollPane(fileList);
        listPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public double convertToSeconds(String time) {
        // Split the string by ":"
        String[] parts = time.split(":");
        double minutes = Double.parseDouble(parts[0]);
        double seconds = Double.parseDouble(parts[1]);
        // convert minutes to seconds
        double totalSeconds = minutes * 60 + seconds;
        return totalSeconds;
    }

}

