package net.aryt.aryts_random_items.utils;

import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.commands.ParticleCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.level.ClipContext;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RayCast {

    // To raycast just call the function rayTraceEyes and input the player (Minecraft.Getinstance().player or whatever) then input the distance and it will reutrn the target!
    public static Entity rayTraceEyes(final LivingEntity entity, final double length, final boolean destroyBlock) {
        // Get your Eye Pose

        Vec3 start = entity.getEyePosition(1.0F);

        // Get the entitys angle of yaw
        Vec3 lookVec = entity.getLookAngle();

        Vec3 end = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);

        Level world = entity.getCommandSenderWorld();

        BlockHitResult blockHitResult = world.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        Vec3 rayEnd = blockHitResult.getType() != HitResult.Type.MISS ? blockHitResult.getLocation() : end;
        if(destroyBlock){
            BlockPos toDestroy = blockHitResult.getBlockPos();
            world.destroyBlock(toDestroy, false);
        }
        // Vec 3 takes there cords similer to roblox's vetcor 3 and vectors 2 stored like (x,y,z)

        // Create a list of alo entitys in the world in a 50 block range ( this is the possisble targets it could hit)

        List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expandTowards(lookVec.scale(length)).inflate(1.0), e -> e != entity);

        if (entities.size() == 0) {
//            entity.sendSystemMessage(Component.literal("NO ENTITIES DETECTED"));
            return null;
        }
        else {
//            entity.sendSystemMessage(Component.literal(entities.size() + " ENTITIES DETECTED"));
        }

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity e : entities) {

            // Do some basic math
            Vec3 entityVec = e.position().add(0, e.getEyeHeight(), 0);

            // Calculate the intersection of the ray with the entity's bounding box
            AABB entityBox = e.getBoundingBox();
            Optional<Vec3> intersection = entityBox.clip(start, rayEnd);

            if (intersection.isPresent()) {
                double intersectionDistance = start.distanceToSqr(intersection.get());
                if (intersectionDistance < closestDistance) {
                    closestEntity = e;
                    closestDistance = intersectionDistance;
                }
            }
        }

        return closestEntity;
    }

    public static List<Entity> horizontalRayTracesEyes(final LivingEntity entity, final double length, int split, double step, final boolean destroyBlock) {
        // Get your Eye Pose

        Vec3 start = entity.getEyePosition(1.0F);

        // Get the entitys angle of yaw
        Vec3 lookVec = entity.getLookAngle();
        Direction dir = entity.getDirection();
        // Set up the array
        Vec3 rays[] = new Vec3[1 + (split*2)];
        if (dir == Direction.NORTH || dir == Direction.SOUTH){
            rays[0] = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);
            int index = 1;
            double currStepSize = 0.0;
            for (int i = 0; i < split; i++) {
                currStepSize += step;
                rays[index] = start.add((lookVec.x + currStepSize) * length, lookVec.y * length, lookVec.z * length);
                index += 1;
                rays[index] = start.add((lookVec.x - currStepSize) * length, lookVec.y * length, lookVec.z * length);
                index += 1;
            }
        }
        else {
            rays[0] = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);
            int index = 1;
            double currStepSize = 0.0;
            for (int i = 0; i < split; i++) {
                currStepSize += step;
                rays[index] = start.add(lookVec.x * length, lookVec.y * length, (lookVec.z + currStepSize) * length);
                index += 1;
                rays[index] = start.add(lookVec.x * length, lookVec.y * length, (lookVec.z - currStepSize) * length);
                index += 1;
            }
        }

        Level world = entity.getCommandSenderWorld();

        ServerLevel sWorld = entity.getServer().getLevel(world.dimension());

        Vec3 raysEnd[] = new Vec3[rays.length];

        for (int i = 0; i < rays.length; i++) {
            BlockHitResult blockHitResult = world.clip(new ClipContext(start, rays[i], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

            raysEnd[i] = blockHitResult.getType() != HitResult.Type.MISS ? blockHitResult.getLocation() : rays[i];
            if(destroyBlock && !world.isClientSide()){
                BlockPos toDestroy = blockHitResult.getBlockPos();
                world.playSound(null, toDestroy, SoundEvents.ALLAY_DEATH, SoundSource.BLOCKS, 1f,1f);
                world.destroyBlock(toDestroy, false);
                sWorld.sendParticles(ParticleTypes.FIREWORK,toDestroy.getX(),toDestroy.getY(),toDestroy.getZ(),15,0,0,0,0.10);
            }
        }
        // Vec 3 takes there cords similer to roblox's vetcor 3 and vectors 2 stored like (x,y,z)

        // Create a list of alo entitys in the world in a 50 block range ( this is the possisble targets it could hit)

        List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expandTowards(lookVec.scale(length)).inflate(1.0), e -> e != entity);

        if (entities.size() == 0) {
//            entity.sendSystemMessage(Component.literal("NO ENTITIES DETECTED"));
            return null;
        }
        else {
//            entity.sendSystemMessage(Component.literal(entities.size() + " ENTITIES DETECTED"));
        }

        List<Entity> hitEntities = new ArrayList<Entity>();

//        entity.sendSystemMessage(Component.literal("AM IN FOR LOOP"));
        for (Entity e : entities) {

            // Do some basic math
            Vec3 entityVec = e.position().add(0, e.getEyeHeight(), 0);
            boolean hit = false;
            for (int i = 0; i < raysEnd.length; i++) {
                // Calculate the intersection of the ray with the entity's bounding box
                AABB entityBox = e.getBoundingBox();
                Optional<Vec3> intersection = entityBox.clip(start, raysEnd[i]);

                if (intersection.isPresent()) {
                    hitEntities.add(e);
                    break;
                }
            }
        }

        return hitEntities;
    }

    public static List<Entity> verticalRayTracesEyes(final LivingEntity entity, final double length, int split, double step, final boolean destroyBlock) {
        // Get your Eye Pose

        Vec3 start = entity.getEyePosition(1.0F);

        // Get the entitys angle of yaw
        Vec3 lookVec = entity.getLookAngle();
        Direction dir = entity.getDirection();
        // Set up the array
        Vec3 rays[] = new Vec3[1 + (split*2)];

        rays[0] = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);
        int index = 1;
        double currStepSize = 0.0;
        for (int i = 0; i < split; i++) {
            currStepSize += step;
            rays[index] = start.add(lookVec.x * length, (lookVec.y + currStepSize) * length, lookVec.z * length);
            index += 1;
            rays[index] = start.add(lookVec.x * length, (lookVec.y - currStepSize) * length, lookVec.z * length);
            index += 1;
        }
        Level world = entity.getCommandSenderWorld();

        ServerLevel sWorld = entity.getServer().getLevel(world.dimension());

        Vec3 raysEnd[] = new Vec3[rays.length];

        for (int i = 0; i < rays.length; i++) {
            BlockHitResult blockHitResult = world.clip(new ClipContext(start, rays[i], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

            raysEnd[i] = blockHitResult.getType() != HitResult.Type.MISS ? blockHitResult.getLocation() : rays[i];
            if(destroyBlock && !world.isClientSide()){
                BlockPos toDestroy = blockHitResult.getBlockPos();
                world.playSound(null, toDestroy, SoundEvents.ALLAY_DEATH, SoundSource.BLOCKS, 1f,1f);
                world.destroyBlock(toDestroy, false);
                sWorld.sendParticles(ParticleTypes.FIREWORK,toDestroy.getX(),toDestroy.getY(),toDestroy.getZ(),15,0,0,0,0.10);
            }
        }
        // Vec 3 takes there cords similer to roblox's vetcor 3 and vectors 2 stored like (x,y,z)

        // Create a list of alo entitys in the world in a 50 block range ( this is the possisble targets it could hit)

        List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expandTowards(lookVec.scale(length)).inflate(1.0), e -> e != entity);

        if (entities.size() == 0) {
//            entity.sendSystemMessage(Component.literal("NO ENTITIES DETECTED"));
            return null;
        }
        else {
//            entity.sendSystemMessage(Component.literal(entities.size() + " ENTITIES DETECTED"));
        }

        List<Entity> hitEntities = new ArrayList<Entity>();

//        entity.sendSystemMessage(Component.literal("AM IN FOR LOOP"));
        for (Entity e : entities) {

            // Do some basic math
            Vec3 entityVec = e.position().add(0, e.getEyeHeight(), 0);
            boolean hit = false;
            for (int i = 0; i < raysEnd.length; i++) {
                // Calculate the intersection of the ray with the entity's bounding box
                AABB entityBox = e.getBoundingBox();
                Optional<Vec3> intersection = entityBox.clip(start, raysEnd[i]);

                if (intersection.isPresent()) {
                    hitEntities.add(e);
                    break;
                }
            }
        }

        return hitEntities;
    }

    public static List<Entity> diagonalRayTracesEyes(final LivingEntity entity, final double length, int split, double step, final boolean destroyBlock) {
        // Get your Eye Pose

        Vec3 start = entity.getEyePosition(1.0F);

        // Get the entitys angle of yaw
        Vec3 lookVec = entity.getLookAngle();
        Direction dir = entity.getDirection();
        // Set up the array
        Vec3 rays[] = new Vec3[1 + (split*2)];
        double rng = RandomSource.create().nextDouble();
        if (dir == Direction.NORTH || dir == Direction.SOUTH){
            rays[0] = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);
            int index = 1;
            double currStepSize = 0.0;
            if (rng < 0.50){
                for (int i = 0; i < split; i++) {
                    currStepSize += step;
                    rays[index] = start.add((lookVec.x + currStepSize) * length, (lookVec.y + currStepSize) * length, lookVec.z * length);
                    index += 1;
                    rays[index] = start.add((lookVec.x - currStepSize) * length, (lookVec.y - currStepSize) * length, lookVec.z * length);
                    index += 1;
                }
            }
            else {
                for (int i = 0; i < split; i++) {
                    currStepSize += step;
                    rays[index] = start.add((lookVec.x + currStepSize) * length, (lookVec.y - currStepSize) * length, lookVec.z * length);
                    index += 1;
                    rays[index] = start.add((lookVec.x - currStepSize) * length, (lookVec.y + currStepSize) * length, lookVec.z * length);
                    index += 1;
                }
            }
        }
        else {
            rays[0] = start.add(lookVec.x * length, lookVec.y * length, lookVec.z * length);
            int index = 1;
            double currStepSize = 0.0;
            if (rng < 0.50){
                for (int i = 0; i < split; i++) {
                    currStepSize += step;
                    rays[index] = start.add((lookVec.x + currStepSize) * length, (lookVec.y + currStepSize) * length, lookVec.z * length);
                    index += 1;
                    rays[index] = start.add((lookVec.x - currStepSize) * length, (lookVec.y - currStepSize) * length, lookVec.z * length);
                    index += 1;
                }
            }
            else {
                for (int i = 0; i < split; i++) {
                    currStepSize += step;
                    rays[index] = start.add((lookVec.x + currStepSize) * length, (lookVec.y - currStepSize) * length, lookVec.z * length);
                    index += 1;
                    rays[index] = start.add((lookVec.x - currStepSize) * length, (lookVec.y + currStepSize) * length, lookVec.z * length);
                    index += 1;
                }
            }
        }

        Level world = entity.getCommandSenderWorld();

        ServerLevel sWorld = entity.getServer().getLevel(world.dimension());

        Vec3 raysEnd[] = new Vec3[rays.length];

        for (int i = 0; i < rays.length; i++) {
            BlockHitResult blockHitResult = world.clip(new ClipContext(start, rays[i], ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

            raysEnd[i] = blockHitResult.getType() != HitResult.Type.MISS ? blockHitResult.getLocation() : rays[i];
            if(destroyBlock && !world.isClientSide()){
                BlockPos toDestroy = blockHitResult.getBlockPos();
                world.playSound(null, toDestroy, SoundEvents.ALLAY_DEATH, SoundSource.BLOCKS, 1f,1f);
                world.destroyBlock(toDestroy, false);
                sWorld.sendParticles(ParticleTypes.FIREWORK,toDestroy.getX(),toDestroy.getY(),toDestroy.getZ(),15,0,0,0,0.10);
            }
        }
        // Vec 3 takes there cords similer to roblox's vetcor 3 and vectors 2 stored like (x,y,z)

        // Create a list of alo entitys in the world in a 50 block range ( this is the possisble targets it could hit)

        List<Entity> entities = world.getEntities(entity, entity.getBoundingBox().expandTowards(lookVec.scale(length)).inflate(1.0), e -> e != entity);

        if (entities.size() == 0) {
//            entity.sendSystemMessage(Component.literal("NO ENTITIES DETECTED"));
            return null;
        }
        else {
//            entity.sendSystemMessage(Component.literal(entities.size() + " ENTITIES DETECTED"));
        }

        List<Entity> hitEntities = new ArrayList<Entity>();

//        entity.sendSystemMessage(Component.literal("AM IN FOR LOOP"));
        for (Entity e : entities) {

            // Do some basic math
            Vec3 entityVec = e.position().add(0, e.getEyeHeight(), 0);
            boolean hit = false;
            for (int i = 0; i < raysEnd.length; i++) {
                // Calculate the intersection of the ray with the entity's bounding box
                AABB entityBox = e.getBoundingBox();
                Optional<Vec3> intersection = entityBox.clip(start, raysEnd[i]);

                if (intersection.isPresent()) {
                    hitEntities.add(e);
                    break;
                }
            }
        }

        return hitEntities;
    }



}
