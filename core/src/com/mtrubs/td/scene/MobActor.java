package com.mtrubs.td.scene;

import aurelienribon.tweenengine.Timeline;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mtrubs.td.graphics.Mob;
import com.mtrubs.td.graphics.ProjectileType;

/**
 * @author mrubino
 * @since 2015-02-12
 */
public class MobActor extends CombatActor {

    private Timeline timeline;
    private Mob type;

    /**
     * Creates an actor with the given texture region at the given x,y coordinates.
     *
     * @param positionX     the x coordinate of this actor.
     * @param positionY     the y coordinate of this actor.
     * @param textureRegion the texture of this actor.
     */
    public MobActor(float positionX, float positionY, Mob type, float scale, TextureRegion textureRegion) {
        super(positionX, positionY, textureRegion);
        setHitPoints((int) ((float) type.getHealth() * scale));
        this.type = type;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public float getProgress() {
        return this.timeline.getCurrentTime();
    }

    @Override
    protected void handleNoTarget(float delta) {
        super.handleNoTarget(delta);
        // otherwise if we are not moving we continue moving again
        startMoving();
    }

    @Override
    protected void handleTarget(float delta) {
        super.handleTarget(delta);
        // if we have a target we stop moving and attack it
        stopMoving();
        attackTarget(delta);
    }

    @Override
    protected float getAttackCoolDown() {
        return this.type.getAttackCoolDown();
    }

    @Override
    protected ProjectileType getProjectileType() {
        return this.type.getProjectileType();
    }

    @Override
    protected boolean canAttack() {
        return this.type != null;
    }

    @Override
    protected void handleDefeat() {
        ((LevelStage) getStage()).remove(this);
    }

    private void stopMoving() {
        if (!this.timeline.isPaused()) {
            this.timeline.pause();
        }
    }

    private void startMoving() {
        if (this.timeline.isPaused()) {
            this.timeline.resume();
        }
    }

    @Override
    protected Targetable checkForTarget() {
        LevelStage stage = (LevelStage) getStage();
        // mobs will only attack the unit if it is attacking them
        // TODO: change this for different types of mobs
        for (TowerGroup tower : stage.getTowers()) {
            if (tower.getTarget() == this) {
                UnitActor unit = tower.getUnit();
                if (isInRange(unit)) {
                    return unit;
                }
            }
        }
        return super.checkForTarget();
    }

    @Override
    protected float getRange() {
        return this.type.getRange();
    }

    @Override
    public int getDamage() {
        return this.type.getDamage();
    }
}
