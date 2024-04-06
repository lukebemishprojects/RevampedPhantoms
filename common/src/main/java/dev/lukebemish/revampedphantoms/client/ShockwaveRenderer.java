package dev.lukebemish.revampedphantoms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.entity.Shockwave;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;


public class ShockwaveRenderer extends EntityRenderer<Shockwave> {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(RevampedPhantoms.MOD_ID,"textures/entity/phantom/shockwave.png");
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

	public ShockwaveRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected int getBlockLightLevel(Shockwave entity, BlockPos pos) {
		return 15;
	}

	@Override
	public void render(Shockwave entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		matrixStack.pushPose();
		float scale = entity.getDistFromInitial();
		matrixStack.scale(scale, scale, scale);
		matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		matrixStack.mulPose(new Quaternionf(new AxisAngle4f((float) Math.PI,  0, 1f, 0)));
		PoseStack.Pose pose = matrixStack.last();
		Matrix4f matrix4f = pose.pose();
		Matrix3f matrix3f = pose.normal();
		VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
		ShockwaveRenderer.vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 0.0f, 0, 0, 1);
		ShockwaveRenderer.vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 1.0f, 0, 1, 1);
		ShockwaveRenderer.vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 1.0f, 1, 1, 0);
		ShockwaveRenderer.vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 0.0f, 1, 0, 0);
		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
	}

	private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, int k, int l) {
		vertexConsumer.vertex(matrix4f, f - 0.5f, (float)j - 0.25f, 0.0f).color(255, 255, 255, 255).uv(k, l).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(matrix3f, 0.0f, 1.0f, 0.0f).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(Shockwave entity) {
		return TEXTURE_LOCATION;
	}
}
