package com.Nightingale.Ui;

import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.events.SlotClickingEventData;
import com.Nightingale.HyTrinketPlugin;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class EventsGrid {
    public  static void initEvent(PlayerRef playerRef, PageBuilder page){
        page.addEventListener("inventory-item-grid", CustomUIEventBindingType.SlotClicking, SlotClickingEventData.class,(drag, event)->{
            Integer slotIndex = drag.getSlotIndex();
            HyTrinketPlugin.Log("slotIndex: " + slotIndex);
        });
    }
}
