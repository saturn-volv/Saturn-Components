package com.github.saturnvolv.saturncomponents.component;

import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DataComponentTypes {
    public static final DataComponentType<FoodPropertiesComponent> FOOD_PROPERTIES_CONTENT;
    public static void initialize() {}
    static {
        FOOD_PROPERTIES_CONTENT = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("food_properties"),
                DataComponentType.<FoodPropertiesComponent>builder().codec(FoodPropertiesComponent.CODEC).packetCodec(FoodPropertiesComponent.PACKET_CODEC).build());
    }
}
