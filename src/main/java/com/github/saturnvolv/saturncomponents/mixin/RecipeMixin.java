package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.fabric.impl.item.RecipeRemainderHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Recipe.class, priority = 1500)
public interface RecipeMixin<C extends Inventory>{
    @ModifyExpressionValue(method = "getRemainder", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;"))
    private ItemStack getStackAwareRemainder( ItemStack original, @Share("recipe_remainder") LocalRef<ItemStack> sharedStack ) {
        if (original.contains(DataComponentTypes.RECIPE_REMAINDER))
            RecipeRemainderHandler.REMAINDER_STACK.set(original.get(DataComponentTypes.RECIPE_REMAINDER).copy());
        return original;
    }
}
