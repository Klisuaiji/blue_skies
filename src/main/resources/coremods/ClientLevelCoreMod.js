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
		'addParticle': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.multiplayer.ClientLevel',
				'methodName': 'm_7106_',
				'methodDesc': '(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V'
			},
			'transformer': addParticle
		}
	}	
}

function addParticle(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 2));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 4));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.DLOAD, 6));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/ClientLevelHooks', 'shouldCancelParticle', '(Lnet/minecraft/core/particles/ParticleOptions;DDDLnet/minecraft/client/multiplayer/ClientLevel;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.RETURN));

	instructions.insertBefore(insn, label);

	return method;
}