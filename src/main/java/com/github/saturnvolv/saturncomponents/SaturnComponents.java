package com.github.saturnvolv.saturncomponents;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.RarityComponent;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;

import java.util.Arrays;

public class SaturnComponents implements ModInitializer {
    public static final Codec<Integer> STACK_SIZE_INTEGER = Codec.intRange(0, Item.DEFAULT_MAX_COUNT);
    @Override
    public void onInitialize() {
        DataComponentTypes.initialize();
        System.out.println(Arrays.toString(RarityComponent.values()));
    }
}
