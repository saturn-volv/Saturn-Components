package com.github.saturnvolv.saturncomponents.mixin.server.command;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.mixin.ItemStackArgumentAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.item.Item;
import net.minecraft.server.command.GiveCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GiveCommand.class)
public abstract class GiveCommandMixin {
    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxCount()I"))
    private static int getComponentAwareMaxCount( Item instance, @Local(argsOnly = true) ItemStackArgument item ) {
        if (((ItemStackArgumentAccessor) item).getComponents().contains(DataComponentTypes.MAX_COUNT))
            return ((ItemStackArgumentAccessor) item).getComponents().get(DataComponentTypes.MAX_COUNT);
        return instance.getMaxCount();
    }
}
