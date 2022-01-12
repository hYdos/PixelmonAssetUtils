package cf.hydos.pixelconverter;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

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

    public PixelConverter() {
        PixelAsset asset = new PixelAsset(Path.of("/home/cope/tmp/formatComparison2/work-out/venusaur-all.pk"));

        Path glbFile = Path.of("/home/cope/tmp/formatComparison2/work-out/venusaur-all.glb");
        Path outFile = Path.of("/home/cope/tmp/formatComparison2/work-out/venusaur-all.pk");

        try {
            if (!Files.exists(outFile)) {
                Files.createFile(outFile);
            }

            try (OutputStream xz = new XZOutputStream(Files.newOutputStream(outFile), options)) {
                try (TarArchiveOutputStream tar = new TarArchiveOutputStream(xz)) {
                    tar.putArchiveEntry(new TarArchiveEntry(glbFile, glbFile.getFileName().toString()));
                    IOUtils.copy(new BufferedInputStream(Files.newInputStream(glbFile)), tar);
                    tar.closeArchiveEntry();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PixelConverter();
    }
}
