package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author mrubino
 * @since 2015-01-26
 */
public interface TextureRegionManager {

    /**
     * Gets the texture region associated with the given reference.
     *
     * @param type the reference to the texture we want to load.
     * @return the loaded texture region of the given reference.
     */
    TextureRegion get(TextureReference type);

    /**
     * Disposes of this manager and all the textures that it has loaded.
     */
    void dispose();
}
