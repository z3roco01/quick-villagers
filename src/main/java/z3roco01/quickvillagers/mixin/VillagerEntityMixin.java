package z3roco01.quickvillagers.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z3roco01.quickvillagers.QuickVillagers;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin {
    @Shadow
    protected abstract void writeCustomData(WriteView view);

    @Inject(method="interactMob", at=@At("HEAD"), cancellable = true)
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
        if(player.isSneaking()) {
            NbtCompound eggData = new NbtCompound();

            NbtWriteView view = NbtWriteView.create(ErrorReporter.EMPTY);
            this.writeCustomData(view);

            view.putString("id", "minecraft:villager");
            view.putBoolean("quickvillager:baby", ((VillagerEntity)(Object)this).isBaby());

            eggData.put("EntityTag", view.getNbt());

            ItemStack egg = new ItemStack(Items.VILLAGER_SPAWN_EGG, 1);
            egg.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(view.getNbt()));

            String profession = view.getNbt().getCompound("VillagerData").get().getString("profession").get();
            String type = view.getNbt().getCompound("VillagerData").get().getString("type").get();

            Text loreText = Text.of(Text.translatable("biome.minecraft." + type.substring(type.indexOf(":")+1)).getString()
                            + " " +
                            Text.translatable("entity.minecraft.villager." + profession.substring(profession.indexOf(":")+1)).getString());
            egg.set(DataComponentTypes.LORE, new LoreComponent(loreText.getWithStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(false))));
            //egg.set(QuickVillagerComponents.SPAWN_BABY, ((VillagerEntity)(Object)this).isBaby());
            player.getInventory().offerOrDrop(egg);

            ((VillagerEntity)(Object)this).discard();

            ci.setReturnValue(ActionResult.SUCCESS);
            ci.cancel();
        }
    }
}
