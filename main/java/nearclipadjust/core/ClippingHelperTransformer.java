package nearclipadjust.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ClippingHelperTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.client.renderer.EntityRenderer".equals(transformedName)) {
            return patchClippingHelper(basicClass);
        }
        return basicClass;
    }

    private byte[] patchClippingHelper(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        // 遍历方法，找到 isBoxInFrustum
        for (MethodNode method : classNode.methods) {
            if ("a".equals(method.name) && "(IFJ)V".equals(method.desc)) {
                //System.out.println("CHANGEDD");
                //for (AbstractInsnNode instruction : method.instructions.toArray()) {
                    // 找到目标指令：INVOKEINTERFACE 或 INVOKEVIRTUAL
                    // if (instruction.getOpcode() == Opcodes.INVOKEINTERFACE || instruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                    //     MethodInsnNode methodInsn = (MethodInsnNode) instruction;
    
                    //     // 检查方法名和描述符
                    //     if ("gluPerspective".equals(methodInsn.name)) {
                            // 创建新的指令列表
    
                            // // 插入 System.out.println("Position set!")
                            // newInstructions.add(new MethodInsnNode(
                            //     Opcodes.INVOKESTATIC,
                            //     "nearclipadjust/core/ClippingHelperUtil",
                            //     "customIsBoxInFrustum",
                            //     "(IF)V",
                            //     false
                            // ));
    
                            // // 在目标指令之后插入新指令
                            // method.instructions.insert(instruction, newInstructions);

                            
                            // 清空方法体
                            //method.instructions.clear();
                            //System.out.println("DESC:method.desc");

                            // 调用自定义逻辑
                            // for (int i =20;i<500;i=i+35)
                            // {    
                            //     InsnList newInstructions = new InsnList();
                            //     newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载 this
                            //     //newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 1)); // 加载 minX
                            //     newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 2)); // 加载 minY
                            //     //newInstructions.add(new VarInsnNode(Opcodes.LLOAD, 3)); // 加载 minZ
                            //     // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 7)); // 加载 maxX
                            //     // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 9)); // 加载 maxY
                            //     // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 11)); // 加载 maxZ
                            //     // method.instructions.insertBefore(
                            //     //     method.instructions.get(15),
                            //     //     new MethodInsnNode(
                            //     //         Opcodes.INVOKESTATIC,
                            //     //         "nearclipadjust/core/ClippingHelperUtil",
                            //     //         "customIsBoxInFrustum",
                            //     //         "()V",
                            //     //         false
                            //     // ));
                            //     newInstructions.add(
                            //         new MethodInsnNode(
                            //             Opcodes.INVOKESTATIC,
                            //             "nearclipadjust/core/ClippingHelperUtil",
                            //             "customIsBoxInFrustum",
                            //             "(Lnet/minecraft/client/renderer/EntityRenderer;F)V",
                            //             false
                            //     ));

                            //     method.instructions.insert(
                            //         method.instructions.get(i),
                            //         newInstructions
                            //         );}
                                    InsnList newInstructions = new InsnList();
                                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载 this
                                    //newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 1)); // 加载 minX
                                    newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 2)); // 加载 minY
                                    //newInstructions.add(new VarInsnNode(Opcodes.LLOAD, 3)); // 加载 minZ
                                    // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 7)); // 加载 maxX
                                    // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 9)); // 加载 maxY
                                    // method.instructions.add(new VarInsnNode(Opcodes.DLOAD, 11)); // 加载 maxZ
                                    // method.instructions.insertBefore(
                                    //     method.instructions.get(15),
                                    //     new MethodInsnNode(
                                    //         Opcodes.INVOKESTATIC,
                                    //         "nearclipadjust/core/ClippingHelperUtil",
                                    //         "customIsBoxInFrustum",
                                    //         "()V",
                                    //         false
                                    // ));
                                    newInstructions.add(
                                        new MethodInsnNode(
                                            Opcodes.INVOKESTATIC,
                                            "nearclipadjust/core/ClippingHelperUtil",
                                            "customIsBoxInFrustum",
                                            "(Lnet/minecraft/client/renderer/EntityRenderer;F)V",
                                            false
                                    ));
    
                                    method.instructions.insert(
                                        //method.instructions.get(20+(35*8)),   //没有Optifine
                                        method.instructions.get(20+(35*13)),   //有Optifine
                                        newInstructions
                                        );

                            break;
                    //     }
                    // }
                //}
            }
        }

        // 将修改后的类写回字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}