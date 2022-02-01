package cf.hydos.pixelmonassetutils.scene;

import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.util.List;

public class Scene {

    public final List<SceneObject> objects;

    public Scene(List<SceneObject> objects) {
        this.objects = objects;
    }

    public void exportAssimpScene(String path) {

        AIScene scene = AIScene.create();
        scene.mMeshes(null);
    }
}
