package com.Nightingale.command;

import au.ellie.hyui.builders.PageBuilder;
import com.Nightingale.Ui.EventsGrid;
import com.Nightingale.Ui.MainPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class OpenHub extends AbstractPlayerCommand {

    public static final String PERMISSION = "hyTrinkets.command.base";

    public OpenHub() {
        super("trinket", "Command to Open Hub");
        this.addAliases("tk");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext commandContext, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        playerRef.sendMessage(Message.raw("Opening Hub"));
        PageBuilder page = MainPage.Main(playerRef);

        MainPage.UpdateInventory(playerRef, page);
        EventsGrid.initEvent(playerRef, page);

        page.open(store);
    }
}
