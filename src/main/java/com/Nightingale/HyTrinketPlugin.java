package com.Nightingale;

import com.Nightingale.Api.RegisterTrinkets;
import com.Nightingale.command.OpenHub;
import com.Nightingale.components.TrinketsComponents;
import com.Nightingale.config.TrinketsConfig;
import com.Nightingale.events.PlayerReady;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import java.util.Set;

public class HyTrinketPlugin extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static HyTrinketPlugin instance;
    public final Config<TrinketsConfig> config;
    private ComponentType<EntityStore, TrinketsComponents> TrinketsComponentType;

    public HyTrinketPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;

        this.config = this.withConfig("TrinketsConfig", TrinketsConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new OpenHub());

        RegisterTrinkets.RegisterSlot("Ring",2);
        RegisterTrinkets.RegisterSlot("Necklace",1);

        // EntityStoreRegis
        final var entityStoreRegistry = this.getEntityStoreRegistry();
        this.TrinketsComponentType = entityStoreRegistry
                .registerComponent(TrinketsComponents.class, "TrinketsComponents", TrinketsComponents.CODEC);

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerReady::handle);
        this.saveConfig();
    }

    @Override
    protected void start() {
        super.start();
        LOGGER.atInfo().log("Starting plugin " + this.getName());

        final Set<String> playerPermissions = Set.of(
                OpenHub.PERMISSION
        );

        try {
            PermissionsModule.get().addGroupPermission("Default", playerPermissions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static HyTrinketPlugin get() {
        return instance;
    }

    public void saveConfig() {
        this.config.get().cleanSlot();
        this.config.save();
    }

    public TrinketsConfig getConfig() {
        return this.config.get();
    }

    public ComponentType<EntityStore, TrinketsComponents> getTrinketsComponentsType() {
        return TrinketsComponentType;
    }

    static public void Log(String message) {
        LOGGER.atInfo().log(message);
    }
}