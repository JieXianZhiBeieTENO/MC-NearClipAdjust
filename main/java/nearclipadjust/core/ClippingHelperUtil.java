package nearclipadjust.core;

import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.util.glu.Project;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
//import com.mojang.authlib.properties.Property;
import net.minecraft.client.renderer.EntityRenderer;
import nearclipadjust.NearClipAdjustMod;
import net.minecraftforge.common.config.Configuration;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

// import org.lwjgl.Sys;
// import org.lwjgl.opengl.GL20;
// import org.lwjgl.opengl.GL11;
// import org.lwjgl.opengl.ARBShaderObjects;
import net.optifine.shaders.uniform.ShaderUniform1f;
import net.optifine.shaders.uniform.ShaderUniformM4;
// import net.optifine.shaders.Program;
// import java.util.Arrays;
// import org.joml.Matrix4f;


public class ClippingHelperUtil {
    public static boolean GetConfigValue(String name, boolean defaultValue){
        return NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, name, defaultValue).getBoolean();
    }
    public static float GetConfigValue(String name, float defaultValue){
        return (float)NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, name, defaultValue).getDouble();
    }
    public static float getTrueFarClip(){
        return projection_var[3]*GetConfigValue("farClipDistance", 1f);
    }
    public static void EntityRenderer_NearClip(EntityRenderer instance, float partialTicks)
    {
        if (!GetConfigValue("isOpenClip", false)) return;

        Minecraft mc = Minecraft.getMinecraft();
        Method getFOVModifier = ReflectionUtil.getMethod(
            instance,
            "func_78481_a",
            new Class<?>[]{float.class, boolean.class}
        );
        float farPlaneDistance = (float)ReflectionUtil.getFieldValue(
            instance,
            "field_78530_s",
            false
        );

        float zfar = farPlaneDistance * MathHelper.SQRT_2 ;
        projection_var[3] = zfar;

        float znear = GetConfigValue("nearClipDistance", 0.05f);

        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)ReflectionUtil.realizeMethod(getFOVModifier, instance, partialTicks, true), (float)mc.displayWidth / (float)mc.displayHeight, znear, getTrueFarClip());
        GlStateManager.matrixMode(5888);

    }
    public static String floatBufferToString(FloatBuffer intBuffer_c) {
        FloatBuffer intBuffer = intBuffer_c.duplicate();
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        while (intBuffer.hasRemaining()) {
            sb.append(intBuffer.get());
            if (intBuffer.hasRemaining()) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    public static void SetUniformNearFar(ShaderUniform1f unif, float value){
        
        if (!GetConfigValue("isOpenClip", false)) return;
        
        String name = (String)ReflectionUtil.getFieldValue(
            unif,
            "name",
            true
        );

        if (name.equals("near")){
            float znear = GetConfigValue("nearClipDistance", 0.05f);
            unif.setValue(znear);
        } else if (name.equals("far")){
            unif.setValue(getTrueFarClip());
            //System.out.println(Float.toString(unif.getValue()));
        }
    }
    
    public static float getValue(FloatBuffer matrix, int row, int col){
        return matrix.get(row*4+col);
    }
    public static void setValue(FloatBuffer matrix, int row, int col, float value){
        matrix.put(row*4+col, value);
    }
    public static void setValue(FloatBuffer matrix, int row, int col, double value){
        matrix.put(row*4+col, (float)value);
    }

    public static float[] projection_var = {0,0,0,0};
    public static float fma(float a, float b, float c){
        return a*b+c;
    }
    public static FloatBuffer invert(FloatBuffer matrix) {
        float m00 = getValue(matrix, 0, 0);
        float m01 = getValue(matrix, 0, 1);
        float m02 = getValue(matrix, 0, 2);
        float m03 = getValue(matrix, 0, 3);
        float m10 = getValue(matrix, 1, 0);
        float m11 = getValue(matrix, 1, 1);
        float m12 = getValue(matrix, 1, 2);
        float m13 = getValue(matrix, 1, 3);
        float m20 = getValue(matrix, 2, 0);
        float m21 = getValue(matrix, 2, 1);
        float m22 = getValue(matrix, 2, 2);
        float m23 = getValue(matrix, 2, 3);
        float m30 = getValue(matrix, 3, 0);
        float m31 = getValue(matrix, 3, 1);
        float m32 = getValue(matrix, 3, 2);
        float m33 = getValue(matrix, 3, 3);
        float a = m00 * m11 - m01 * m10;
        float b = m00 * m12 - m02 * m10;
        float c = m00 * m13 - m03 * m10;
        float d = m01 * m12 - m02 * m11;
        float e = m01 * m13 - m03 * m11;
        float f = m02 * m13 - m03 * m12;
        float g = m20 * m31 - m21 * m30;
        float h = m20 * m32 - m22 * m30;
        float i = m20 * m33 - m23 * m30;
        float j = m21 * m32 - m22 * m31;
        float k = m21 * m33 - m23 * m31;
        float l = m22 * m33 - m23 * m32;
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        float nm00 = fma( m11, l, fma(-m12, k,  m13 * j)) * det;
        float nm01 = fma(-m01, l, fma( m02, k, -m03 * j)) * det;
        float nm02 = fma( m31, f, fma(-m32, e,  m33 * d)) * det;
        float nm03 = fma(-m21, f, fma( m22, e, -m23 * d)) * det;
        float nm10 = fma(-m10, l, fma( m12, i, -m13 * h)) * det;
        float nm11 = fma( m00, l, fma(-m02, i,  m03 * h)) * det;
        float nm12 = fma(-m30, f, fma( m32, c, -m33 * b)) * det;
        float nm13 = fma( m20, f, fma(-m22, c,  m23 * b)) * det;
        float nm20 = fma( m10, k, fma(-m11, i,  m13 * g)) * det;
        float nm21 = fma(-m00, k, fma( m01, i, -m03 * g)) * det;
        float nm22 = fma( m30, e, fma(-m31, c,  m33 * a)) * det;
        float nm23 = fma(-m20, e, fma( m21, c, -m23 * a)) * det;
        float nm30 = fma(-m10, j, fma( m11, h, -m12 * g)) * det;
        float nm31 = fma( m00, j, fma(-m01, h,  m02 * g)) * det;
        float nm32 = fma(-m30, d, fma( m31, b, -m32 * a)) * det;
        float nm33 = fma( m20, d, fma(-m21, b,  m22 * a)) * det;
        setValue(matrix, 0, 0, nm00);
        setValue(matrix, 0, 1, nm01);
        setValue(matrix, 0, 2, nm02);
        setValue(matrix, 0, 3, nm03);
        setValue(matrix, 1, 0, nm10);
        setValue(matrix, 1, 1, nm11);
        setValue(matrix, 1, 2, nm12);
        setValue(matrix, 1, 3, nm13);
        setValue(matrix, 2, 0, nm20);
        setValue(matrix, 2, 1, nm21);
        setValue(matrix, 2, 2, nm22);
        setValue(matrix, 2, 3, nm23);
        setValue(matrix, 3, 0, nm30);
        setValue(matrix, 3, 1, nm31);
        setValue(matrix, 3, 2, nm32);
        setValue(matrix, 3, 3, nm33);
        return matrix;
    }
    public static FloatBuffer perspective(float fovY, float aspect, float znear, float zfar) {
        FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
        setValue(matrix, 0, 0, 1/(aspect*Math.tan(fovY/2)));
        setValue(matrix, 1, 1, 1/(Math.tan(fovY/2)));
        setValue(matrix, 2, 2, -(zfar+znear)/(zfar-znear));
        setValue(matrix, 3, 2, -(2*zfar*znear)/(zfar-znear));
        setValue(matrix, 2, 3, -1);
        return matrix;
    }
    public static void SetUniformProjection(ShaderUniformM4 instance, int transpose_int, FloatBuffer matrix_clone){
        
        if (!GetConfigValue("isOpenClip", false)) return;

        String name = (String)ReflectionUtil.getFieldValue(
            instance,
        "name",
        true
        );

        if (name.equals("gbufferProjection") || name.equals("gbufferProjectionInverse")){

            FloatBuffer matrix = matrix_clone.duplicate();
            boolean transpose = transpose_int==1 ? true : false;

            if (name.equals("gbufferProjection")){

                float aspect = getValue(matrix, 1, 1) / getValue(matrix, 0, 0);
                float znear = GetConfigValue("nearClipDistance", 0.05f);
                //float zNear = getValue(matrix, 3, 2) / (getValue(matrix, 2, 2) - 1);
                //float zFar = getValue(matrix, 3, 2) / (getValue(matrix, 2, 2) + 1);
                float fovY = (float)(2 * Math.atan(1 / getValue(matrix, 1, 1)));

                projection_var[0] = fovY;
                projection_var[1] = aspect;
                projection_var[2] = znear;
                //projection_var[3] = zFar;
            }

            FloatBuffer projectionMatrix = perspective(projection_var[0],projection_var[1],projection_var[2],getTrueFarClip());

            if (name.equals("gbufferProjection")){
                instance.setValue(transpose, projectionMatrix);
            } else if (name.equals("gbufferProjectionInverse")){
                instance.setValue(transpose, invert(projectionMatrix));
            }

            //System.out.println(floatBufferToString(matrix_clone) + "  " + floatBufferToString(projectionMatrix));

        }

    }
}
