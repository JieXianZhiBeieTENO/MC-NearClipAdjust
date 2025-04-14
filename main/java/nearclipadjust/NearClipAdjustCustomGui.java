package nearclipadjust;

import net.minecraftforge.common.config.Property;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraftforge.common.config.Configuration;

import java.io.IOException;

public class NearClipAdjustCustomGui extends GuiScreen {

    private GuiSlider nearClipSlider;
    private GuiSlider farClipSlider;
    private static Property isOpenClip = NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "isOpenClip", false);
    private final GuiScreen parentScreen;

    public NearClipAdjustCustomGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        int small_gap = 3+20;
        int big_gap = 14;
        int width = (this.width / 2) - 75;
        int height = (this.height / 2) - 50;
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
                1, // 控件 ID
                width, // x 坐标
                height+small_gap+big_gap, // y 坐标
                "近裁切距离", // 滑块名称
                0.001f, // 最小值
                0.1f, // 最大值
                (float) NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f).getDouble(), // 当前值
                new GuiSlider.FormatHelper() {
                    @Override
                    public String getText(int id, String name, float value) {
                        return name + ": " + String.format("%.3f", value);
                    }
                }
        );


        farClipSlider = new GuiSlider(
                new GuiPageButtonList.GuiResponder() {
                    @Override
                    public void setEntryValue(int id, boolean value) {
                        // 不需要实现
                    }

                    @Override
                    public void setEntryValue(int id, float value) {
                        // 当滑块值变化时调用
                        //NearClipAdjustMod.updateNearClipDistance(value); // 调用函数
                        NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "farClipDistance", 1f)
                                .set(value); // 更新配置
                    }

                    @Override
                    public void setEntryValue(int id, String value) {
                        // 不需要实现
                    }
                },
                3, // 控件 ID
                width, // x 坐标
                height+(small_gap*2)+big_gap, // y 坐标
                "远裁切距离（相乘）", // 滑块名称
                0.001f, // 最小值
                2f, // 最大值
                (float) NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "farClipDistance", 1f).getDouble(), // 当前值
                new GuiSlider.FormatHelper() {
                    @Override
                    public String getText(int id, String name, float value) {
                        return name + ": " + String.format("%.3f", value);
                    }
                }
        );

        this.buttonList.add(new GuiButton(0, width, height, 150, 20, "裁切调整"+": "+(isOpenClip.getBoolean() ? "是" : "否")));
        this.buttonList.add(nearClipSlider);
        this.buttonList.add(farClipSlider);

    }

    private void setValue(GuiButton button, Property value){
        value.set(!value.getBoolean());
        button.displayString = button.displayString.substring(0,button.displayString.length()-3)+": "+(value.getBoolean() ? "是" : "否");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case 0:
                setValue(button, isOpenClip);
                break;
            default:
                break;
        }
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
