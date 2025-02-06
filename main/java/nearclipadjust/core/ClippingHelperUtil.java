package nearclipadjust.core;

import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.util.glu.Project;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.EntityRenderer;
import nearclipadjust.NearClipAdjustMod;
import net.minecraftforge.common.config.Configuration;

import java.lang.reflect.Method;

public class ClippingHelperUtil {
    public static int co=0;
    public static void customIsBoxInFrustum(EntityRenderer instance, float partialTicks)
    {

        Minecraft mc = Minecraft.getMinecraft();
        Method getFOVModifier = ReflectionUtil.Method(
            instance,
            "func_78481_a",
            new Class<?>[]{float.class, boolean.class}
        );
        float farPlaneDistance = (float)ReflectionUtil.getFieldValue(
            instance,
            "field_78530_s"
        );

        float znear = (float)NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f).getDouble();

        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)ReflectionUtil.realizeMethod(getFOVModifier, instance, partialTicks, true), (float)mc.displayWidth / (float)mc.displayHeight, znear, farPlaneDistance * MathHelper.SQRT_2);
        GlStateManager.matrixMode(5888);
        if (co==0)
            System.out.println("operating"+co);
            co++;

    }
}
