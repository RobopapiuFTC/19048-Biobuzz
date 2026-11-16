package org.firstinspires.ftc.teamcode;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;


public class Robot {


    public HardwareMap h;
    public Gamepad g1;
    public Follower f;
    public Intake i;
    Turret t;
    boolean slowmode = false, intake = false, polenFront = false, latchON=false, forward=true;
    double direction;


    Pose Hive1B= new Pose(83.42,48.7,Math.toRadians(90));
    Pose Hive2B=new Pose(83.42, 89.7, Math.toRadians(270));
    private Pose Robotpose;


    public Robot(HardwareMap h, Follower f, Gamepad g1, boolean blue) {
        this.h = h;
        this.g1 = g1;
        this.f = f;
        i = new Intake(this.h);
        t = new Turret(this.h, true);
        direction=1;
        this.Robotpose= f.pose();
    }

    public void tstart() {
        f.manual();
    }


    public void drive() {
        if (slowmode) {
            f.manual(-g1.left_stick_y * 0.3, -g1.left_stick_x * 0.3, -g1.right_stick_x * 0.3);
        } else f.manual(-g1.left_stick_y, -g1.left_stick_x, -g1.right_stick_x);
    }

    public void controls() {
        slowmode = g1.right_trigger > 0.3;
        if(g1.y){
            intake=true;
        }
        if(g1.aWasPressed()){
            latchON=!latchON;

        }
        if(g1.bWasPressed()){
            forward=!forward;
        }
        if(g1.xWasPressed()){
            intake=false;
        }
        direction = 1;
    }
    public void sequenceIntake(){
        if(intake){
            i.Int(1,getFront());
        }else{
            i.IntStop();
        }


    }
    public void Latch(){
        i.latch(direction, latchON);


    }
    public double getFront(){
        if(forward){
            return 1;
        }
        else
            return -1;
    }

    public void loop(){
        controls();
        sequenceIntake();
        Latch();

    }
    public void TurretPosition() {
        Robotpose=f.pose();

        if(Robotpose.y()>70.75){
            t.face(Hive2B,Robotpose);
        }else
            t.face(Hive1B,Robotpose);
    }
    public void turret(){
        TurretPosition();
        t.periodic();

    }
    public void rPeriodic(){
        loop();
        drive();
        turret();
        //Shooter();

    }
    public void Shooter(){
        // i.shooter.setPower(1);
    }

}
