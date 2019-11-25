package com.example.phase2.AppCoreClasses;

import android.app.Application;

/** An app store the data that used by all activities. */
public class GameApp extends Application {

    /**
     * The color theme of this app.
     */
    private String colorTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        colorTheme = "pink";
    }

    /**
     * Get current color theme.
     * @return current color theme.
     */
    public String getColorTheme() {
        return colorTheme;
    }

    /**
     * Set the color theme to a specific corlor.
     * @param colorTheme the color you want to choose.
     */
    public void setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
    }
}
