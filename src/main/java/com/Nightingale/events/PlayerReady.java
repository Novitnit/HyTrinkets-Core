package com.Nightingale.events;

import com.Nightingale.HyTrinketPlugin;
import com.Nightingale.components.TrinketsComponents;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PlayerReady {

    public static void handle(PlayerReadyEvent event){
        Player player = event.getPlayer();
        Ref<EntityStore> ref = event.getPlayerRef();
        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        world.execute(()->{
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if(playerRef == null) return;

            TrinketsComponents trinketsComponts = store.ensureAndGetComponent(ref, HyTrinketPlugin.get().getTrinketsComponentsType());
            trinketsComponts.loadComponent();
        });
    }
}
