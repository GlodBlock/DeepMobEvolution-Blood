package com.glodblock.github.dmeblood.coremod.transform;

import com.glodblock.github.dmeblood.coremod.CoreModHooker;
import com.glodblock.github.dmeblood.coremod.DBEClassTransformer;
import net.minecraft.entity.MultiPartEntityPart;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Fix tinker tools damage doesn't apply to {@link MultiPartEntityPart}
 */
public class ToolHelperTransformer extends DBEClassTransformer.ClassMapper {

    public static final ToolHelperTransformer INSTANCE = new ToolHelperTransformer();

    private ToolHelperTransformer() {
        // NO-OP
    }

    @Override
    protected ClassVisitor getClassMapper(ClassVisitor downstream) {
        return new TransformToolHelper(Opcodes.ASM5, downstream);
    }

    private static class TransformToolHelper extends ClassVisitor {

        TransformToolHelper(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals("attackEntity") && desc.equals("(Lnet/minecraft/item/ItemStack;Lslimeknights/tconstruct/library/tools/ToolCore;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Z)Z")) {
                return new TransformAttackEntity(api, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

    private static class TransformAttackEntity extends MethodVisitor {

        private int null_cnt;

        TransformAttackEntity(int api, MethodVisitor mv) {
            super(api, mv);
            this.null_cnt = 0;
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ACONST_NULL) {
                this.null_cnt ++;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            super.visitVarInsn(opcode, var);
            if (this.null_cnt == 2 && opcode == Opcodes.ASTORE) {
                this.null_cnt ++;
                super.visitVarInsn(Opcodes.ALOAD, 3);
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        CoreModHooker.OWNER,
                        "getEntityBase",
                        "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/EntityLivingBase;",
                        false);
                super.visitVarInsn(Opcodes.ASTORE, 7);
            }
        }

    }

}
