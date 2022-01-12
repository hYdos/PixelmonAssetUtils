package cf.hydos.pixelconverter.scene;

import cf.hydos.pixelconverter.reader.GlbReader;
import cf.hydos.pixelconverter.scene.material.Material;

public class SceneObject {

    public final String name;
    public final GlbReader.MeshData mesh;
    public final Material material;

    public SceneObject(String name, GlbReader.MeshData mesh, Material material) {
        this.name = name;
        this.mesh = mesh;
        this.material = material;
    }
}
