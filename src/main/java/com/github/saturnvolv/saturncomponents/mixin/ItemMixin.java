package com.github.saturnvolv.saturncomponents.mixin;

import com.github.saturnvolv.saturncomponents.component.DataComponentTypes;
import com.github.saturnvolv.saturncomponents.component.FoodPropertiesImpl;
import com.github.saturnvolv.saturncomponents.component.type.FoodPropertiesComponent;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, priority = 1001)
abstract class ItemMixin implements FoodPropertiesImpl {
    @Shadow public abstract ComponentMap getComponents();
    @Shadow public abstract Item asItem();

    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract ItemStack finishUsing( ItemStack stack, World world, LivingEntity user );

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;getComponents()Lnet/minecraft/component/ComponentMap;"))
    protected void applyDefaultProperties( Item.Settings settings, CallbackInfo ci ) {
        int maxCount = ((ItemSettingsAccessor) settings).getMaxCount();
        if (maxCount != Item.DEFAULT_MAX_COUNT)
            settings.component(DataComponentTypes.MAX_COUNT, maxCount);
        int maxDamage = ((ItemSettingsAccessor) settings).getMaxDamage();
        if (maxDamage > 0)
            settings.component(DataComponentTypes.MAX_DAMAGE, maxDamage);

        FoodComponent foodComponent = ((ItemSettingsAccessor) settings).getFoodComponent();
        if (foodComponent != null) {
            settings.component(DataComponentTypes.FOOD_PROPERTIES_CONTENT, getDefaultFoodComponent(foodComponent));
        }
    }
    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    protected void getMaxCount( CallbackInfoReturnable<Integer> cir ) {
        cir.setReturnValue(this.getComponentMaxCount());
    }

    @Inject(method = "getItemBarStep", at = @At("HEAD"))
    private void getStackForStep( ItemStack stack, CallbackInfoReturnable<Integer> cir, @Share("durable_item") LocalRef<ItemStack> durableItem) {
        durableItem.set(stack);
    }
    @Inject(method = "getItemBarColor", at = @At("HEAD"))
    private void getStackForColor( ItemStack stack, CallbackInfoReturnable<Integer> cir, @Share("durable_item") LocalRef<ItemStack> durableItem) {
        durableItem.set(stack);
    }
    @Redirect(method = {"getItemBarStep", "getItemBarColor"}, at = @At(value = "FIELD", target = "Lnet/minecraft/item/Item;maxDamage:I"))
    private int getStackAwareMaxDamage( Item instance, @Share("durable_item") LocalRef<ItemStack> durableItem ) {
        return durableItem.get().getMaxDamage();
    }
    @Unique
    public int getComponentMaxCount() {
        Integer maxCount = this.getComponents().get(DataComponentTypes.MAX_COUNT);
        if (maxCount != null)
            return maxCount;
        return Item.DEFAULT_MAX_COUNT;
    }

    @Unique
    public int getComponentMaxDamage() {
        Integer maxDamage = this.getComponents().get(DataComponentTypes.MAX_DAMAGE);
        if (maxDamage != null)
            return maxDamage;
        return 0;
    }

    @Inject(method = "finishUsing", at = @At("TAIL"), cancellable = true)
    protected void finishUsing(ItemStack itemStack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir ) {
        if (this.hasFoodProperties(itemStack)) {
            FoodPropertiesComponent foodComp = this.getFoodProperties(itemStack);
            if (foodComp.hasResultItem() && user instanceof PlayerEntity player) {
                if (!player.isInCreativeMode()) {
                    ItemStack resultItem = foodComp.getResult();
                    if (!player.getInventory().insertStack(resultItem.copy()))
                        player.dropItem(resultItem, false);
                }
            }
            cir.setReturnValue(user.eatFood(world, itemStack));
        }
        else
            cir.setReturnValue(itemStack);
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
                this.hunger(component),
                this.saturationModifier(component),
                this.timeToEat(component),
                new FoodPropertiesComponent.FoodBehaviour(
                        this.isMeat(component),
                        this.alwaysEdible(component),
                        this.isSnack(component),
                        this.isDrink(component),
                        this.clearsEffects(component)
                ),
                this.resultItem(component),
                this.statusEffects(component)
        );
    }
}
