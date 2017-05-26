package com.mtrubs.td.scene;

import com.badlogic.gdx.math.Vector2;

/**
 * @author mrubino
 * @since 2015-02-19
 */
public interface Targetable {

    Vector2 getCenter();

    void damage(int amount);

    int getDamage();
}
