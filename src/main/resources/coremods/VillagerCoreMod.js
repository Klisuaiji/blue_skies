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
		'registerBrainGoals': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.npc.Villager',
				'methodName': 'm_35424_',
				'methodDesc': '(Lnet/minecraft/world/entity/ai/Brain;)V'
			},
			'transformer': registerBrainGoals
		},
		'hasFarmSeeds': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.npc.Villager',
				'methodName': 'm_35516_',
				'methodDesc': '()Z'
			},
			'transformer': hasFarmSeeds
		},
		'updateTrades': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.npc.Villager',
				'methodName': 'm_7604_',
				'methodDesc': '()V'
			},
			'transformer': updateTrades
		}
	}	
}

function registerBrainGoals(method) {
	var instructions = method.instructions;
	var insn = instructions.get(instructions.size() - 2);

    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/VillagerHooks', 'registerBrainGoals', '(Lnet/minecraft/world/entity/npc/Villager;Lnet/minecraft/world/entity/ai/Brain;)V'));

	return method;
}

function hasFarmSeeds(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

   	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/VillagerHooks', 'hasFarmSeeds', '(Lnet/minecraft/world/entity/npc/Villager;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.ICONST_1));
	instructions.insertBefore(insn, new InsnNode(Opcodes.IRETURN));

	instructions.insertBefore(insn, label);

	return method;
}

function updateTrades(method) {
	var instructions = method.instructions;
	var insn = instructions.get(instructions.size() - 2);

    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/VillagerHooks', 'dimBasedTrades', '(Lnet/minecraft/world/entity/npc/Villager;)V'));

	return method;
}