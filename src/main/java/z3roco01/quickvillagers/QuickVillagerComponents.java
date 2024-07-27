package z3roco01.quickvillagers;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class QuickVillagerComponents {
    public static ComponentType<Boolean> SPAWN_BABY;

    public static void register() {
        SPAWN_BABY = register("spawn_baby", (builder) ->
                builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).cache()
        );
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(QuickVillagers.modid, id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
