package com.Nightingale;

import com.Nightingale.Api.TrinketRegistry;
import com.Nightingale.command.OpenHub;
import com.Nightingale.components.TrinketComponent;
import com.Nightingale.config.TrinketsConfig;
import com.Nightingale.events.PlayerReady;
import com.Nightingale.events.PreventDeathSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.Nightingale.Api.TrinketRegistry.SlotAllowedItems;

public class HyTrinketPlugin extends JavaPlugin {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static HyTrinketPlugin instance;
    public final Config<TrinketsConfig> config;
//    private ComponentType<EntityStore, TrinketsComponents> TrinketsComponentType;

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
        SlotAllowedItems.clear();

        // RegisterTrinkets Slots
        TrinketRegistry.RegisterSlot("Ring", 2);
        TrinketRegistry.RegisterSlot("Necklace", 1);

        //Register Item
        TrinketRegistry.RegisterItem("Necklace","ResurrectionCollar");

        // EntityStoreRegis
        final var entityStoreRegistry = this.getEntityStoreRegistry();
        TrinketComponent.setTrinketsComponentType(
                entityStoreRegistry.registerComponent(TrinketComponent.class, "TrinketsComponents", TrinketComponent.CODEC)
        );

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerReady::handle);
        this.saveConfig();
    }

    @Override
    protected void start() {
        super.start();

        final Set<String> playerPermissions = Set.of(
                OpenHub.PERMISSION
        );

        this.getEntityStoreRegistry().registerSystem(new PreventDeathSystem());

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
    static public void Log(String message) {
        LOGGER.atInfo().log(message);
    }
}