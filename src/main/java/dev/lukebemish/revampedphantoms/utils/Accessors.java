package dev.lukebemish.revampedphantoms.utils;

import dev.lukebemish.opensesame.annotations.Coerce;
import dev.lukebemish.opensesame.annotations.Open;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public final class Accessors {
	private Accessors() {}

	@Open(
		name = "<init>",
		targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance",
		type = Open.Type.CONSTRUCT
	)
	public static @Coerce(targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance") Object cooldownInstance(int start, int end) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "startTime",
		targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance",
		type = Open.Type.GET_INSTANCE
	)
	public static int getStartTime(@Coerce(targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance") Object instance) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "endTime",
		targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance",
		type = Open.Type.GET_INSTANCE
	)
	public static int getEndTime(@Coerce(targetName = "net.minecraft.world.item.ItemCooldowns$CooldownInstance") Object instance) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "cooldowns",
		targetClass = ItemCooldowns.class,
		type = Open.Type.GET_INSTANCE
	)
	public static Map<Item, Object> getCooldowns(ItemCooldowns itemCooldowns) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "useItemRemaining",
		targetClass = LivingEntity.class,
		type = Open.Type.SET_INSTANCE
	)
	public static void setUseItemRemaining(LivingEntity entity, int remaining) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "SWOOP",
		targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase",
		type = Open.Type.GET_STATIC
	)
	public static @Coerce(targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase") Object getSwoopPhase() {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "CIRCLE",
		targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase",
		type = Open.Type.GET_STATIC
	)
	public static @Coerce(targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase") Object getCirclePhase() {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "attackPhase",
		targetName = "net.minecraft.world.entity.monster.Phantom",
		type = Open.Type.GET_INSTANCE
	)
	public static @Coerce(targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase") Object getAttackPhase(Phantom phantom) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "attackPhase",
		targetName = "net.minecraft.world.entity.monster.Phantom",
		type = Open.Type.SET_INSTANCE
	)
	public static void setAttackPhase(Phantom phantom, @Coerce(targetName = "net.minecraft.world.entity.monster.Phantom$AttackPhase") Object phase) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "moveTargetPoint",
		targetName = "net.minecraft.world.entity.monster.Phantom",
		type = Open.Type.SET_INSTANCE
	)
	public static void setMoveTargetPoint(Phantom phantom, Vec3 point) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}

	@Open(
		name = "attachments",
		targetClass = EntityAttachments.class,
		type = Open.Type.GET_INSTANCE
	)
	public static Map<EntityAttachment, List<Vec3>> getAttachments(EntityAttachments attachments) {
		throw new UnsupportedOperationException("Replaced by OpenSesame");
	}
}
