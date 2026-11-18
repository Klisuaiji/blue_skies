var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

function initializeCoreMod() {
	return {
		'render': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.gui.components.BossHealthOverlay',
				'methodName': 'm_93704_',
				'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;)V'
			},
			'transformer': render
		}
	}	
}

function render(method) {
	var instructions = method.instructions;
	var insn = instructions.get(instructions.size() - 2);

    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/gui/components/BossHealthOverlay', ASMAPI.mapField('f_93698_'), 'Lnet/minecraft/client/Minecraft;'));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/gui/components/BossHealthOverlay', ASMAPI.mapField('f_93699_'), 'Ljava/util/Map;'));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/BossHealthOverlayHooks', 'render', '(Lnet/minecraft/client/Minecraft;Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;)V'));

	return method;
}