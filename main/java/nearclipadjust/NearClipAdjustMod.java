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
        config.get(Configuration.CATEGORY_CLIENT, "isOpenClip", true, "Is open clip");
        config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f, "The near clip distance");
        config.get(Configuration.CATEGORY_CLIENT, "farClipDistance", 1f, "The far clip distance");
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
        Op_setCommand.init(event);
    }

}