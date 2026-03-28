package com.Nightingale.Ui;

import au.ellie.hyui.builders.*;
import au.ellie.hyui.types.ItemGridStyle;
import au.ellie.hyui.types.LayoutMode;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.CombinedItemContainer;
import com.hypixel.hytale.server.core.ui.ItemGridSlot;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;
import java.util.Optional;

public class MainPage {
    public static PageBuilder Main(PlayerRef player) {
        GroupBuilder mainGroup =
                new GroupBuilder().withLayoutMode(LayoutMode.Top)
                        .addChild(new ContainerBuilder().withId("trinket-slot").withTitleText("Trinket Slot")
                                .withFlexWeight(1)
                                .withAnchor(new HyUIAnchor().setHeight(450).setWidth(750))
                                .withPadding(new HyUIPadding().setBottom(16))
                                .addChild(
                                        new GroupBuilder().withId("trinket-content")
                                )
                        )
                        .addChild(new ContainerBuilder().withId("inventory-slot").withTitleText("Inventory")
                                .withFlexWeight(1)
                                .withAnchor(new HyUIAnchor().setHeight(470).setWidth(750))
                                .addChild(
                                        new GroupBuilder().withId("inventory-content")
                                                .addChild(
                                                        new ItemGridBuilder()
                                                                .withId("inventory-item-grid")
                                                                .withInventorySectionId(950)
                                                                .withStyle(
                                                                        new ItemGridStyle()
                                                                                .withSlotBackground(new HyUIPatchStyle().setTexturePath("Common/BlockSelectorSlotBackground.png"))
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


        Optional<ItemGridBuilder> grid = page.getById("inventory-item-grid", ItemGridBuilder.class);
        grid.ifPresent(gridBuilder -> {
            short capacity = inv.getCapacity();
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
            gridBuilder
                    .withSlots(List.of(inventorySlots));
        });
    }
}
