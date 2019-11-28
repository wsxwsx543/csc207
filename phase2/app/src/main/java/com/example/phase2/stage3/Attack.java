package com.example.phase2.stage3;

import com.example.phase2.appcore.game.Property;

public class Attack implements Strategy {
    @Override
    public Property doMove(Property property) {
        property.addPropertyToSelf(10,0,0,0);
        return property;
    }
}