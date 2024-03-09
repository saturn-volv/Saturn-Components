package com.github.saturnvolv.saturncomponents.mixin;

import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.component.ComponentMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackArgument.class)
public interface ItemStackArgumentAccessor {
    @Accessor
    ComponentMap getComponents();
}
