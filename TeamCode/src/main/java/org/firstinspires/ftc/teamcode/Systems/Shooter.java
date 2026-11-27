package org.firstinspires.ftc.teamcode.Systems;

import static com.pedropathing.utils.Utils.clamp;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Arrays;
import java.util.List;
import kotlin.jvm.internal.ShortSpreadBuilder;
import org.firstinspires.ftc.teamcode.TestsAndUtilities.ShotSample;

public class Shooter {

    public static double kS = 0.106135, kV = 0.00035, kP = 0.0039;
    public static double tof = 0.25;
    public boolean activated;
    double t;
    DcMotorEx Shooter1, Shooter2;
    public double Power;
    Servo hood;
    double Hood = 0.65;
    double anglemax = 0.85;
    public double flywheelOffset=0;
    public static List<ShotSample> samples = Arrays.asList(
            new ShotSample(20, 1400, 0.65, 0.85),
            new ShotSample(25, 1500, 0.65, 0.85),
            new ShotSample(30, 1600, 0.65, 0.85),
            new ShotSample(35, 1700, 0.65, 0.85),
            new ShotSample(40, 1800, 0.65, 0.85),
            new ShotSample(45, 1900, 0.6, 0.85),
            new ShotSample(50, 1900, 0.6, 0.85),
            new ShotSample(55, 1950, 0.6, 0.85),
            new ShotSample(60, 2000, 0.6, 0.85)
    );

    public Shooter(HardwareMap h) {
        Shooter1 = h.get(DcMotorEx.class, "SS");
        Shooter2 = h.get(DcMotorEx.class, "SD");
        Shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        hood = h.get(Servo.class, "SVD");
        hood.setDirection(Servo.Direction.REVERSE);
        //hood.setPosition(0.6);


    }

    public void loop() {
        if (activated)
            //   setPower(BangBang(getVelocity(),getTarget()));
            setPower((kV * getTarget()) + (kP * (getTarget() - getVelocity())) + kS);
        hood();
    }

    public void hood(){
        //Hood=clamp(anglemax-(getTarget()-getVelocity()-20)*angle,0.1,anglemax);
        hood.setPosition(Hood);
    }
    public void setPower(double p) {
        Shooter1.setPower(p);
        Shooter2.setPower(p);
    }

    public double getTarget() {
        return t;
    }

    public double getVelocity() {
        return Shooter1.getVelocity();
    }

    public void forDistance(double distance) {
        ShotSample shoot = lookupShot(distance);
        Hood = shoot.angle;
        anglemax = shoot.anglemax;
        setTarget(shoot.power + flywheelOffset);
    }

    public void setTarget(double velocity) {
        t = velocity;
    }

    private ShotSample lookupShot(double d) {

        ShotSample before = null;
        ShotSample after = null;

        // find closest sample <= d  and closest sample >= d
        for (ShotSample s : samples) {
            if (s.distance <= d) {
                if (before == null || s.distance > before.distance) {
                    before = s;
                }
            }
            if (s.distance >= d) {
                if (after == null || s.distance < after.distance) {
                    after = s;
                }
            }
        }

        if (before == null) return after;
        if (after == null) return before;
        if (before.distance == after.distance) {
            return before;
        }
        double t = (d - before.distance) / (after.distance - before.distance);

        double power = lerp(before.power, after.power, t);
        double angle = lerp(before.angle, after.angle, t);

        return new ShotSample(d, power, angle,after.anglemax);
    }
    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
    }

