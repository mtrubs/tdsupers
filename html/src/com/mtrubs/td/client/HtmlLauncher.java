package com.mtrubs.td.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mtrubs.td.GdxTd;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GdxTd();
    }
}