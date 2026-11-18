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
		'getArmPose': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.renderer.LevelRenderer',
				'methodName': 'm_117794_',
				'methodDesc': '(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;'
			},
			'transformer': getArmPose
		}
	}	
}

function getArmPose(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/PlayerRendererHooks', 'useHammerPose', '(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/PlayerRendererHooks', 'getHammerPose', '()Lnet/minecraft/client/model/HumanoidModel$ArmPose;'));
	instructions.insertBefore(insn, new InsnNode(Opcodes.ARETURN));

	instructions.insertBefore(insn, label);

	return method;
}