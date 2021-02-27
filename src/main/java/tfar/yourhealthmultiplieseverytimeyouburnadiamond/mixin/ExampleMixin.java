package tfar.yourhealthmultiplieseverytimeyouburnadiamond.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.yourhealthmultiplieseverytimeyouburnadiamond.YourHealthMultipliesEverytimeYouBurnADiamond;

@Mixin(ItemEntity.class)
public abstract class ExampleMixin extends Entity {
	public ExampleMixin(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}

	@Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/entity/item/ItemEntity;remove()V"), method = "attackEntityFrom")
	private void init(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		YourHealthMultipliesEverytimeYouBurnADiamond.diamondBurnHook((ItemEntity)(Object)this,source);
	}
}
