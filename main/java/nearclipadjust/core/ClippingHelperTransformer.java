package nearclipadjust.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;

public class ClippingHelperTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        //System.out.println(name +"    "+ transformedName);
        if ("net.minecraft.client.renderer.EntityRenderer".equals(transformedName)) {
            return patchEntityRendererHelper(basicClass);
        }
        else if ("net.optifine.shaders.Shaders".equals(transformedName)) {
            return patchSetUniformNearFarHelper(basicClass);
        }
        // if ("net.optifine.shaders.Shaders".equals(transformedName)) {
        //     return patchOptCameraShadowsHelper(basicClass);
        // }
        //  else if ("net.minecraft.client.renderer.GlStateManager".equals(transformedName)){
        //     return patchClippingHelper1(basicClass);
        // }
        return basicClass;
    }

    private byte[] patchEntityRendererHelper(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        // String[] method_needReplace = {"a (FI)V", "b (FI)V", "a (IFJ)V", "a (Lnet/minecraft/client/renderer/RenderGlobal;FIDDD)V"};
        // float[] func_count = {0,0,0,0};
        for (MethodNode method : classNode.methods) {
            if ("a".equals(method.name) && "(IFJ)V".equals(method.desc)) {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载 this
                newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 2)); // 加载 partialTicks

                newInstructions.add(
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "nearclipadjust/core/ClippingHelperUtil",
                        "EntityRenderer_NearClip",
                        "(Lnet/minecraft/client/renderer/EntityRenderer;F)V",
                        false
                ));

                method.instructions.insert(
                    //method.instructions.get(20+(35*8)),   //没有Optifine
                    method.instructions.get(20+(35*13)),   //有Optifine
                    newInstructions
                    );
                break;
            }
            // if (!Arrays.asList(method_needReplace).contains(method.name+" "+method.desc)) continue;
            // for (AbstractInsnNode insn : method.instructions) {
            //     if (insn.getType() == AbstractInsnNode.METHOD_INSN && ("gluPerspective".equals(method.name) && "(FFFF)V".equals(method.desc))){
            //         // MethodInsnNode methodInsn = (MethodInsnNode) insn;
            //         // System.out.println("  Calls: " + 
            //         //                 //methodInsn.owner + "." + 
            //         //                 methodInsn.name + "  " +
            //         //                 methodInsn.desc);
            //         InsnList newInstructions = new InsnList();
            //         newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // 加载 this

            //         int partialTicks_location = 0;
            //         int type1 = 0;
            //         int type2 = 1;
            //         switch (method.name+" "+method.desc){
            //             case "a (FI)V":
            //                 partialTicks_location = 1;
            //                 type1 = 1;
            //                 //func_count[0] += 1;
            //                 break;
            //             case "b (FI)V":
            //                 partialTicks_location = 1;
            //                 type1 = 2;
            //                 type2 = 0;
            //                 //func_count[1] += 1;
            //                 break;
            //             case "a (IFJ)V":
            //                 partialTicks_location = 2;
            //                 if (func_count[0] == 0){
            //                     type1 = 2;
            //                 } else {
            //                     type1 = 1;
            //                 }
            //                 func_count[2] += 1;
            //                 break;
            //             case "a (Lnet/minecraft/client/renderer/RenderGlobal;FIDDD)V":
            //                 partialTicks_location = 2;
            //                 if (func_count[0] == 0){
            //                     type1 = 3;
            //                 } else {
            //                     type1 = 1;
            //                 }
            //                 func_count[3] += 1;
            //                 break;
            //         }
            //         newInstructions.add(new VarInsnNode(Opcodes.FLOAD, partialTicks_location)); // 加载 partialTicks
            //         newInstructions.add(new LdcInsnNode(type1));
            //         newInstructions.add(new LdcInsnNode(type2));

            //         newInstructions.add(
            //             new MethodInsnNode(
            //                 Opcodes.INVOKESTATIC,
            //                 "nearclipadjust/core/ClippingHelperUtil",
            //                 "EntityRenderer_NearClip",
            //                 "(Lnet/minecraft/client/renderer/EntityRenderer;FII)V",
            //                 false
            //         ));

            //         method.instructions.insert(
            //             //method.instructions.get(20+(35*8)),   //没有Optifine
            //             //method.instructions.get(20+(35*13)),   //有Optifine
            //             insn,
            //             newInstructions
            //             );
            //         //method.instructions.remove(insn);
            //         //break;
            //     }
            // }
            //}
        }

        // 将修改后的类写回字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        // String dir = "D:\\A user Mr. li\\java\\MOD script\\buq.class";
        // File file = new File(dir);
        // if (!file.exists()) {
        //     try {
        //         file.createNewFile();
        //     } catch (IOException e) {
        //         e.printStackTrace(); // 或者其他适当的错误处理
        //         return null; // 或者抛出一个自定义异常
        //     }
        // }
     
        // byte[] classBytes = classWriter.toByteArray();
     
        // try (FileOutputStream fos = new FileOutputStream(file)) {
        //     fos.write(classBytes);
        // } catch (IOException e) {
        //     e.printStackTrace(); // 或者其他适当的错误处理
        //     return null; // 或者抛出一个自定义异常
        // }
        return classWriter.toByteArray();
    }
    
    private byte[] patchSetUniformNearFarHelper(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            //System.out.println(method.name+"    "+method.desc);
            if ("setProgramUniform1f".equals(method.name) && "(Lnet/optifine/shaders/uniform/ShaderUniform1f;F)V".equals(method.desc)) {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 1));
                newInstructions.add(
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "nearclipadjust/core/ClippingHelperUtil",
                        "SetUniformNearFar",
                        "(Lnet/optifine/shaders/uniform/ShaderUniform1f;F)V",
                        false
                ));

                for (AbstractInsnNode node : method.instructions){
                    if (node.getOpcode() == Opcodes.RETURN){
                        method.instructions.insertBefore(
                            node,
                            newInstructions
                            );
                    }
                }

                //break;
            } else if ("setProgramUniformMatrix4ARB".equals(method.name) && "(Lnet/optifine/shaders/uniform/ShaderUniformM4;ZLjava/nio/FloatBuffer;)V".equals(method.desc)) {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                //newInstructions.add(new VarInsnNode(Opcodes.FLOAD, 1));
                newInstructions.add(
                    new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "nearclipadjust/core/ClippingHelperUtil",
                        "SetUniformProjection",
                        "(Lnet/optifine/shaders/uniform/ShaderUniformM4;ILjava/nio/FloatBuffer;)V",
                        false
                ));

                for (AbstractInsnNode node : method.instructions){
                    if (node.getOpcode() == Opcodes.RETURN){
                        method.instructions.insertBefore(
                            node,
                            newInstructions
                            );
                    }
                }
            }
        }

        // 将修改后的类写回字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        // String dir = "D:\\A user Mr. li\\java\\MOD script\\buq.class";
        // File file = new File(dir);
        // if (!file.exists()) {
        //     try {
        //         file.createNewFile();
        //     } catch (IOException e) {
        //         e.printStackTrace(); // 或者其他适当的错误处理
        //         return null; // 或者抛出一个自定义异常
        //     }
        // }
     
        // byte[] classBytes = classWriter.toByteArray();
     
        // try (FileOutputStream fos = new FileOutputStream(file)) {
        //     fos.write(classBytes);
        // } catch (IOException e) {
        //     e.printStackTrace(); // 或者其他适当的错误处理
        //     return null; // 或者抛出一个自定义异常
        // }
        return classWriter.toByteArray();
    }
   
}