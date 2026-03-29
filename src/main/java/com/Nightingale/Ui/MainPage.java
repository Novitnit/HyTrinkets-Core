package com.Nightingale.Ui;

import au.ellie.hyui.builders.*;
import au.ellie.hyui.types.ItemGridStyle;
import au.ellie.hyui.types.LayoutMode;
import com.Nightingale.components.TrinketComponent;
import com.Nightingale.components.TrinketSlot;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.ui.ItemGridSlot;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPage {

    private static final String SLOT_BACKGROUND = "Common/BlockSelectorSlotBackground.png";
    private static final int UI_WIDTH = 750;
    private static final int UI_HEIGHT = 470;

    private static final ItemGridStyle GRID_STYLE =
            new ItemGridStyle()
                    .withSlotBackground(new HyUIPatchStyle()
                            .setTexturePath(SLOT_BACKGROUND))
                    .withSlotSpacing(4)
                    .withSlotSize(60)
                    .withSlotIconSize(60);

    public static PageBuilder Main(PlayerRef player) {
        Ref<EntityStore> ref = player.getReference();
        if (ref == null) {
            return PageBuilder.pageForPlayer(player);
        }

        Store<EntityStore> store = ref.getStore();
        store.assertThread();

        TrinketComponent trinkets = store.getComponent(
                ref,
                TrinketComponent.getTrinketsComponentType()
        );

        GroupBuilder mainGroup =
                new GroupBuilder().withLayoutMode(LayoutMode.Top)
                        .addChild(new ContainerBuilder().withId("trinket-slot").withTitleText("Trinket Slot")
                                .withFlexWeight(1)
                                .withAnchor(new HyUIAnchor().setHeight(UI_HEIGHT).setWidth(UI_WIDTH))
                                .withPadding(new HyUIPadding().setBottom(16))
                                .addChild(
                                        new GroupBuilder()
                                                .withLayoutMode(LayoutMode.Left)
                                                .addChild(setUpTrinkets(trinkets))
                                                .addChild(
                                                        new GroupBuilder().withId("trinket-info-content")
                                                                .withLayoutMode(LayoutMode.Top)
                                                                .withFlexWeight(1)
                                                                .withAnchor(new HyUIAnchor().setWidth(250))
                                                )
                                )
                        )
                        .addChild(new ContainerBuilder().withId("inventory-slot").withTitleText("Inventory")
                                .withFlexWeight(1)
                                .withAnchor(new HyUIAnchor().setHeight(UI_HEIGHT).setWidth(UI_WIDTH))
                                .addChild(
                                        new GroupBuilder().withId("inventory-content")
                                                .addChild(
                                                        new ItemGridBuilder()
                                                                .withId("inventory-item-grid")
                                                                .withInventorySectionId(950)
                                                                .withStyle(
                                                                        new ItemGridStyle()
                                                                                .withSlotBackground(new HyUIPatchStyle().setTexturePath(SLOT_BACKGROUND))
                                                                                .withSlotSpacing(4)
                                                                                .withSlotSize(76)
                                                                                .withSlotIconSize(76)
                                                                )
                                                                .withSlotsPerRow(9)
                                                                .withAreItemsDraggable(false)
                                                )
                                )
                        );

        return PageBuilder.pageForPlayer(player)
                .addElement(
                        new PageOverlayBuilder().withLayoutMode(LayoutMode.MiddleCenter)
                                .addChild(mainGroup)
                );
    }

    public static void UpdateInventory(PlayerRef playerRef, PageBuilder page) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return;

        Store<EntityStore> store = ref.getStore();
        store.assertThread();

        CombinedItemContainer inv = InventoryComponent.getCombined(
                store,
                ref,
                InventoryComponent.STORAGE_HOTBAR_BACKPACK
        );

        page.getById("inventory-item-grid", ItemGridBuilder.class).ifPresent(grid -> {
            int capacity = inv.getCapacity();
            if (capacity <= 0) return;

            ItemGridSlot[] inventorySlots = new ItemGridSlot[capacity];

            for(short i = 0; i < capacity; i++) {
                ItemStack stack = inv.getItemStack(i);
                if (stack == null || stack.isEmpty()) {
                    inventorySlots[i] = new ItemGridSlot();
                    inventorySlots[i].setActivatable(true);
                } else {
                    inventorySlots[i] = new ItemGridSlot(stack);
                    inventorySlots[i].setActivatable(true);
                }
            }

            grid.withSlots(List.of(inventorySlots));
        });
    }

    public static void UpdateTrinkets(PlayerRef playerRef, PageBuilder page) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null) return;
        Store<EntityStore> store = ref.getStore();
        store.assertThread();
        TrinketComponent trinkets = store.getComponent(ref, TrinketComponent.getTrinketsComponentType());
        if (trinkets == null) return;

        for (Map.Entry<String, TrinketSlot> entry : trinkets.getSlots().entrySet()) {
            String slotName = entry.getKey();
            TrinketSlot slot = entry.getValue();
            String gridIdName = slotName.concat("-item-grid");

            int capacity = slot.getItems().length;
            if (capacity <= 0) return;

            List<ItemGridSlot> slots = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) {
                ItemStack stack = slot.getItem(i);
                ItemGridSlot Slot = new ItemGridSlot();
                if (stack != null && !stack.isEmpty()) {
                    Slot.setItemStack(stack);
                }
                Slot.setActivatable(true);
                slots.add(Slot);
            }

            page.getById(gridIdName, ItemGridBuilder.class).ifPresent(grid -> {
                grid.withSlots(slots);
            });
        }
    }

    public static GroupBuilder setUpTrinkets(TrinketComponent trinkets) {
        if (trinkets == null) return new GroupBuilder();

        GroupBuilder root = new GroupBuilder()
                .withId("trinket-content")
                .withLayoutMode(LayoutMode.Top)
                .withFlexWeight(1);

        int gridId = 9950;

        for (Map.Entry<String, TrinketSlot> entry : trinkets.getSlots().entrySet()) {
            String slotName = entry.getKey();
            TrinketSlot slot = entry.getValue();

            String gridIdName = slotName.concat("-item-grid");

            root.addChild(
                    new GroupBuilder()
                            .withId(slotName + "-group")
                            .withLayoutMode(LayoutMode.Left)
                            .withAnchor(new HyUIAnchor().setHeight(70))
                            .withPadding(new HyUIPadding().setBottom(6))
                            .addChild(
                                    new LabelBuilder()
                                            .withText(slotName)
                                            .withStyle(new HyUIStyle().setFontSize(20))
                                            .withPadding(new HyUIPadding().setRight(10))
                            )
                            .addChild(
                                    new ItemGridBuilder()
                                            .withId(gridIdName)
                                            .withInventorySectionId(gridId++)
                                            .withSlots(buildSlots(slot.getItems()))
                                            .withSlotsPerRow(9)
                                            .withStyle(GRID_STYLE)
                                            .withAreItemsDraggable(false)
                            )
            );
        }

        return root;
    }

    private static List<ItemGridSlot> buildSlots(ItemStack[] items) {
        List<ItemGridSlot> slots = new ArrayList<>(items.length);

        for (ItemStack item : items) {
            ItemGridSlot slot = new ItemGridSlot();
            if (item != null) {
                slot.setItemStack(item);
            }
            slot.setActivatable(true);
            slots.add(slot);
        }

        return slots;
    }
}