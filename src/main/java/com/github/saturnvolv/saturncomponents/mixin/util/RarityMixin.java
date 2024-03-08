package com.github.saturnvolv.saturncomponents.mixin.util;

import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Rarity.class)
public abstract class RarityMixin {
    public String toString(Rarity rarity) {
        return switch (rarity) {
            case EPIC -> "epic";
            case RARE -> "rare";
            case UNCOMMON -> "uncommmon";
            default -> "common";
        };
    }
}
