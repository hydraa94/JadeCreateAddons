package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;
import com.zurrtum.create.content.equipment.blueprint.BlueprintEntity;
import com.zurrtum.create.content.fluids.tank.FluidTankBlockEntity;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

import net.minecraft.world.level.block.Block;

import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;

import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock;
import com.zurrtum.create.content.decoration.placard.PlacardBlock;
import com.zurrtum.create.content.equipment.armor.BacktankBlock;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;


@WailaPlugin
public class CreatePlugin implements IWailaPlugin {

    public static final String ID = "create";
    public static final Identifier BLAZE_BURNER = Identifier.fromNamespaceAndPath(ID, "blaze_burner");
    public static final Identifier BACKTANK_CAPACITY = Identifier.fromNamespaceAndPath(ID, "backtank_capacity");
    public static final Identifier PLACARD = Identifier.fromNamespaceAndPath(ID, "placard");
    public static final Identifier FILTER = Identifier.fromNamespaceAndPath(ID, "filter");
    public static final Identifier GOGGLES = Identifier.fromNamespaceAndPath(ID, "goggles");
    public static final Identifier REQUIRES_GOGGLES = Identifier.fromNamespaceAndPath(ID, "goggles.requires_goggles");
    public static final Identifier GOGGLES_DETAILED = Identifier.fromNamespaceAndPath(ID, "goggles.detailed");
    public static final Identifier HIDE_BOILER_TANKS = Identifier.fromNamespaceAndPath(ID, "hide_boiler_tanks");
    public static final Identifier CRAFTING_BLUEPRINT = Identifier.fromNamespaceAndPath(ID, "crafting_blueprint");
    public static final Identifier CONTRAPTION_INVENTORY = Identifier.fromNamespaceAndPath(ID, "contraption_inv");



    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BlazeBurnerDataProvider.INSTANCE, BlazeBurnerBlockEntity.class);
        registration.registerBlockDataProvider(BacktankDataProvider.INSTANCE, BacktankBlockEntity.class);
        registration.registerFluidStorage(HideBoilerHandlerProvider.INSTANCE, FluidTankBlockEntity.class);
        registration.registerItemStorage(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
        registration.registerBlockDataProvider(KineticDataProvider.INSTANCE, KineticBlockEntity.class);
        registration.registerItemStorage(ContraptionItemStorageProvider.INSTANCE, AbstractContraptionEntity.class);
        registration.registerFluidStorage(ContraptionFluidStorageProvider.INSTANCE, AbstractContraptionEntity.class);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(CreatePlugin.REQUIRES_GOGGLES, true);
        registration.addConfig(CreatePlugin.GOGGLES_DETAILED, false);
        registration.registerBlockComponent(FilterProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(BlazeBurnerProvider.INSTANCE, BlazeBurnerBlock.class);
        registration.registerBlockComponent(BacktankProvider.INSTANCE, BacktankBlock.class);
        registration.registerBlockComponent(PlacardProvider.INSTANCE, PlacardBlock.class);
        registration.registerBlockIcon(PlacardProvider.INSTANCE, PlacardBlock.class);
        registration.registerBlockComponent(GogglesProvider.INSTANCE, Block.class);
        registration.registerFluidStorageClient(HideBoilerHandlerProvider.INSTANCE);
        registration.registerEntityComponent(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
        registration.registerEntityIcon(CraftingBlueprintProvider.INSTANCE, BlueprintEntity.class);
        registration.registerItemStorageClient(CraftingBlueprintProvider.INSTANCE);
        registration.registerItemStorageClient(ContraptionItemStorageProvider.INSTANCE);
        registration.registerFluidStorageClient(ContraptionFluidStorageProvider.INSTANCE);
    }

}