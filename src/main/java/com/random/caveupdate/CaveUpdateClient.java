package com.random.caveupdate;

import com.random.caveupdate.renderer.CavePigEntityRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class CaveUpdateClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.ROOTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.CAVE_CARROTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.PLUMP_HELMETS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.PLUMP_HELMET_WILD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.DIMPLE_CAPS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.CAVE_REEDS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.CAVE_REED_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CaveUpdate.CAVE_REED_TRAPDOOR, RenderLayer.getCutout());
		
        EntityRendererRegistry.INSTANCE.register(CaveUpdate.CAVE_PIG, (dispatcher, context) -> {
            return new CavePigEntityRenderer(dispatcher);
        });
	}

}
