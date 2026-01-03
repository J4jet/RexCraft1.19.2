package net.jrex.rexcraft.entity.goal;

import net.jrex.rexcraft.entity.custom.AbstractGroupingUtilDino;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class DinoFollowLeaderGoal extends Goal {
    private final AbstractGroupingUtilDino animal;
    @javax.annotation.Nullable
    private AbstractGroupingUtilDino leader;
    private final double speedModifier;
    private int timeToRecalcPath;

    public DinoFollowLeaderGoal(AbstractGroupingUtilDino pAnimal, double pSpeedModifier) {
        this.animal = pAnimal;
        this.speedModifier = pSpeedModifier;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {

        //If this is a leader, or it is tame, return false
        if (this.animal.isLeader || this.animal.isTame()) {
            return false;
        } else {
            List<? extends AbstractGroupingUtilDino> list = this.animal.level.getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            AbstractGroupingUtilDino animal = null;
            double d0 = Double.MAX_VALUE;

            for(AbstractGroupingUtilDino animal1 : list) {
                if (animal1.getAge() >= 0) {
                    double d1 = this.animal.distanceToSqr(animal1);
                    //if this raptor's number is smaller than the other raptor, set it as the leader
                    // && animal1.followers < animal1.follower_cap
                    if (!(d1 > d0) && this.animal.raptor_num < animal1.raptor_num) {
                        d0 = d1;
                        animal = animal1;
                    }
                }
            }

            if (animal == null) {
                return false;
            } else if (d0 < 250.0D) {
                return false;
                //if this raptor's number is smaller than the other raptor, set it as the leader
            } //else if (animal.isfollower) {
            //return false;
            //animal.followers < animal.follower_cap
            else if (this.animal.raptor_num < animal.raptor_num){
                if (animal.isfollower){
                    this.leader = animal.veloLeader;
                    this.animal.veloLeader = animal.veloLeader;
                    this.animal.becomeFollower();
                    return true;
                }
//                    //check to see if it has a leader, if that leader has a higher number, don't switch leaders!
//                    if (this.leader != null){
//                        if (this.leader.raptor_num > animal.raptor_num){
//                            //Don't set new leader, but return true
//                            System.out.println("already following");
//                            System.out.println(this.leader.raptor_num);
//                            return true;
//                        }else{
//                            if (!animal.isLeader){
//                                animal.becomeLeader();
//                            }
//                            animal.unfollow();
//                            animal.addFollower();
//                            this.leader = animal;
//                            this.animal.becomeFollower();
//                            return true;
//                        }
//                    }
//                    if (!animal.isLeader){
//                        animal.becomeLeader();
//                    }
                //animal.unfollow();
                //animal.addFollower();
                this.leader = animal;
                this.animal.becomeFollower();
                this.animal.veloLeader = animal;
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        if (!this.leader.isAlive()) {
            this.leader = null;
            this.animal.veloLeader = null;
            this.animal.unfollow();
            return false;
        } else if (this.animal.isTame()) {
            return false;
        } else {
            double d0 = this.animal.distanceToSqr(this.leader);
            return !(d0 < 300.0D) && !(d0 > 1000.0D);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.timeToRecalcPath = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.leader = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.leader != null){
                this.animal.getNavigation().moveTo(this.leader, this.speedModifier);
            }
        }
    }
}
