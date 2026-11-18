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
		'start': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.ai.behavior.SetWalkTargetFromBlockMemory',
				'methodName': 'm_6735_',
				'methodDesc': '(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)V'
			},
			'transformer': start
		}
	}	
}

function start(method) {
	var instructions = method.instructions;
	var insn = instructions.get(0);

	var label = new LabelNode();

	instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
    instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
   	instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/world/entity/ai/behavior/SetWalkTargetFromBlockMemory', ASMAPI.mapField('f_24040_'), 'Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;'));
	instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/SetWalkTargetFromBlockMemoryHooks', 'canContinueToWander', '(Lnet/minecraft/core/particles/ParticleOptions;DDDLnet/minecraft/client/multiplayer/ClientLevel;)Z'));
	instructions.insertBefore(insn, new JumpInsnNode(Opcodes.IFEQ, label));
	instructions.insertBefore(insn, new InsnNode(Opcodes.RETURN));

	instructions.insertBefore(insn, label);

	return method;
}