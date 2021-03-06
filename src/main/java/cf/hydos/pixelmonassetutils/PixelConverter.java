package cf.hydos.pixelmonassetutils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility for writing and reading Pixelmon: Generation's model format.
 */
public class PixelConverter {

    private static final LZMA2Options options = new LZMA2Options();

    public static void convertToPk(Path glbFile, Path output) {
        try {
            if (!Files.exists(output)) {
                Files.createDirectories(output.getParent());
                Files.createFile(output);
            }

            try (OutputStream xz = new XZOutputStream(Files.newOutputStream(output), options)) {
                try (TarArchiveOutputStream tar = new TarArchiveOutputStream(xz)) {
                    tar.putArchiveEntry(new TarArchiveEntry(glbFile, glbFile.getFileName().toString()));
                    IOUtils.copy(new BufferedInputStream(Files.newInputStream(glbFile)), tar);
                    tar.closeArchiveEntry();
                }
            }

            byte[] lockedBytes = PixelAsset.lockArchive(Files.newInputStream(output).readAllBytes());
            try (OutputStream out = Files.newOutputStream(output)) {
                out.write(lockedBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pixel Asset Utils");
        frame.setContentPane(new ConverterGui().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
