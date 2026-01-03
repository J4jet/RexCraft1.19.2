package net.jrex.rexcraft.entity.goal;

import net.jrex.rexcraft.entity.custom.AbstractUtilDino;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class DinoOwnerHurtByTargetGoal extends TargetGoal {
    private final AbstractUtilDino tameAnimal;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public DinoOwnerHurtByTargetGoal(AbstractUtilDino dino) {
        super(dino, false);
        this.tameAnimal = dino;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }


    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (this.tameAnimal.isTamed()) {
            LivingEntity livingentity = this.tameAnimal.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.ownerLastHurtBy = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && AbstractUtilDino.wantsToAttack(this.ownerLastHurtBy, livingentity);
            }
        } else {
            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity livingentity = this.tameAnimal.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}
