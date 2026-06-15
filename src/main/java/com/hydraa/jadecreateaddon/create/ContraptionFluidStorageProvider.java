package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;
import com.zurrtum.create.infrastructure.fluids.FluidInventory;
import com.zurrtum.create.infrastructure.fluids.FluidStack;

import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

import snownee.jade.api.Accessor;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public enum ContraptionFluidStorageProvider implements IServerExtensionProvider<FluidView.Data>,
        IClientExtensionProvider<FluidView.Data, FluidView> {
    INSTANCE;

    @Override
    public @Nullable Identifier getUid() {
        return CreatePlugin.CONTRAPTION_INVENTORY;
    }

    @Override
    public @Nullable List<ViewGroup<FluidView.Data>> getGroups(Accessor<?> accessor) {
        if (!(accessor.getTarget() instanceof AbstractContraptionEntity entity)) return null;
        FluidInventory fluids = entity.getContraption().getStorage().getFluids();

        List<FluidView.Data> dataList = new ArrayList<>();
        for (int i = 0; i < fluids.size(); i++) {
            FluidStack stack = fluids.getStack(i);
            if (stack.isEmpty()) continue;
            JadeFluidObject fluidObject = JadeFluidObject.of(stack.getFluid(), stack.getAmount());
            dataList.add(new FluidView.Data(fluidObject, stack.getMaxAmount()));
        }

        if (dataList.isEmpty()) return null;
        return List.of(new ViewGroup<>(dataList));
    }

    @Override
    public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<FluidView.Data>> groups) {
        return ClientViewGroup.map(groups, FluidView::readDefault, null);
    }
}