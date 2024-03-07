package com.github.saturnvolv.saturncomponents;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;

public class SaturnComponents implements ModInitializer {
    @Override
    public void onInitialize() {
        DataComponentTypes.initialize();
    }
}
