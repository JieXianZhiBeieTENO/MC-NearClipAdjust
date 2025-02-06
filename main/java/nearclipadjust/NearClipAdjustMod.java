package nearclipadjust;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
// import net.minecraft.client.renderer.EntityRenderer;
// import net.minecraft.client.Minecraft;
// import net.minecraft.util.math.AxisAlignedBB;

import java.io.File;

//import org.lwjgl.opengl.GL11;

@Mod(modid = NearClipAdjustMod.MODID, name = "Near Clip Adjust", version = "1.0", guiFactory = "nearclipadjust.NearClipAdjustGuiFactory")
public class NearClipAdjustMod {
    public static final String MODID = "nearclipadjust";
    public static Configuration config;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        config = new Configuration(configFile);
        config.load();

        // 定义滑块的值
        config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f, "The near clip distance");
        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 进入游戏时调用函数
        //updateNearClipDistance(config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f).getDouble());
    }
    
    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        // 注册指令
        event.registerServerCommand(new Op_setCommand());
    }

    public static void updateNearClipDistance(double value) {
        // 获取 Minecraft 的 EntityRenderer
        // EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;

        // // 修改投影矩阵
        // if (renderer != null) {
            // GL11.glMatrixMode(GL11.GL_PROJECTION);
            // GL11.glLoadIdentity();
            // double aspectRatio = (double) Minecraft.getMinecraft().displayWidth / Minecraft.getMinecraft().displayHeight;
            // double fov = Math.toRadians(Minecraft.getMinecraft().gameSettings.fovSetting); // 获取当前 FOV
            // double top = value * Math.tan(fov / 2.0);
            // double bottom = -top;
            // double right = top * aspectRatio;
            // double left = -right;

            // // 设置自定义投影矩阵
            // GL11.glFrustum(left, right, bottom, top, value, 1000.0);

            // // 恢复矩阵模式
            // GL11.glMatrixMode(GL11.GL_MODELVIEW);
        //}import net.minecraft.client.renderer.GlStateManager;


        // 切换到投影矩阵
        // ClippingHelper a = new ClippingHelper();
        // System.out.println("VALUE IS: "+a.isBoxInFrustum(1,1,1,1,1,1));
        System.out.println(value);
    }
}