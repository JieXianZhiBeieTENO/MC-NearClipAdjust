package nearclipadjust;

import net.minecraftforge.common.config.Configuration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class Op_setCommand extends CommandBase {

    @Override
    public String getName() {
        return "setclip"; // 指令名称
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setclip <number>"; // 指令用法
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 检查参数数量
        if (args.length < 1) {
            throw new CommandException("Usage: /setclip <number>");
        }

        // 获取参数并转换为整数
        double number;
        try {
            number = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid number: " + args[0]);
        }

        if (number > 0.1d){
            throw new CommandException("输入数字不得大于0.1");
        } else if(number < 0.001d){
            throw new CommandException("输入数字不得小于0.001");
        }

        NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "nearClipDistance", 0.05f, "The near clip distance").set(number);

        // 在聊天面板中打印消息
        sender.sendMessage(new TextComponentString("设置近裁切: " + number));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 指令权限等级（0 为所有玩家可用）
    }
}