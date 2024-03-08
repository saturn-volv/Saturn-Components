package com.github.saturnvolv.saturncomponents.util;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum RarityComponent implements StringIdentifiable {
    COMMON(0, "common", Formatting.WHITE, Rarity.COMMON),
    UNCOMMON(1, "uncommon", Formatting.YELLOW, Rarity.UNCOMMON),
    RARE(2, "rare", Formatting.AQUA, Rarity.RARE),
    EPIC(3, "epic", Formatting.LIGHT_PURPLE, Rarity.EPIC);

    public static final IntFunction<RarityComponent> BY_ID = ValueLists.createIdToValueFunction(RarityComponent::id, values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final StringIdentifiable.EnumCodec<RarityComponent> CODEC = StringIdentifiable.createCodec(RarityComponent::values);
    public static final PacketCodec<ByteBuf, RarityComponent> PACKET_CODEC = PacketCodecs.indexed(BY_ID, RarityComponent::id);
    public final int id;
    public final String name;
    public final Formatting formatting;
    public final Rarity rarity;
    private RarityComponent( int id, String name, Formatting formatting, Rarity rarity ) {
        this.id = id;
        this.name = name;
        this.formatting = formatting;
        this.rarity = rarity;
    }

    public int id() {
        return id;
    }

    @Override
    public String asString() {
        return this.name;
    }
    
    public static RarityComponent fromVanillaRarity( Rarity rarity ) {
        for (RarityComponent erarity : values()) {
            if (erarity.rarity == rarity) return erarity;
        }
        return COMMON;
    }

    private static boolean hasRarityComponent(ItemStack itemStack) {
        return itemStack.getComponents().contains(DataComponentTypes.RARITY);
    }
    @NotNull
    public static RarityComponent getRarityComponent( ItemStack itemStack ) {
        if (!hasRarityComponent(itemStack)) return COMMON;
        return itemStack.getComponents().get(DataComponentTypes.RARITY);
    }
}

