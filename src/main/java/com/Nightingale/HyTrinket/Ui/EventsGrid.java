package com.Nightingale.HyTrinket.Ui;

import au.ellie.hyui.builders.PageBuilder;
import au.ellie.hyui.events.SlotClickingEventData;
import au.ellie.hyui.events.UIContext;
import com.Nightingale.HyTrinket.Api.TrinketRegistry;
import com.Nightingale.HyTrinket.Api.TrinketCallback;
import com.Nightingale.HyTrinket.components.TrinketComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.entity.entities.Player;

import java.util.List;
import java.util.Objects;

public class EventsGrid {

    public static void initEvent(PlayerRef playerRef, PageBuilder page) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return;

        Store<EntityStore> store = ref.getStore();
        store.assertThread();

        CombinedItemContainer inv = InventoryComponent.getCombined(
                store,
                ref,
                InventoryComponent.STORAGE_HOTBAR_BACKPACK
        );

        TrinketComponent trinkets = store.getComponent(
                ref,
                TrinketComponent.getTrinketsComponentType()
        );

        if (trinkets == null) return;

        SwapState state = new SwapState();
        bind(page, playerRef, "inventory-item-grid", true, inv, trinkets, state);

        if (trinkets.getSlots().isEmpty()) return;

        trinkets.getSlots().keySet().forEach(key ->
                bind(page, playerRef, key + "-item-grid", false, inv, trinkets, state)
        );
    }

    private static void bind(
            PageBuilder page,
            PlayerRef playerRef,
            String id,
            boolean isInventory,
            CombinedItemContainer inv,
            TrinketComponent trinkets,
            SwapState state
    ) {
        page.addEventListener(
                id,
                CustomUIEventBindingType.SlotClicking,
                SlotClickingEventData.class,
                (click, context) -> {
                    int slot = click.getSlotIndex();

                    if (isInventory) {
                        state.inventoryItemSlot = slot;
                        state.inventoryItem = inv.getItemStack((short) slot);
                    } else {
                        state.gridId = id.replace("-item-grid", "");
                        state.gridSlot = slot;
                        state.gridItem = Objects.requireNonNull(trinkets.getSlot(state.gridId)).getItem(slot);
                    }
                    if (!ready(state)) return;

                    handleSwap(playerRef, state, inv, trinkets);

                    UpdatePage(state, playerRef, page, context);
                }
        );
    }

    private static void handleSwap(
            PlayerRef playerRef,
            SwapState s,
            CombinedItemContainer inv,
            TrinketComponent trinkets
    ) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        store.assertThread();

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return;

        ItemStack invItem = s.inventoryItem;
        ItemStack gridItem = s.gridItem;


        if (invItem != null) {
            List<String> allowed = TrinketRegistry.SlotAllowedItems.get(s.gridId);
            if (allowed == null || !allowed.contains(invItem.getItemId())) return;
        }

        boolean equipResult = runCallback(store, ref, playerRef, gridItem, false);
        boolean unequipResult = runCallback(store, ref, playerRef, gridItem, true);

//        HyTrinketPlugin.Log("equip result = " + String.valueOf(equipResult));
//        HyTrinketPlugin.Log("unequipResult result = " + String.valueOf(unequipResult));

        if (equipResult && unequipResult) {
            inv.setItemStackForSlot((short) (int) s.inventoryItemSlot, gridItem == null ? ItemStack.EMPTY : gridItem);
            Objects.requireNonNull(trinkets.getSlot(s.gridId)).setItem((short) (int) s.gridSlot, invItem);
        }
    }

    private static boolean runCallback(
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef,
            ItemStack item, boolean equip
    ) {
        boolean result = true;
        if (item == null) return true;
        Class<? extends TrinketCallback> clazz =
                TrinketRegistry.TrinketBehaviors.get(item.getItemId());

        TrinketCallback callback = create(clazz);

        if (callback == null) return true;

        if (equip) result = callback.equip(store, ref, playerRef);
        else result = callback.unequip(store, ref, playerRef);
        return result;
    }

    private static TrinketCallback create(Class<? extends TrinketCallback> clazz) {
        if (clazz == null) return null;
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private static void UpdatePage(
            SwapState state,
            PlayerRef playerRef,
            PageBuilder page,
            UIContext context
    ) {
        reset(state);
        MainPage.UpdateInventory(playerRef, page);
        MainPage.UpdateTrinkets(playerRef, page);
        context.updatePage(true);
    }

    private static boolean ready(SwapState s) {
//        HyTrinketPlugin.Log(s.inventoryItemSlot + s.gridSlot + s.gridId);
        return s.inventoryItemSlot != null && s.gridSlot != null && s.gridId != null;
    }

    private static void reset(SwapState s) {
        s.inventoryItem = null;
        s.inventoryItemSlot = null;
        s.gridSlot = null;
        s.gridId = null;
        s.gridItem = null;
    }
}

class SwapState {
    Integer inventoryItemSlot;
    ItemStack inventoryItem;
    Integer gridSlot;
    String gridId;
    ItemStack gridItem;
}