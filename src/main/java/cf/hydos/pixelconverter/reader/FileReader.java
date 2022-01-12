package cf.hydos.pixelconverter.reader;

import cf.hydos.pixelconverter.scene.Scene;
import org.apache.commons.compress.archivers.tar.TarFile;

import java.io.IOException;

public interface FileReader {

    Scene read(TarFile file) throws IOException;
}
