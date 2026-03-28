package com.Nightingale.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SlotComponts {
    public static final BuilderCodec<SlotComponts> CODEC;

    private String Type;
    private String[] Item;

    public SlotComponts() {}

    public SlotComponts(String Type, String[] Item) {
        this.Type = Type;
        this.Item = Arrays.copyOf(Item, Item.length);
    }

    public String[] getItem(){
        return this.Item;
    }

    public String getType(){
        return this.Type;
    }

    @Override
    protected SlotComponts clone(){
        return new SlotComponts(this.Type, this.Item);
    }

    static {
        CODEC =BuilderCodec.builder(SlotComponts.class,SlotComponts::new)
                .append(new KeyedCodec<>("Type", Codec.STRING), (comp, val)->comp.Type = val, comp -> comp.Type).add()
                .append(new KeyedCodec<>("Item",Codec.STRING_ARRAY), (comp,val)->comp.Item = val, comp -> comp.Item).add()
                .build();
    }
}
