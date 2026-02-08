package net.jrex.rexcraft.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.level.Level;

import java.util.Random;

public class AbstractGroupingUtilDino extends AbstractUtilDino{

    //This dino's leader, stolen from velo
    public AbstractGroupingUtilDino veloLeader = null;

    //number of followers this dino has
    public int followers = 0;

    //if this dino is a leader
    public boolean isLeader = false;

    //if this dino is a follower
    public boolean isfollower = false;

    public int raptor_num = choose_id();

    //Chooses a number for the id
    public static int choose_id(){
        Random rand = new Random();
        return rand.nextInt(100000);
    }

    protected AbstractGroupingUtilDino(EntityType<? extends AbstractUtilDino> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void becomeFollower(){
        this.isLeader = false;
        this.isfollower = true;
    }

    public void unfollow(){
        this.isfollower = false;
    }



}
