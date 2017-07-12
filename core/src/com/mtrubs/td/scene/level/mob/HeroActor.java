package com.mtrubs.td.scene.level.mob;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mtrubs.td.graphics.level.HeroUnit;

public class HeroActor extends PcActor<HeroUnit> {

  public HeroActor(HeroUnit type, TextureRegion textureRegion, float startX, float startY) {
    super(startX, startY, textureRegion, type);
    setHitPoints(type.getHealth());
    setVisible(false);

    addListener(new ClickListener() {

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        return true; // to intercept for the touch up event
      }

      @Override
      public void touchUp(InputEvent event, float xDelta, float yDelta, int pointer, int button) {
        super.touchUp(event, xDelta, yDelta, pointer, button);
        if (!inTapSquare()) {
          // as long as we get the touchUp and we are outside the tap square then we move
          moveTo(event);
        }
      }

      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        getStage().setSelected(HeroActor.this);
      }
    });
  }

  @Override
  protected boolean hasUnit() {
    return true;
  }
}
