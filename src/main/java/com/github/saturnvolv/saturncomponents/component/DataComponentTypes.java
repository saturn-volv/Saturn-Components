package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.SaturnComponents;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

public class DataComponentTypes {
    public static final DataComponentType<FoodPropertiesComponent> FOOD_PROPERTIES_CONTENT;
    public static final DataComponentType<Integer> MAX_STACK_SIZE_CONTENT;
    public static void initialize() {}
    static {
        FOOD_PROPERTIES_CONTENT = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("food_properties"),
                DataComponentType.<FoodPropertiesComponent>builder().codec(FoodPropertiesComponent.CODEC).packetCodec(FoodPropertiesComponent.PACKET_CODEC).build());
        MAX_STACK_SIZE_CONTENT = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("max_count"),
                DataComponentType.<Integer>builder().codec(SaturnComponents.STACK_SIZE_INTEGER).packetCodec(PacketCodecs.VAR_INT).build()
        );
    }
}
