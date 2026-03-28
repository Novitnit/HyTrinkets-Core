package com.Nightingale.components;

import com.Nightingale.HyTrinketPlugin;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.*;

public class TrinketsComponents implements Component<EntityStore> {

    public static final BuilderCodec<TrinketsComponents> CODEC;
    static Codec<SlotComponts[]> SlotsCodec;
    private Map<String, SlotComponts> Slots = new HashMap<>();

    public TrinketsComponents(TrinketsComponents other) {
        this.Slots = new HashMap<>();

        for (Map.Entry<String, SlotComponts> entry : other.Slots.entrySet()) {
            this.Slots.put(entry.getKey(), entry.getValue().clone());
        }
    }

    public TrinketsComponents() {
    }

    public void loadComponent() {
        Map<String, Integer> trinketSlots = HyTrinketPlugin.get().config.get().getSlots();

        for (var entry : trinketSlots.entrySet()) {
            String type = entry.getKey();
            int size = entry.getValue();

            if (!this.Slots.containsKey(type)) {
                String[] items = new String[size];
                Arrays.fill(items, null);

                Slots.put(type, new SlotComponts(type, items));
            }
        }
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new TrinketsComponents(this);
    }

    static {
        SlotsCodec = new ArrayCodec<>(SlotComponts.CODEC, SlotComponts[]::new);
        CODEC = BuilderCodec.<TrinketsComponents>builder(TrinketsComponents.class, TrinketsComponents::new)
                .append(new KeyedCodec<>("Slots", SlotsCodec),
                        (comp, val) -> {
                            comp.Slots = new HashMap<>();
                            for (SlotComponts slot : val) {
                                comp.Slots.put(slot.getType(), slot);
                            }
                        },
                        comp -> comp.Slots.values().toArray(new SlotComponts[0])

                ).add()
                .build();
    }
}
