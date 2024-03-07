package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = Item.class, priority = 1001)
abstract class ItemMixin implements FoodPropertiesImpl {
    @Shadow public abstract ComponentMap getComponents();

    @Shadow public abstract int getMaxCount();

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;getComponents()Lnet/minecraft/component/ComponentMap;"))
    protected void applyDefaultProperties( Item.Settings settings, CallbackInfo ci ) {
        FoodComponent foodComponent = ((ItemSettingsAccessor) settings).getFoodComponent();
        if (foodComponent != null) {
            settings.component(DataComponentTypes.FOOD_PROPERTIES_CONTENT, getDefaultFoodComponent(foodComponent));
        }

        int maxCount = ((ItemSettingsAccessor) settings).getMaxCount();
        if (maxCount != Item.DEFAULT_MAX_COUNT)
            settings.component(DataComponentTypes.MAX_STACK_SIZE_CONTENT, maxCount);
    }
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    protected void getMaxCount( CallbackInfoReturnable<Integer> cir ) {
        cir.setReturnValue(this.getComponents().get(DataComponentTypes.MAX_STACK_SIZE_CONTENT));
    }
    @Inject( method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    protected void getMaxUseTime( ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        if (this.hasFoodProperties(itemStack)) {
            FoodPropertiesComponent foodComponent = this.getFoodProperties(itemStack);
            assert foodComponent != null;
            int timeToEat = foodComponent.timeToEat();
            if (foodComponent.behaviour().isSnack()) timeToEat /= 2;
            cir.setReturnValue(timeToEat);
        }
    }
    @Inject( method = "getUseAction", at = @At("HEAD"), cancellable = true)
    protected void getUseAction( ItemStack itemStack, CallbackInfoReturnable<UseAction> cir ) {
        if (this.hasFoodProperties(itemStack))
            cir.setReturnValue(
                    this.getFoodProperties(itemStack).behaviour().getUseAction()
            );
        else
            cir.setReturnValue(UseAction.NONE);
    }
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;isFood()Z"), cancellable = true)
    protected void use( World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir ) {
        ItemStack stack = user.getStackInHand(hand);
        if (!this.hasFoodProperties(stack)) cir.setReturnValue(TypedActionResult.pass(stack));
        else if (user.canConsume(this.getFoodProperties(stack).behaviour().alwaysEdible())) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(stack));
        } else
            cir.setReturnValue(TypedActionResult.fail(stack));
    }

    @Unique
    private FoodPropertiesComponent getDefaultFoodComponent(@NotNull FoodComponent foodComponent) {
        FoodPropertiesComponent component = FoodPropertiesComponent.fromFoodSettings(foodComponent);
        return new FoodPropertiesComponent(
                this.saturnComponents$hunger(component),
                this.saturnComponents$saturationModifier(component),
                this.saturnComponents$timeToEat(component),
                new FoodPropertiesComponent.FoodBehaviour(
                        this.saturnComponents$isMeat(component),
                        this.saturnComponents$alwaysEdible(component),
                        this.saturnComponents$isSnack(component),
                        this.saturnComponents$isDrink(component),
                        this.saturnComponents$clearsEffects(component)
                ),
                Optional.of(this.saturnComponents$resultItem(component)),
                this.saturnComponents$statusEffects(component)
        );
    }
}
