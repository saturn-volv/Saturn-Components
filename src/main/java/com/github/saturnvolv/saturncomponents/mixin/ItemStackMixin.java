package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract ComponentMap getComponents();

    @Shadow public abstract ItemStack copy();

    @Shadow public abstract Item getItem();

    @Shadow public abstract boolean isDamageable();

    /**
     * @author saturn-volv
     * @reason Just saves me having to redirect EVERY instance of it being called...
     */
    @Overwrite
    public boolean isFood() {
        return this.hasFoodProperties();
    }
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    public void maxCount( CallbackInfoReturnable<Integer> cir ) {
        cir.setReturnValue(this.getComponentMaxCount());
    }
    @Inject(method = "isDamageable", at = @At("HEAD"))
    private void getStackDamageable( CallbackInfoReturnable<Boolean> cir, @Share("share_stack") LocalRef<ItemStack> sharedStack ) {
        sharedStack.set(this.copy());
    }
    @Redirect(method = "isDamageable", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isDamageable()Z"))
    private boolean getStackAwareDamageable( Item instance, @Share("share_stack") LocalRef<ItemStack> sharedStack ) {
        ItemStack itemStack = sharedStack.get();
        if (itemStack != null && !itemStack.isEmpty())
            if (itemStack.contains(DataComponentTypes.MAX_DAMAGE)) return itemStack.get(DataComponentTypes.MAX_DAMAGE) > 0;
        return false;
    }

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    public void maxDamage( CallbackInfoReturnable<Integer> cir ) {
        cir.setReturnValue(this.getComponents().get(DataComponentTypes.MAX_DAMAGE));
    }

    @Unique
    public int getComponentMaxCount() {
        Integer maxCount = this.getComponents().get(DataComponentTypes.MAX_COUNT);
        if (maxCount != null)
            return maxCount;
        return Item.DEFAULT_MAX_COUNT;
    }
    @Inject(method = "getUseAction", at = @At("TAIL"), cancellable = true)
    public void useAction( CallbackInfoReturnable<UseAction> cir) {
        if (this.hasFoodProperties())
            cir.setReturnValue(
                    this.getFoodProperties().behaviour().getUseAction()
            );
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
