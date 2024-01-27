package com.mbrlabs.mundus.commons.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import net.mgsx.gltf.scene3d.shaders.PBRDepthShader;

public class MundusPBRDepthShader extends PBRDepthShader {

    private final boolean instanced;

    public MundusPBRDepthShader(final Renderable renderable, final Config config, final String prefix) {
        super(renderable, config, prefix);
        instanced = renderable.meshPart.mesh.isInstanced();
    }

    @Override
    public boolean canRender(final Renderable renderable) {
        return renderable.meshPart.mesh.isInstanced() == instanced && super.canRender(renderable);
    }
}
