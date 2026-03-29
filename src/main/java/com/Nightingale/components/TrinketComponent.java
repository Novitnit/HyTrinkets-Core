package com.Nightingale.components;

import com.Nightingale.HyTrinketPlugin;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.*;

public class TrinketComponent implements Component<EntityStore> {

    public static final BuilderCodec<TrinketComponent> CODEC;
    static Codec<TrinketSlot[]> SlotsCodec;
    private Map<String, TrinketSlot> Slots = new HashMap<>();
    private static ComponentType<EntityStore, TrinketComponent> TrinketsComponentType = null;

    public TrinketComponent(TrinketComponent other) {
        this.Slots = new HashMap<>();

        for (Map.Entry<String, TrinketSlot> entry : other.Slots.entrySet()) {
            this.Slots.put(entry.getKey(), entry.getValue().clone());
        }
    }

    public TrinketComponent() {}

    public Map<String, TrinketSlot> getSlots() {
        return this.Slots;
    }

    @NullableDecl
    public TrinketSlot getSlot(String name) {
        return this.Slots.get(name);
    }

    public void clearAllSlots() {
        for (Map.Entry<String, TrinketSlot> entry : this.Slots.entrySet()) {
            entry.getValue().removeAllItems();
        }
    }

    public Integer SlotsSize() {
        return this.Slots.size();
    }

    public void loadComponent() {
        Map<String, Integer> trinketSlots = HyTrinketPlugin.get().config.get().getSlots();

        this.Slots.keySet().retainAll(trinketSlots.keySet());
        for (var entry : trinketSlots.entrySet()) {
            String type = entry.getKey();
            int size = entry.getValue();

            if (!this.Slots.containsKey(type)) {
                ItemStack[] items = new ItemStack[size];
                Arrays.fill(items, null);
                this.Slots.put(type, new TrinketSlot(type, items));
            }
        }
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new TrinketComponent(this);
    }

    static {
        SlotsCodec = new ArrayCodec<>(TrinketSlot.CODEC, TrinketSlot[]::new);
        CODEC = BuilderCodec.<TrinketComponent>builder(TrinketComponent.class, TrinketComponent::new)
                .append(new KeyedCodec<>("Slots", SlotsCodec),
                        (comp, val) -> {
                            comp.Slots = new HashMap<>();
                            for (TrinketSlot slot : val) {
                                comp.Slots.put(slot.getType(), slot);
                            }
                        },
                        comp -> comp.Slots.values().toArray(new TrinketSlot[0])

                ).add()
                .build();
    }

    public static void setTrinketsComponentType(ComponentType<EntityStore, TrinketComponent> trinketsComponentType) {
        if (TrinketsComponentType != null) return;
        TrinketsComponentType = trinketsComponentType;
    }

    public static ComponentType<EntityStore, TrinketComponent> getTrinketsComponentType() {
        return TrinketsComponentType;
    }
}
