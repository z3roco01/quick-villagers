package z3roco01.quickvillagers.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SpawnEggItem.class)
public abstract class SpawnEggItemMixin extends Item {
    public SpawnEggItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method="spawnBaby", at=@At("HEAD"), cancellable = true)
    private void spawnBaby(PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack, CallbackInfoReturnable<Optional<MobEntity>> cir) {
        NbtComponent entityData = stack.get(DataComponentTypes.ENTITY_DATA);
        if(entityType == EntityType.VILLAGER && entityData != null) {
            entity.readCustomDataFromNbt(entityData.copyNbt());
            if(entity.age != 0) { // baby
                entity.setBaby(true);
            }
            cir.setReturnValue(Optional.of(entity));
            cir.cancel();
        }
    }
}
