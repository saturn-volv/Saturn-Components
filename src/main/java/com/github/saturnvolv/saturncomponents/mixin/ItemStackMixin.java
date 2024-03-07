package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract ComponentMap getComponents();

    @Shadow public abstract ItemStack copy();

    @Overwrite
    public boolean isFood() {
        return this.hasFoodProperties();
    }
    @Inject(method = "getUseAction", at = @At("TAIL"), cancellable = true)
    public void useAction( CallbackInfoReturnable<UseAction> cir) {
        if (this.hasFoodProperties())
            cir.setReturnValue(
                    this.getFoodProperties().behaviour().getUseAction()
            );
    }
    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishUsing( World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir ) {
        ItemStack instance = this.copy();
        if (!this.hasFoodProperties()) cir.setReturnValue(instance);
        else {
            ItemStack outputItem = user.eatFood(world, instance);
            FoodPropertiesComponent foodComp = this.getFoodProperties();
            if (foodComp.hasResultItem() && outputItem.isEmpty())
                outputItem = new ItemStack(foodComp.getResult().getItem(), foodComp.getResult().getCount());
            cir.setReturnValue(outputItem);
        }
    }
    @Unique
    @Nullable
    public FoodPropertiesComponent getFoodProperties() {
        return this.getComponents().get(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }
    @Unique
    public boolean hasFoodProperties() {
        return this.getComponents().contains(DataComponentTypes.FOOD_PROPERTIES_CONTENT);
    }
}
