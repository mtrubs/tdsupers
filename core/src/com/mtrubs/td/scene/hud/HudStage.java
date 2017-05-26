package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mtrubs.td.graphics.HeadsUpDisplay;
import com.mtrubs.td.graphics.TextureRegionManager;
import com.mtrubs.td.scene.TextureRegionActor;

/**
 * @author mrubino
 * @since 2015-02-21
 */
public class HudStage extends Stage {


    public HudStage(float worldWidth, float worldHeight, TextureRegionManager textureRegionManager) {
        super(new ExtendViewport(worldWidth, worldHeight));

        // TODO: player health - top left
        // TODO: player currency - next to health
        // TODO: hero icons - bottom left
        // TODO: hero specials - bottom right
        // TODO: wave status - under health
        // TODO: pause - top right
        // TODO: fast forward - next to pause
        TextureRegion currencyTexture = textureRegionManager.get(HeadsUpDisplay.Currency);
        TextureRegionActor currency = new TextureRegionActor(5, getHeight() - currencyTexture.getRegionHeight() - 5, currencyTexture);

        Table topLeft = new Table();
        topLeft.add(currency);
        topLeft.setPosition(50.0F, 50.0F);

        addActor(topLeft);
    }
}
