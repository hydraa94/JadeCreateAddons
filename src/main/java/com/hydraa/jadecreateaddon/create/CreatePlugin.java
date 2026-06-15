package com.hydraa.jadecreateaddon.create;

import com.zurrtum.create.client.foundation.utility.RaycastHelper;
import com.zurrtum.create.content.contraptions.AbstractContraptionEntity;
import com.zurrtum.create.content.contraptions.Contraption;
import com.zurrtum.create.content.decoration.placard.PlacardBlock;
import com.zurrtum.create.content.equipment.armor.BacktankBlock;
import com.zurrtum.create.content.equipment.armor.BacktankBlockEntity;
import com.zurrtum.create.content.equipment.blueprint.BlueprintEntity;
import com.zurrtum.create.content.fluids.tank.FluidTankBlockEntity;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlock;
import com.zurrtum.create.content.processing.burner.BlazeBurnerBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.overlay.RayTracing;


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
    public static final Identifier CONTRAPTION_EXACT_BLOCK = Identifier.fromNamespaceAndPath(ID, "exact_block");



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
        registration.addConfig(CreatePlugin.CONTRAPTION_EXACT_BLOCK, true);
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
        registration.registerEntityComponent(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);
        registration.registerEntityIcon(ContraptionExactBlockProvider.INSTANCE, AbstractContraptionEntity.class);

//        EntityType.byString("create:super_glue").ifPresent(registration::hideTarget);



        RayTracing.INSTANCE.entityFilter = RayTracing.INSTANCE.entityFilter.and(e -> {
            if (!(e instanceof AbstractContraptionEntity contraptionEntity)) return true;
            Minecraft mc = Minecraft.getInstance();
            Entity camera = mc.getCameraEntity();
            if (camera == null) return true;
            float pt = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            Vec3 origin = camera.getEyePosition(pt);
            Vec3 lookVector = camera.getViewVector(pt);
            float reach = (float)(mc.player.blockInteractionRange() + IWailaConfig.get().general().getExtendedReach());
            Vec3 target = origin.add(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
            Vec3 localOrigin = contraptionEntity.toLocalVector(origin, 1);
            Vec3 localTarget = contraptionEntity.toLocalVector(target, 1);
            Contraption contraption = contraptionEntity.getContraption();
            RaycastHelper.PredicateTraceResult predicateResult = RaycastHelper.rayTraceUntil(localOrigin, localTarget, p -> {
                StructureBlockInfo blockInfo = contraption.getBlocks().get(p);
                if (blockInfo == null) return false;
                BlockState state = blockInfo.state();
                VoxelShape shape = state.getShape(mc.level, BlockPos.ZERO);
                if (shape.isEmpty()) return false;
                BlockHitResult rayTrace = shape.clip(localOrigin, localTarget, p);
                if (IWailaConfig.get().plugin().get(CreatePlugin.CONTRAPTION_EXACT_BLOCK) && rayTrace != null && rayTrace.getType() != Type.MISS) {
                    BlockAccessor originalAccessor = registration.blockAccessor().blockState(state).hit(rayTrace).build();
                    Accessor<?> accessor = originalAccessor;
                    for (JadeRayTraceCallback callback : WailaClientRegistration.instance().rayTraceCallback.callbacks()) {
                        accessor = callback.onRayTrace(rayTrace, accessor, originalAccessor);
                    }
                    if (accessor != null) {
                        ContraptionExactBlockProvider.INSTANCE.setHit(contraptionEntity, accessor);
                    }
                }
                return rayTrace != null;
            });
            return predicateResult != null && !predicateResult.missed();
        });
    }

}