package com.hydraa.jadecreateaddon.create;

import java.util.List;

import com.zurrtum.create.content.logistics.filter.FilterItem;
import com.zurrtum.create.foundation.blockEntity.SmartBlockEntity;
import com.zurrtum.create.foundation.blockEntity.behaviour.filtering.ServerFilteringBehaviour;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.JadeUI;

public enum FilterProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.FILTER;
    }

    @Override
    public void appendTooltip(@Nullable ITooltip tooltip, BlockAccessor accessor, @Nullable IPluginConfig config) {
        if (tooltip == null) return;
        if (!accessor.showDetails()) return;
        if (!(accessor.getBlockEntity() instanceof SmartBlockEntity te)) return;

        ServerFilteringBehaviour behaviour = te.getBehaviour(ServerFilteringBehaviour.TYPE);
        if (behaviour == null) return;

        ItemStack filter = behaviour.getFilter(accessor.getSide());
        if (filter == null || !(filter.getItem() instanceof FilterItem item)) return;

        List<Component> components = item.makeSummary(filter);
        if (components.isEmpty()) return;

        ITooltip tooltip2 = JadeUI.tooltip();
        for (Component component : components) {
            tooltip2.add(JadeUI.text(component).scale(0.5F));
        }
        tooltip.add(JadeUI.box(tooltip2, BoxStyle.nestedBox()));
    }
}