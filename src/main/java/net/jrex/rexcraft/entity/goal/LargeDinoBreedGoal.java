package net.jrex.rexcraft.entity.goal;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;

public class LargeDinoBreedGoal extends BreedGoal {

    private int loveTime;
    private final double speedModifier;

    public LargeDinoBreedGoal(Animal pAnimal, double pSpeedModifier) {
        super(pAnimal, pSpeedModifier);
        this.speedModifier = pSpeedModifier;
    }

    // Diplo is actually just too big to breed lmao
    @Override
    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.loveTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 30.0D) {
            this.breed();
        }

    }
}
