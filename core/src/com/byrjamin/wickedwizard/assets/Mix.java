package com.byrjamin.wickedwizard.assets;

/**
 * Created by Home on 03/06/2017.
 */

public class Mix {

    private String fileName;
    private float volume;
    private float pitch;

    public Mix(String fileName) {
        this.fileName = fileName;
    }

    public Mix(String fileName, float volume) {
        this.fileName = fileName;
        this.volume = volume;
    }

    public String getFileName() {
        return fileName;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
