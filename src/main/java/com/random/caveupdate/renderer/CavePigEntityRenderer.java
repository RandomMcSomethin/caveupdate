package com.random.caveupdate.renderer;

import com.random.caveupdate.entities.CavePigEntity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;

public class CavePigEntityRenderer extends MobEntityRenderer<CavePigEntity, PigEntityModel<CavePigEntity>> {

	   public CavePigEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
	      super(entityRenderDispatcher, new PigEntityModel<CavePigEntity>(), 0.7F);
	      this.addFeature(new SaddleFeatureRenderer(this, new PigEntityModel(0.5F), new Identifier("textures/entity/pig/pig_saddle.png")));
	   }

	   @Override
	   public Identifier getTexture(CavePigEntity pigEntity) {
	      return new Identifier("caveupdate", "textures/entity/cave_pig.png");
	   }
	}
