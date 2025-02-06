package nearclipadjust;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraftforge.common.config.Configuration;

import java.io.IOException;

public class NearClipAdjustCustomGui extends GuiScreen {

    private GuiSlider nearClipSlider;
    private final GuiScreen parentScreen;

    public NearClipAdjustCustomGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        // 添加滑块控件
        nearClipSlider = new GuiSlider(
                new GuiPageButtonList.GuiResponder() {
                    @Override
                    public void setEntryValue(int id, boolean value) {
                        // 不需要实现
                    }

                    @Override
                    public void setEntryValue(int id, float value) {
                        // 当滑块值变化时调用
                        //NearClipAdjustMod.updateNearClipDistance(value); // 调用函数
                        NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f)
                                .set(value); // 更新配置
                    }

                    @Override
                    public void setEntryValue(int id, String value) {
                        // 不需要实现
                    }
                },
                0, // 控件 ID
                this.width / 2 - 100, // x 坐标
                this.height / 2 - 30, // y 坐标
                "Near Clip Distance", // 滑块名称
                0.001f, // 最小值
                0.1f, // 最大值
                (float) NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f).getDouble(), // 当前值
                new GuiSlider.FormatHelper() {
                    @Override
                    public String getText(int id, String name, float value) {
                        return name + ": " + String.format("%.3f", value); // 显示滑块名称和值，保留两位小数
                    }
                }
        );

        this.buttonList.add(nearClipSlider);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        // 不需要额外处理，因为 GuiResponder 已经处理了滑块值变化
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground(); // 绘制默认背景
        super.drawScreen(mouseX, mouseY, partialTicks); // 绘制控件
    }

    @Override
    public void onGuiClosed() {
        // 当 GUI 关闭时，保存配置
        NearClipAdjustMod.config.save();
    }
}