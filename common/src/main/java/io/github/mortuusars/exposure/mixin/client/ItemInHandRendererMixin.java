package io.github.mortuusars.exposure.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.mortuusars.exposure.ExposureClient;
import io.github.mortuusars.exposure.client.camera.CameraClient;
import io.github.mortuusars.exposure.world.item.PhotographItem;
import io.github.mortuusars.exposure.world.item.StackedPhotographsItem;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow
    protected abstract void renderPlayerArm(PoseStack pMatrixStack, SubmitNodeCollector pBuffer, int pCombinedLight, float pEquippedProgress, float pSwingProgress, HumanoidArm pSide, PlayerRenderState playerState);
    @Shadow
    protected abstract float calculateMapTilt(float pPitch);
    @Shadow
    protected abstract void renderMapHand(PoseStack pMatrixStack, SubmitNodeCollector pBuffer, int pCombinedLight, HumanoidArm pSide, PlayerRenderState playerState);

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0),
            cancellable = true)
    private void renderPhotograph(PlayerRenderState player, FirstPersonHandsAndItemsRenderState state,
                                  float partialTicks, float pitch, InteractionHand hand,
                                  float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack,
                                  SubmitNodeCollector buffer, int combinedLight, CallbackInfo ci, @Local boolean isMainHand, @Local HumanoidArm arm) {
        if (CameraClient.viewfinder() != null && CameraClient.viewfinder().isLookingThrough()) {
            poseStack.popPose();
            ci.cancel();
            return;
        }

        if (stack.getItem() instanceof PhotographItem || stack.getItem() instanceof StackedPhotographsItem) {
            if (isMainHand && state.offHandItem.isEmpty()) {
                exposure$renderTwoHandedPhotograph(player, poseStack, buffer, combinedLight, pitch, equipProgress, swingProgress, stack);
            } else {
                exposure$renderOneHandedPhotograph(player, poseStack, buffer, combinedLight, equipProgress, arm, swingProgress, stack);
            }

            poseStack.popPose();

            ci.cancel();
        }
    }

    @Unique
    private void exposure$renderOneHandedPhotograph(PlayerRenderState player, PoseStack poseStack, SubmitNodeCollector buffer, int packedLight, float pEquippedProgress, HumanoidArm pHand, float pSwingProgress, ItemStack stack) {
        float f = pHand == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate(f * 0.125F, -0.125D, 0.0D);
        if (player.avatarRenderState != null && !player.avatarRenderState.isInvisible) {
            poseStack.pushPose();
            poseStack.rotate(Axis.ZP.rotationDegrees(f * 10.0F));
            this.renderPlayerArm(poseStack, buffer, packedLight, pEquippedProgress, pSwingProgress, pHand, player);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(f * 0.51F, -0.08F + pEquippedProgress * -1.2F, -0.75D);
        float f1 = Mth.sqrt(pSwingProgress);
        float f2 = Mth.sin(f1 * (float)Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f5 = -0.3F * Mth.sin(pSwingProgress * (float)Math.PI);
        poseStack.translate(f * f3, f4 - 0.3F * f2, f5);
        poseStack.rotate(Axis.XP.rotationDegrees(f2 * -45.0F));
        poseStack.rotate(Axis.YP.rotationDegrees(f * f2 * -30.0F));

        poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
        poseStack.rotate(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(0.38f, 0.38f, 0.38f);
        poseStack.translate(-0.5, -0.5, 0);
        poseStack.scale(1f, 1f, -1f);

        ExposureClient.photographRenderer().render(stack, true, false, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

    @Unique
    private void exposure$renderTwoHandedPhotograph(PlayerRenderState player, PoseStack poseStack, SubmitNodeCollector buffer, int packedLight, float pitch, float equippedProgress, float swingProgress, ItemStack stack) {
        float f = Mth.sqrt(swingProgress);
        float f1 = -0.2F * Mth.sin(swingProgress * (float)Math.PI);
        float f2 = -0.4F * Mth.sin(f * (float)Math.PI);
        poseStack.translate(0.0D, -f1 / 2.0F, f2);
        float f3 = this.calculateMapTilt(pitch);
        poseStack.translate(0.0D, 0.04F + equippedProgress * -1.2F + f3 * -0.5F, -0.72F);
        poseStack.rotate(Axis.XP.rotationDegrees(f3 * -85.0F));
        if (player.avatarRenderState != null && !player.avatarRenderState.isInvisible) {
            poseStack.pushPose();
            poseStack.rotate(Axis.YP.rotationDegrees(90.0F));
            this.renderMapHand(poseStack, buffer, packedLight, HumanoidArm.RIGHT, player);
            this.renderMapHand(poseStack, buffer, packedLight, HumanoidArm.LEFT, player);
            poseStack.popPose();
        }

        float f4 = Mth.sin(f * (float)Math.PI);
        poseStack.rotate(Axis.XP.rotationDegrees(f4 * 20.0F));
        poseStack.scale(2.0F, 2.0F, 2.0F);

        poseStack.rotate(Axis.YP.rotationDegrees(180.0F));
        poseStack.rotate(Axis.ZP.rotationDegrees(180.0F));
        poseStack.scale(0.38f, 0.38f, 0.38f);
        poseStack.translate(-0.5, -0.5, 0);
        poseStack.scale(1f, 1f, -1f);

        ExposureClient.photographRenderer().render(stack, true, false, poseStack, buffer, packedLight);
    }
}
