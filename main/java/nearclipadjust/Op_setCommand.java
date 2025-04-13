package nearclipadjust;

import net.minecraftforge.common.config.Configuration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Op_setCommand {
    public static void init(FMLServerStartingEvent event){
        event.registerServerCommand(new Op_setNearFarClipCommand());
    }
}

class Op_setNearFarClipCommand extends CommandBase {

    @Override
    public String getName() {
        return "setclip"; // 指令名称
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setclip near/far <number>   或   /setclip on/off"; // 指令用法
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 检查参数数量
        if (args.length < 1) {
            throw new CommandException("Usage: /setclip near/far <number>   或   /setclip on/off");
        }

            
        // 获取参数并转换为整数
        if (args[0].equals("on")){
            NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "isOpenClip", false).set(true);
            sender.sendMessage(new TextComponentString("开启裁切调整"));
        } else if (args[0].equals("off")){
            NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, "isOpenClip", false).set(false);
            sender.sendMessage(new TextComponentString("关闭裁切调整"));
        } else {

            if (!(args[0].equals("near") || args[0].equals("far"))) throw new CommandException("Invalid type: " + args[0]);
            else if (args.length < 2) throw new CommandException("Usage: /setclip near/far <number>   或   /setclip on/off");
            boolean isNearClip = args[0].equals("near");
            String farneartypeChinesechar = isNearClip ? "近" : "远" ;
            String farneartypeEnglishchar2 = isNearClip ? "nearClipDistance" : "farClipDistance" ;
            float farneartypedefaultvalue = isNearClip ? 0.05f : 1f ;

            
            double number;
            try {
                number = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                throw new CommandException("Invalid number: " + args[1]);
            }

            float min = isNearClip ? 0.001f : 0.001f;
            float max = isNearClip ? 0.1f : 2f;
            if (number > max){
                throw new CommandException("输入数字不得大于"+Float.toString(max));
            } else if(number < min){
                throw new CommandException("输入数字不得小于"+Float.toString(min));
            }

            NearClipAdjustMod.config.get(Configuration.CATEGORY_CLIENT, farneartypeEnglishchar2, farneartypedefaultvalue).set(number);

            // 在聊天面板中打印消息
            sender.sendMessage(new TextComponentString("设置"+farneartypeChinesechar+"裁切: " + number));
        }
        
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 指令权限等级（0 为所有玩家可用）
    }
}