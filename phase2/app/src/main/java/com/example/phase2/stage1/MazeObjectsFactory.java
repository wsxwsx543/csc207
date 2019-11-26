package com.example.phase2.stage1;

import android.content.res.Resources;

public class MazeObjectsFactory {
    public static MazeObjects getMazeObject(String what, int curr_x, int curr_y, Resources res, String type){
        if("Treasure".equalsIgnoreCase(what)) return new Treasure(curr_x, curr_y, res, type);
        else if("Monster".equalsIgnoreCase(what)) return new Monster(curr_x, curr_y, res, type);
        else if("Door".equalsIgnoreCase(what)) return new Door(curr_x, curr_y, res, type);

        return null;
    }
}