package org.firstinspires.ftc.teamcode;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.utils.Timer;
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
    Shooter s;
    public double distance;
    boolean slowmode = false, intake = false, polenFront = false, latchON=false, forward=true;
    double direction;


    Pose Hive1B= new Pose(83.42,48.7,Math.toRadians(90));
    Pose Hive2B=new Pose(83.42, 94.3, Math.toRadians(270));
    Timer sTimer;
    private Pose Robotpose;
    public double dist, target;


    public Robot(HardwareMap h, Follower f, Gamepad g1, boolean blue) {
        this.h = h;
        this.g1 = g1;
        this.f = f;
        i = new Intake(this.h);
        t = new Turret(this.h, true);
        s=new Shooter(this.h);
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
    public void sequenceShoot() {
        if (latchON) {
            shoot(Robotpose);
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
        shoot(Robotpose);
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
    public void setActivated(boolean activated){

    }

    public void shoot(Pose RobotPose) {
        Robotpose=f.pose();
        s.activated=true;
        if(Robotpose.y()<70.75){
            dist=Math.hypot(Hive1B.x() - Robotpose.x(), Hive1B.y() - Robotpose.y());
        }else{
            dist=Math.hypot(Hive2B.x() - Robotpose.x(), Hive2B.y() - Robotpose.y());
        }
        distance=dist;
        s.forDistance(dist);
        s.periodic();
    }
    public void setTarget(double velocity) {
        target = velocity;
    }
    public double GetTarget(){
        return target;
    }
    public void rPeriodic(){
        loop();
        drive();
        turret();


    }
    public void Shooter(){
        // i.shooter.setPower(1);
    }

}
