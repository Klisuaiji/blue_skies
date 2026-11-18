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
		'wantsToStopRiding': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.player.Player',
				'methodName': 'm_36342_',
				'methodDesc': '()Z'
			},
			'transformer': wantsToStopRiding
		},
		'getDigSpeed': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.player.Player',
				'methodName': 'getDigSpeed',
				'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F'
			},
			'transformer': getDigSpeed
		}
	}	
}

function wantsToStopRiding(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/PlayerHooks', 'cannotDismount', '(Lnet/minecraft/world/entity/player/Player;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.ICONST_0));
	instructions.insertBefore(insn, new InsnNode(Opcodes.IRETURN));

	instructions.insertBefore(insn, label);

	return method;
}

function getDigSpeed(method) {
	var instructions = method.instructions;
	
	for (var index = 0; index < instructions.size(); index++)
	{
		var insn = instructions.get(index);
		if (insn instanceof VarInsnNode && insn.getOpcode() == Opcodes.FSTORE && insn.var == 3)
		{
			instructions.insert(insn, new VarInsnNode(Opcodes.FSTORE, 3));
			instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/PlayerHooks', 'modifyBreakSpeed', '(FLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)F'));
			instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 0));
			instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 2));
			instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 1));
			instructions.insert(insn, new VarInsnNode(Opcodes.FLOAD, 3));

			return method;
		}
	}


	return method;
}