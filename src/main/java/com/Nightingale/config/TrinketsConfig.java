package com.Nightingale.config;

import com.Nightingale.Api.RegisterTrinkets;
import com.Nightingale.HyTrinketPlugin;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.logger.HytaleLogger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TrinketsConfig {
    private Map<String, Integer> TrinketsSlotsType = new HashMap<String, Integer>();
    static final Integer MaxSlot = 10;

    public static final BuilderCodec<TrinketsConfig> CODEC;

    private Map<String, Integer> getTrinketSlots() {
        return this.TrinketsSlotsType;
    }

    public TrinketsConfig() {
    }

    public void addSlot(String type, Integer size) {
        if (!this.TrinketsSlotsType.containsKey(type)) {
            size = Math.max(0, Math.min(size, MaxSlot));
            this.TrinketsSlotsType.put(type, size);
        }
    }

    public void cleanSlot() {
        HyTrinketPlugin.Log("Cleaning TrinketsConfig");
        this.TrinketsSlotsType.entrySet().removeIf(entry ->
                !RegisterTrinkets.TrinketsSlotsType.contains(entry.getKey())
        );
    }

    public Map<String, Integer> getSlots() {
        return this.TrinketsSlotsType;
    }

    static {
        CODEC = BuilderCodec.builder(TrinketsConfig.class, TrinketsConfig::new)
                .append(new KeyedCodec<>("TrinketsSlot", new MapCodec<>(Codec.INTEGER, LinkedHashMap::new)),
                        (comp, map) -> comp.TrinketsSlotsType = new LinkedHashMap<>(map),
                        TrinketsConfig::getTrinketSlots)
                .add()
                .build();
    }
}