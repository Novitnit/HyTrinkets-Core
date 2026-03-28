package com.Nightingale.Api;

import com.Nightingale.HyTrinketPlugin;

import java.util.HashSet;
import java.util.Set;

public class RegisterTrinkets {

    public static final Set<String> TrinketsSlotsType =  new HashSet<String>();

    public static void RegisterSlot(String name, Integer size) {
        HyTrinketPlugin.get().getConfig().addSlot(name, size);
        TrinketsSlotsType.add(name);
        HyTrinketPlugin.get().saveConfig();
    }
}
