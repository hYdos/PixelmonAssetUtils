package cf.hydos.pixelmonassetutils.reader;

import cf.hydos.pixelmonassetutils.scene.Scene;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarFile;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.io.IOException;
import java.nio.ByteBuffer;

public class GlbReader implements FileReader {

    @Override
    public Scene read(TarFile file) throws IOException {
        AIScene aiScene = null;
        for (TarArchiveEntry entry : file.getEntries()) {
            if (entry.getName().endsWith(".glb")) {
                byte[] bytes = file.getInputStream(entry).readAllBytes();
                ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
                buffer.put(bytes).flip();

                aiScene = Assimp.aiImportFileFromMemory(buffer, Assimp.aiProcess_Triangulate, "glb");
            }
        }

        if (aiScene == null) {
            throw new RuntimeException("Unable to locate .glb file to load! Reason: " + Assimp.aiGetErrorString());
        }

        return AssimpReader.read(aiScene);
    }
}
