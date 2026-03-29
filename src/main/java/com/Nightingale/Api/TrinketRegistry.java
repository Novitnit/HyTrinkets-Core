package com.Nightingale.Api;

import com.Nightingale.HyTrinketPlugin;

import java.util.*;

public class TrinketRegistry {

    public static final Map<String, List<String>> SlotAllowedItems = new HashMap<String, List<String>>();
    public static final Map<String, Class<? extends TrinketCallback>> TrinketBehaviors = new HashMap<>();

    public static void RegisterSlot(String name, Integer size) {
        HyTrinketPlugin.get().getConfig().addSlot(name, size);
        SlotAllowedItems.put(name, new ArrayList<String>());
        HyTrinketPlugin.get().saveConfig();
    }

    public static void RegisterItem(String SlotName, String ItemId) {
        List<String> list = SlotAllowedItems.get(SlotName);
        if (list == null) {
            throw new IllegalArgumentException("Slot not found: " + SlotName);
        }
        list.add(ItemId);
    }

    public static void RegisterItem(String SlotName, String ItemId, Class<? extends TrinketCallback> callbackClass) {
        List<String> list = SlotAllowedItems.get(SlotName);
        if (list == null) {
            throw new IllegalArgumentException("Slot not found: " + SlotName);
        }
        TrinketBehaviors.put(ItemId, callbackClass);
        list.add(ItemId);
    }
}
