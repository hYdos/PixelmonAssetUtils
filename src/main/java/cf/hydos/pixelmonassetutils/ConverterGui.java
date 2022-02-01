package cf.hydos.pixelmonassetutils;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConverterGui {
    private static final String GLB_EXTENSION = ".glb";
    private static final String PIXEL_ASSET_EXTENSION = ".pk";
    private static final Path INPUT_FOLDER = Paths.get("input");
    private static final Path OUTPUT_FOLDER = Paths.get("output");
    public JPanel root;
    private JProgressBar progress;
    private JButton convertGlbToPkButton;
    private JLabel status;
    private final boolean bulkConversion = false; // TODO: experimental. May result in smaller sizes but can be a pain to deal with.

    public ConverterGui() {
        try {
            Files.createDirectories(INPUT_FOLDER);
            Files.createDirectories(OUTPUT_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create in/out folders.", e);
        }

        this.convertGlbToPkButton.addActionListener(e -> this.convertFiles());
    }

    private void convertFiles() {
        updateStatus("Starting");
        try {
            List<String> inputFiles = Files.find(INPUT_FOLDER, 24, (path, basicFileAttributes) -> path.toString().endsWith(GLB_EXTENSION)).map(path -> path.toString().replace("input/", "")).toList();
            new SwingWorker<String, String>() {

                @Override
                protected String doInBackground() {
                    if (bulkConversion) {
                        PixelConverter.bulkConvertToPk(INPUT_FOLDER, OUTPUT_FOLDER.resolve("assets.pk"));
                    } else {
                        for (int i = 0; i < inputFiles.size(); i++) {
                            String originalInputFile = inputFiles.get(i);
                            String inputFile = originalInputFile.replace(GLB_EXTENSION, "");
                            Path inPath = Paths.get(inputFile + GLB_EXTENSION);
                            Path outPath = Paths.get(inputFile.replace("input", "output") + PIXEL_ASSET_EXTENSION);

                            System.out.println("Processing file " + inPath);
                            updateStatus("Processing " + inputFile);
                            progress.setValue(i / inputFiles.size() * 100);

                            PixelConverter.convertToPk(inPath, outPath);
                        }
                    }

                    updateStatus("Finished!");
                    return "";
                }
            }.execute();
        } catch (IOException e) {
            throw new RuntimeException("Failed to loop over input files.", e);
        }
    }

    public void updateStatus(String status) {
        this.status.setText("Status: " + status);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        root.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        convertGlbToPkButton = new JButton();
        convertGlbToPkButton.setText("Convert .glb to .pk");
        panel1.add(convertGlbToPkButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Convert .glb files into Pixel Asset (.pk) Files");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        status = new JLabel();
        status.setText("Status: Waiting");
        panel2.add(status, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        progress = new JProgressBar();
        progress.setOrientation(0);
        progress.setStringPainted(true);
        progress.setValue(0);
        panel2.add(progress, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
