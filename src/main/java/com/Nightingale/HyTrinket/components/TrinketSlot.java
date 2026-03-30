package com.Nightingale.HyTrinket.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Arrays;

public class TrinketSlot {
    public static final BuilderCodec<TrinketSlot> CODEC;

    private String Type;
    private ItemStack[] Item;

    public TrinketSlot() {
    }

    public TrinketSlot(String Type, ItemStack[] Item) {
        this.Type = Type;
        this.Item = Arrays.copyOf(Item, Item.length);
    }

    public ItemStack[] getItems() {
        return this.Item;
    }

    public void setItems(ItemStack[] Item) {
        if (this.Item == null || Item.length != this.Item.length)
            throw new IllegalArgumentException("Item size must be equal to Item size.");
        this.Item = Arrays.copyOf(Item, Item.length);
    }

    public void setItem(short Slot, ItemStack ItemId) {
        if (Slot < 0 || Slot >= this.Item.length) {
            throw new IndexOutOfBoundsException("Invalid slot: " + Slot);
        }
        this.Item[Slot] = ItemId;
    }

    @NullableDecl
    public ItemStack getItem(int index) {
        return this.Item[index];
    }

    public String getType() {
        return this.Type;
    }

    public boolean haveItem(String ItemId) {
        boolean found = false;
        for (ItemStack itemStack : this.Item) {
            if (itemStack != null && itemStack.getItemId().equals(ItemId)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void removeItem(String itemId, int amount) {
        int removed = 0;
        for (int i = 0; i < this.Item.length; i++) {
            ItemStack itemStack = this.Item[i];
            if (itemStack != null && itemStack.getItemId().equals(itemId)) {
                this.Item[i] = null;
                removed++;
                if (removed >= amount) break;
            }
        }
    }

    public void removeAllItems() {
        Arrays.fill(this.Item, null);
    }

    @Override
    protected TrinketSlot clone() {
        return new TrinketSlot(this.Type, this.Item);
    }

    static {
        CODEC = BuilderCodec.builder(TrinketSlot.class, TrinketSlot::new)
                .append(new KeyedCodec<>("Type", Codec.STRING),
                        (comp, val) -> comp.Type = val,
                        comp -> comp.Type).add()
                .append(new KeyedCodec<>("Item", new ArrayCodec<>(ItemStack.CODEC, ItemStack[]::new)),
                        (comp, val) -> comp.Item = val,
                        comp -> comp.Item).add()
                .build();
    }
}
