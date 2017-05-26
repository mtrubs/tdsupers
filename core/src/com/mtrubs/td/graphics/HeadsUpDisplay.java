package com.mtrubs.td.graphics;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author mrubino
 * @since 2015-02-21
 */
public enum HeadsUpDisplay implements TextureReference {

    Currency("images/hud/influence.png"),
    Health("images/hud/health.png"),
    FastForward("images/hud/fastForward.png"),
    Pause("images/hud/pause.png");

    private final String texturePath;
    private final String key;

    private HeadsUpDisplay(String texturePath) {
        this.texturePath = texturePath;
        this.key = String.format("%s.%s", getClass().getSimpleName(), name());
    }

    @Override
    public String getTexturePath() {
        return this.texturePath;
    }

    @Override
    public void setTextureFilter(Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public String getKey() {
        return this.key;
    }
}