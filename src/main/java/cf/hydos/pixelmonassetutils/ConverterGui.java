package cf.hydos.pixelmonassetutils;

import javax.swing.*;
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
    private JButton convertGlbToPkButton;
    private JLabel status;

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
            List<String> inputFiles = Files.find(INPUT_FOLDER, 24, (path, basicFileAttributes) -> path.toString().endsWith(GLB_EXTENSION)).map(path -> path.toString().replace(GLB_EXTENSION, "").replace("input/", "")).toList();
            new SwingWorker<String, String>() {

                @Override
                protected String doInBackground() {
                    for (String inputFile : inputFiles) {
                        Path inPath = INPUT_FOLDER.resolve(inputFile + GLB_EXTENSION);
                        Path outPath = OUTPUT_FOLDER.resolve(inputFile + PIXEL_ASSET_EXTENSION);
                        System.out.println("Processing file " + inPath);
                        updateStatus("Processing " + inputFile);
                        PixelConverter.convertToPk(inPath, outPath);
                        System.out.println("Finished!");
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
}
