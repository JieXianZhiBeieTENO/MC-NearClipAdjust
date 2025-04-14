package nearclipadjust;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class NearClipAdjustGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraft) {
        // 初始化代码（如果需要）
    }

    @Override
    public boolean hasConfigGui() {
        return true; // 表示此模组有配置 GUI
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new NearClipAdjustCustomGui(parentScreen); // 返回自定义 GUI
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null; // 通常返回 null，除非你需要运行时 GUI 分类
    }
}