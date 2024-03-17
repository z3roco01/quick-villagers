package z3roco01.quickvillagers.mixin;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z3roco01.quickvillagers.QuickVillagers;

import java.util.Objects;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Shadow public abstract void writeCustomDataToNbt(NbtCompound nbt);

    @Inject(method="interactMob", at=@At("HEAD"), cancellable = true)
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
        if(player.isSneaking()) {
            QuickVillagers.INSTANCE.getLogger().info("villager pickup :3");

            NbtCompound eggData = new NbtCompound();

            NbtCompound entityTag = new NbtCompound();
            this.writeCustomDataToNbt(entityTag);
            entityTag.putString("id", "minecraft:villager");

            eggData.put("EntityTag", entityTag);

            QuickVillagers.INSTANCE.getLogger().info(eggData.toString());

            ItemStack egg = new ItemStack(Items.VILLAGER_SPAWN_EGG, 1);
            egg.setNbt(eggData);

            String profession = entityTag.getCompound("VillagerData").getString("profession");
            String type = entityTag.getCompound("VillagerData").getString("type");

            Text loreText = Text.of(Text.translatable("biome.minecraft." + type.substring(type.indexOf(":")+1)).getString()
                            + " " +
                            Text.translatable("entity.minecraft.villager." + profession.substring(profession.indexOf(":")+1)).getString());
            NbtList lore = new NbtList();
            lore.addElement(0, NbtString.of(Text.Serialization.toJsonString(loreText.getWithStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(false)).get(0))));
            egg.getOrCreateSubNbt("display").put("Lore", lore);
            Objects.requireNonNull(egg.getNbt()).putBoolean("notSpawnBaby", true);
            player.getInventory().offerOrDrop(egg);

            ((VillagerEntity)(Object)this).discard();

            ci.setReturnValue(ActionResult.SUCCESS);
            ci.cancel();
        }
    }
}
