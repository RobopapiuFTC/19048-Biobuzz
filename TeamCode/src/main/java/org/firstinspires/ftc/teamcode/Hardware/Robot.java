package org.firstinspires.ftc.teamcode.Hardware;


import static com.pedropathing.utils.Utils.clamp;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.utils.Timer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Systems.Intake;
import org.firstinspires.ftc.teamcode.Systems.Shooter;
import org.firstinspires.ftc.teamcode.Systems.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;


public class Robot {


    public HardwareMap h;
    public Gamepad g1;
    public GoBildaPinpointDriver pinpoint;
    public Follower f;
    public Intake i;
    public Turret t;
    public Shooter s;
    public double distance;
    public boolean slowmode = false;
    public double direction;


    public Pose HivePark = new Pose(84,64,Math.toRadians(90));
    public Pose HiveLine = new Pose(84, 80, Math.toRadians(270));
    public static boolean sotm = false;
    public static double tof = 0.25;
    public static Pose currentPose,goalPose,startingPose,endPose,predictedPose,movingPose;
    public double dist, target, scalarX, scalarY;


    public Robot(HardwareMap h, Follower f, Gamepad g1, boolean blue, Pose startingPose) {
        this.h = h;
        this.g1 = g1;
        this.f = f;
        i = new Intake(this.h);
        t = new Turret(this.h, true);
        s=new Shooter(this.h);
        this.startingPose = startingPose;
        pinpoint = h.get(GoBildaPinpointDriver.class, "pinpoint");
    }
    public void tInit(){
        if(endPose == null) f.setPose(startingPose);
        else f.setPose(endPose);
        f.update();
    }
    public void drive() {
        if (slowmode) {
            f.manual(-g1.left_stick_y * 0.3, -g1.left_stick_x * 0.3, -g1.right_stick_x * 0.3);
        } else f.manual(-g1.left_stick_y, -g1.left_stick_x, -g1.right_stick_x);
    }

    public double getShooterVelocity(){
        return s.getVelocity();
    }
    public void controls() {
        i.direction = 1;
        slowmode = g1.right_trigger > 0.3;
        if(g1.yWasPressed()){
            i.intaking = true;
            i.moveRamp = true;
        }
        if(g1.dpadUpWasPressed()){
            s.flywheelOffset=s.flywheelOffset+10;
        }
        if(g1.bWasPressed()){
            sotm = !sotm;
        }
        if(g1.dpadDownWasPressed()){
            s.flywheelOffset=s.flywheelOffset-10;
        }
        if(g1.dpadLeftWasPressed()){
            t.offset -= Math.toRadians(1);
        }
        if(g1.dpadRightWasPressed()){
            t.offset += Math.toRadians(1);
        }
        if(g1.xWasPressed()){
            i.rotateRamp();
        }
        if(g1.aWasPressed()){
            i.Transfer();
        }

    }
    public void poses(){
        currentPose=f.pose();
        predictedPose(currentPose,f.velocity().toVector2D());
        direction();
        shootTarget();
        movingGoal();
    }
    public void predictedPose(Pose current, Vector2D velocity) {
        predictedPose = new Pose(
                current.y() + velocity.x() * tof,
                current.y() + velocity.y() * tof,
                current.heading()
        );
    }
    public void movingGoal(){
        if(currentPose.x() > HiveLine.x())scalarX = -1;
        else scalarX = 1;
        if(goalPose == HiveLine){
            movingPose = new Pose(clamp(goalPose.x()+scalarX*currentPose.distance(goalPose)*0.1, 78,90), clamp(goalPose.y()+currentPose.distance(goalPose)*0.1333333,goalPose.y(),88),0);
        }
        else if(goalPose == HivePark){
            movingPose = new Pose(clamp(goalPose.x()+scalarX*currentPose.distance(goalPose)*0.1,78,90), clamp(goalPose.y()-currentPose.distance(goalPose)*0.1333333,56,goalPose.y()),0);
        }
    }
    public void direction(){
        if(-g1.left_stick_y > 0.1)i.direction = 1;
        else if(-g1.left_stick_y < -0.1) i.direction = -1;
    }
    public void shootTarget(){
        if(currentPose.y()>72)goalPose=HiveLine;
        else goalPose=HivePark;
    }
    public void shoot(Pose RobotPose) {
        s.activated=true;
        dist = movingPose.distance(RobotPose);
        distance=dist;
        s.forDistance(dist);
        t.face(movingPose,currentPose);
        t.automatic();
    }
    public void loop(){
        f.update();
        poses();
        controls();
    }
    public void rPeriodic(){
        loop();
        drive();
        if(sotm)shoot(predictedPose);
        else shoot(currentPose);
        i.loop();
        s.loop();
        t.loop();
    }
}
