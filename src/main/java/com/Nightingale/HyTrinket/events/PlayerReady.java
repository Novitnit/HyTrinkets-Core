package com.Nightingale.HyTrinket.events;

import com.Nightingale.HyTrinket.components.TrinketComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PlayerReady {

    public static void handle(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        Ref<EntityStore> ref = event.getPlayerRef();
        Store<EntityStore> store = ref.getStore();
        World world = store.getExternalData().getWorld();

        world.execute(() -> {
            PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
            if (playerRef == null) return;

            UUID PlayerUUID = Objects.requireNonNull(store.getComponent(ref, UUIDComponent.getComponentType())).getUuid();
            PermissionsModule perms = PermissionsModule.get();
            Set<String> groups = perms.getGroupsForUser(PlayerUUID);
            if (!groups.contains("Default")) {
                perms.addUserToGroup(PlayerUUID, "Default");
            }

            TrinketComponent trinketsComponents = store.ensureAndGetComponent(ref, TrinketComponent.getTrinketsComponentType());
            trinketsComponents.loadComponent();
        });
    }
}
