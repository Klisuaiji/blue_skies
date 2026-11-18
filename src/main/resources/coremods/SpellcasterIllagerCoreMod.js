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
		'tick': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.entity.monster.SpellcasterIllager',
				'methodName': 'm_8119_',
				'methodDesc': '()V'
			},
			'transformer': tick
		}
	}	
}

function tick(method) {
	var instructions = method.instructions;
	
	for (var index = 0; index < instructions.size(); index++)
	{
		var insn = instructions.get(index);
		if (insn instanceof VarInsnNode && insn.getOpcode() == Opcodes.ISTORE && insn.var == 1)
		{
			// Backwards since we use insert
			var label = new LabelNode();

			instructions.insert(insn, new InsnNode(Opcodes.RETURN));
			instructions.insert(insn, new JumpInsnNode(Opcodes.IFEQ, label));
			instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, 'com/legacy/blue_skies/asm_hooks/SpellcasterIllagerHooks', 'shouldCancelParticles', '(Lnet/minecraft/world/entity/monster/SpellcasterIllager;)Z'));
			instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 0)); // Load context

			return method;
		}
	}


	return method;
}