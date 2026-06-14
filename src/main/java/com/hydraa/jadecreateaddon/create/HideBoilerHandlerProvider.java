package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.fluids.tank.FluidTankBlockEntity;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

public enum HideBoilerHandlerProvider implements IServerExtensionProvider<FluidView.Data>,
        IClientExtensionProvider<FluidView.Data, FluidView> {
    INSTANCE;

    @Override
    public Identifier getUid() {
        return CreatePlugin.HIDE_BOILER_TANKS;
    }

    @Override
    public @Nullable List<ViewGroup<FluidView.Data>> getGroups(Accessor<?> accessor) {
        if (!(accessor.getTarget() instanceof FluidTankBlockEntity tank)) return null;
        FluidTankBlockEntity controller = tank.getControllerBE();
        if (controller != null && controller.boiler.isActive()) {
            return List.of();
        }
        return null;
    }

    @Override
    public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<FluidView.Data>> groups) {
        return List.of();
    }
}