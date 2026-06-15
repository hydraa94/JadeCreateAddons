package com.hydraa.jadecreateaddon.create;

import java.util.Set;

import com.hydraa.jadecreateaddon.mixin.KineticBlockEntityAccess;

import com.zurrtum.create.Create;
import com.zurrtum.create.content.equipment.goggles.GogglesItem;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GogglesProvider implements IBlockComponentProvider {
    INSTANCE;

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK
                .get(Identifier.fromNamespaceAndPath("create", id))
                .map(net.minecraft.core.Holder::value)
                .orElse(net.minecraft.world.level.block.Blocks.AIR);
    }

    private static final Set<String> REMOVE_KEYS = Set.of(
            "create.tooltip.chute.contains",
            "create.tooltip.deployer.contains"
    );

    private static final Block PISTON_EXTENSION_POLE = block("piston_extension_pole");

    @Override
    public @NullMarked Identifier getUid() {
        return CreatePlugin.GOGGLES;
    }

    @Override
    public boolean enabledByDefault() {
        return false;
    }

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip1, BlockAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip1 == null || config == null) return;
        if(config.get(CreatePlugin.GOGGLES_DETAILED) && !accessor.showDetails()) return;
        if (!GogglesItem.isWearingGoggles(accessor.getPlayer())) return;

        CompoundTag data = accessor.getServerData();
        int rpm = data.getInt("rpm").orElse(0);
        if (rpm == 0) return;

        int stress = data.getInt("stress").orElse(0);
        int capacity = data.getInt("capacity").orElse(0);

        tooltip1.add(Component.translatable("jade_create_addon.rpm", rpm));
        tooltip1.add(Component.translatable("jade_create_addon.stress", stress, capacity));
    }


}