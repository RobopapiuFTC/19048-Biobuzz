package org.firstinspires.ftc.teamcode.Hardware;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Systems.Intake;
import org.firstinspires.ftc.teamcode.Systems.Shooter;
import org.firstinspires.ftc.teamcode.Systems.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;


public class Robot {


    public HardwareMap h;
    public Gamepad g1;
    public Follower f;
    public Intake i;
    Turret t;
    Shooter s;
    public double distance;
    boolean slowmode = false, intake = false, polenFront = false, latchON = false, dirSwitch = true, shooting, flower;
    double direction;


    Pose Hive1B = new Pose(83.42, 48.7, Math.toRadians(90));
    Pose Hive2B = new Pose(83.42, 92.8, Math.toRadians(270));
    Pose Flower = new Pose(82.4, 8, Math.toRadians(90));
    Timer sTimer;
    private Pose Robotpose;
    public double dist, target;


    public Robot(HardwareMap h, Follower f, Gamepad g1, boolean blue) {
        this.h = h;
        this.g1 = g1;
        this.f = f;
        i = new Intake(this.h);
        t = new Turret(this.h, true);
        s = new Shooter(this.h);
        i.direction = 1;
        this.Robotpose = f.pose();
    }

    public void tstart() {
        f.manual();
    }


    public void drive() {
        if (slowmode) {
            f.manual(-g1.left_stick_y * 0.3, -g1.left_stick_x * 0.3, -g1.right_stick_x * 0.3);
        } else f.manual(-g1.left_stick_y, -g1.left_stick_x, -g1.right_stick_x);
    }

    public double getShooterVelocity() {
        return s.getVelocity();
    }

    public void controls() {
        slowmode = g1.right_trigger > 0.3;
        if (g1.y) {
            intake = true;
            i.LatchOpen = false;
            shooting = false;
        }
        if(g1.rightBumperWasPressed()){
            i.Flower=!i.Flower;
            i.flower();
        }
        if (g1.leftBumperWasPressed()) {
            relocalizationF();
        }
        if (g1.dpadUpWasPressed()) {
            s.flywheelOffset = s.flywheelOffset + 10;
        }
        if (g1.dpadDownWasPressed()) {
            s.flywheelOffset = s.flywheelOffset - 10;
        }
        if(g1.dpadLeftWasPressed()){
            t.offset-=0.002817;
        }
        if(g1.dpadRightWasPressed()){
            t.offset+=0.002817;
        }
        if (g1.aWasPressed()) {
            intake = false;
            i.LatchOpen= true;
            shooting = true;

        }

        if (g1.bWasPressed()) {
            dirSwitch = !dirSwitch;
        }
        if (g1.xWasPressed()) {
            i.LatchOpen = true;
            intake = false;
            shooting = false;
        }

    }

    public void sequenceIntake() {
        if (intake) {
        i.Int();
        } else {
            i.IntStop();
        }
    }


    public void sequenceShoot() {
        if (i.LatchOpen && shooting) {
            shoot(Robotpose);

        }
    }

    public void Latch() {
        if (!shooting) {
            i.IntStop();
        }
        i.latch();

    }

    public void relocalizationF() {
        f.setPose(Flower);
    }

    public double getFront() {
        if (direction>=0) {
            return 1;
        } else
            return -1;
    }

    public void loop() {
        controls();
        Latch();

    }

    public void TurretPosition() {
        Robotpose = f.pose();

        if (Robotpose.y() > 70.75) {
            t.face(Hive2B, Robotpose);
        } else
            t.face(Hive1B, Robotpose);
    }

    public void turret() {
        TurretPosition();
        t.periodic();

    }

    public void shoot(Pose RobotPose) {
        s.activated = true;
        if (RobotPose.y() < 70.75) {
            dist = Math.hypot(Hive1B.x() - RobotPose.x(), Hive1B.y() - RobotPose.y());
        } else {
            dist = Math.hypot(Hive2B.x() - RobotPose.x(), Hive2B.y() - RobotPose.y());
        }
        distance = dist;
        s.forDistance(dist);
        shooting();
        s.periodic();
    }

    public double ShooterPower() {
        return s.getVelocity();
    }

    public void shooting() {
        if (shooting) {
            i.Shooting();
        }
    }

    public void setTarget(double velocity) {
        target = velocity;
    }

    public double GetTarget() {
        return target;
    }

    public void rPeriodic() {
        loop();
        drive();
        turret();
        sequenceIntake();
        sequenceShoot();
    }

}