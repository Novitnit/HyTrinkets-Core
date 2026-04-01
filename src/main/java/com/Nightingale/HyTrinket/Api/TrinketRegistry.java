package com.Nightingale.HyTrinket.Api;

import com.Nightingale.HyTrinket.HyTrinketPlugin;

import java.util.*;

public class TrinketRegistry {

    public static final Map<String, List<String>> SlotAllowedItems = new HashMap<String, List<String>>();
    public static final Map<String, Class<? extends TrinketCallback>> TrinketBehaviors = new HashMap<>();
    public static final Map<String, List<String>> WaitForRegisterItemList = new HashMap<>();
    public static final Map<String, Integer> WaitForRegisterSlot = new HashMap<>();

    public static void RegisterSlot(String name, Integer size) {
        WaitForRegisterSlot.put(name, size);
    }

    public static void RegisterItem(String SlotName, String ItemId) {
        HyTrinketPlugin.Log("registering " + SlotName + " " + ItemId);
        if (SlotAllowedItems.containsKey(SlotName)) {
            HyTrinketPlugin.Log("Registering item" + ItemId + " for " + SlotName);
            List<String> list = SlotAllowedItems.get(SlotName);
            list.add(ItemId);
        } else {
            HyTrinketPlugin.Log("wait registering item" + ItemId + " for " + SlotName);
            WaitForRegisterItemList.computeIfAbsent(SlotName, k -> new ArrayList<>()).add(ItemId);
        }
    }

    public static void RegisterItem(String SlotName, String ItemId, Class<? extends TrinketCallback> callbackClass) {
        HyTrinketPlugin.Log("registering Callback" + SlotName + " " + ItemId);
        if (SlotAllowedItems.containsKey(SlotName)) {
            HyTrinketPlugin.Log("Registering item" + ItemId + " for " + SlotName);
            List<String> list = SlotAllowedItems.get(SlotName);
            list.add(ItemId);
        } else {
            WaitForRegisterItemList.computeIfAbsent(SlotName, k -> new ArrayList<>()).add(ItemId);
        }
        TrinketBehaviors.put(ItemId, callbackClass);
    }

    public static void RegisterAll() {
        HyTrinketPlugin.Log("Registering all Trinkets");

        WaitForRegisterSlot.forEach((name, size) -> {
            HyTrinketPlugin.Log("Registering " + name);
            HyTrinketPlugin.get().getConfig().addSlot(name, size);
            SlotAllowedItems.computeIfAbsent(name, k -> new ArrayList<>());
            HyTrinketPlugin.get().saveConfig();
        });


        WaitForRegisterItemList.forEach((s, itemIds) -> {
            HyTrinketPlugin.Log("Registering " + s +  " for " + itemIds);
            itemIds.forEach(itemId -> {
                HyTrinketPlugin.Log("Registering item" + itemId + " for " + s);
                List<String> list = SlotAllowedItems.get(s);
                if (list == null) {
                    throw new IllegalArgumentException("Slot not found: " + s);
                }
                list.add(itemId);
            });
        });

        HyTrinketPlugin.Log("Successfully registered all Trinkets");
    }
}
