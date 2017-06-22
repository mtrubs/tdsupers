package com.mtrubs.td.scene.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mtrubs.td.scene.TextureRegionActor;
import com.mtrubs.util.Nullable;

public class TowerMenuActor extends TextureRegionActor {

  private static final float OVERLAP = 5.0F;
  private static final String TEXT = "x"; // needed or else it does not render correctly

  @Nullable
  private final Label cost;
  @Nullable
  private final TextureRegion costRegion;

  /**
   * Creates an actor with the given texture region at the given x,y coordinates.
   *
   * @param positionX     the x coordinate of this actor.
   * @param positionY     the y coordinate of this actor.
   * @param textureRegion the texture of this actor.
   * @param costRegion    the cost info of this menu item
   */
  public TowerMenuActor(float positionX, float positionY,
                        TextureRegion textureRegion, @Nullable TextureRegion costRegion) {
    super(positionX, positionY, textureRegion);
    this.costRegion = costRegion;
    if (this.costRegion == null) {
      this.cost = null;
    } else {
      Label.LabelStyle style = new Label.LabelStyle();
      style.font = new BitmapFont();
      style.fontColor = Color.BLACK;
      this.cost = new Label(TEXT, style);
      this.cost.setX(getCenterX() - (this.cost.getPrefWidth() / 2.0F));
      this.cost.setY(getY() - this.costRegion.getRegionHeight() + OVERLAP);
    }
  }

  public void setTextureRegion(TextureRegion textureRegion) {
    super.setTextureRegion(textureRegion);
    if (this.costRegion != null && this.cost != null) {
      this.cost.setX(getCenterX() - (this.cost.getPrefWidth() / 2.0F));
      this.cost.setY(getY() - this.costRegion.getRegionHeight() + OVERLAP);
    }
  }

  public void setCost(int amount) {
    if (this.cost != null) {
      String sAmount = String.valueOf(amount);
      if (!sAmount.equals(this.cost.getText().toString())) {
        this.cost.setText(sAmount);
        this.cost.setX(getCenterX() - (this.cost.getPrefWidth() / 2.0F));
      }
    }
  }

  @Override
  public void setTouchable(Touchable touchable) {
    super.setTouchable(touchable);
    if (this.cost != null) {
      if (touchable == Touchable.enabled) {
        // TODO: un-gray out the texture region
        this.cost.getStyle().fontColor = Color.BLACK;
      } else if (touchable == Touchable.disabled) {
        // TODO: gray out the texture region
        this.cost.getStyle().fontColor = Color.RED;
      }
    }
  }

  @Override
  public void draw(Batch batch, float alpha) {
    super.draw(batch, alpha);
    if (this.costRegion != null) {
      float height = this.costRegion.getRegionHeight();
      batch.draw(this.costRegion, getX(), getY() - height + OVERLAP, getOriginX(), getOriginY(),
        this.costRegion.getRegionWidth(), height,
        getScaleX(), getScaleY(), getRotation());
    }
    if (this.cost != null) {
      this.cost.draw(batch, alpha);
    }
  }
}
