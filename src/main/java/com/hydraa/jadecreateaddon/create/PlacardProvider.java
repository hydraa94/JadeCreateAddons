package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.decoration.placard.PlacardBlockEntity;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;

public enum PlacardProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, BlockAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip == null) return;
        if (accessor.getBlockEntity() instanceof PlacardBlockEntity placard) {
            if (!placard.getHeldItem().isEmpty()) {
                tooltip.add(placard.getHeldItem().getHoverName());
            }
        }
    }

    @Override
    public @Nullable Element getIcon(BlockAccessor accessor, @Nullable IPluginConfig config, @Nullable Element currentIcon) {
        if (accessor.getBlockEntity() instanceof PlacardBlockEntity placard) {
            if (!placard.getHeldItem().isEmpty()) {
                return JadeUI.item(placard.getHeldItem());
            }
        }
        return null;
    }

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.PLACARD;
    }
}