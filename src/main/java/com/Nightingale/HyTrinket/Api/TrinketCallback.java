package com.Nightingale.HyTrinket.Api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public abstract class TrinketCallback {

    public final boolean equip(
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef
    ) {
        return onEquip(store, ref, playerRef);
    }

    public final boolean unequip(
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef
    ) {
        return onUnequip(store, ref, playerRef);
    }

    protected boolean onEquip(Player player) {
        return false;
    }

    protected boolean onUnequip(Player player) {
        return false;
    }

    protected boolean onEquip(
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef
    ) {
        return onEquip(store.getComponent(ref,Player.getComponentType()));
    }

    protected boolean onUnequip(
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            PlayerRef playerRef
    ) {
        return onUnequip(store.getComponent(ref,Player.getComponentType()));
    }
}
