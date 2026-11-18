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
		'attachsTo': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.level.block.IronBarsBlock',
				'methodName': 'm_54217_',
				'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Z)Z'
			},
			'transformer': attachsTo
		}
	}	
}

function attachsTo(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/IronBarsBlockHooks', 'shouldAttach', '(Lnet/minecraft/world/level/block/state/BlockState;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.ICONST_1));
	instructions.insertBefore(insn, new InsnNode(Opcodes.IRETURN));

	instructions.insertBefore(insn, label);

	return method;
}