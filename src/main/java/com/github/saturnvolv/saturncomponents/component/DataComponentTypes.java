package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.SaturnComponents;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.github.saturnvolv.saturncomponents.component.type.RarityComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class DataComponentTypes {
    public static final DataComponentType<FoodPropertiesComponent> FOOD_PROPERTIES_CONTENT;
    public static final DataComponentType<Integer> MAX_COUNT;
    public static final DataComponentType<Integer> MAX_DAMAGE;
    public static final DataComponentType<RarityComponent> RARITY;
    public static final DataComponentType<Boolean> IS_FIREPROOF;
    public static final DataComponentType<ItemStack> RECIPE_REMAINDER;
    public static void initialize() {}
    static {
        FOOD_PROPERTIES_CONTENT = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("food_properties"),
                DataComponentType.<FoodPropertiesComponent>builder().codec(FoodPropertiesComponent.CODEC).packetCodec(FoodPropertiesComponent.PACKET_CODEC).build());
        MAX_COUNT = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("max_count"),
                DataComponentType.<Integer>builder().codec(SaturnComponents.STACK_SIZE_INTEGER).packetCodec(PacketCodecs.VAR_INT).build()
        );
        MAX_DAMAGE = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("max_damage"),
                DataComponentType.<Integer>builder().codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT).build()
        );
        RARITY = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("rarity"),
                DataComponentType.<RarityComponent>builder().codec(RarityComponent.CODEC).packetCodec(RarityComponent.PACKET_CODEC).build()
        );
        IS_FIREPROOF = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("fireproof"),
                DataComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build()
        );
        RECIPE_REMAINDER = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("recipe_remainder"),
                DataComponentType.<ItemStack>builder().codec(ItemStack.CODEC).packetCodec(ItemStack.PACKET_CODEC).build()
        );

    }
}
