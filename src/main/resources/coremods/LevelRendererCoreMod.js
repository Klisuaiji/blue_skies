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
		'getLightColor': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.LevelRenderer',
				'methodName': 'm_109537_',
				'methodDesc': '(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I'
			},
			'transformer': getLightColor
		},
		'renderSky': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.LevelRenderer',
				'methodName': 'm_181409_',
				'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FLjava/lang/Runnable;)V'
			},
			'transformer': renderSky
		}
	}	
}

function getLightColor(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/LevelRendererHooks', 'isEmissive', '(Lnet/minecraft/world/level/block/state/BlockState;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/LevelRendererHooks', 'getMaxLight', '()I'));
	instructions.insertBefore(insn, new InsnNode(Opcodes.IRETURN));

	instructions.insertBefore(insn, label);

	return method;
}

function renderSky(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/LevelRenderer', ASMAPI.mapField('f_109477_'), 'I'));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.FLOAD, 3));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/LevelRenderer', ASMAPI.mapField('f_109465_'), 'Lnet/minecraft/client/multiplayer/ClientLevel;'));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/LevelRenderer', ASMAPI.mapField('f_109461_'), 'Lnet/minecraft/client/Minecraft;'));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 2));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 4));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/LevelRendererHooks', 'shouldRenderSky', '(IFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Minecraft;Lcom/mojang/math/Matrix4f;Ljava/lang/Runnable;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.RETURN));

	instructions.insertBefore(insn, label);

	return method;
}