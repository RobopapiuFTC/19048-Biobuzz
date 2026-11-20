package org.firstinspires.ftc.teamcode.Systems;


import static androidx.core.math.MathUtils.clamp;


//import com.bylazar.telemetry.TelemetryManager;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;


import com.pedropathing.controllers.Controller;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;




import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
//import org.firstinspires.ftc.teamcode.Systems.Camera;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Turret {
    //public Camera c;
    public static double rpt = 6.28319/1900;
    public static double gear = 1900/360,target;
    public double tti=-Math.toRadians(90),tpc=0;
    public static double offset=0;
    public boolean auto;
    public final ServoImplEx t1,t2;


    // public final DcMotorEx turret;
    private Controller p, s;
    public static double t = 0, cameraerror=0;
    public static double pidfSwitch = 30;
    public static double kp = 0.01, kf = 0.0, kd = 0.000, sp = .013, sf = 0, sd = 0.0001;
    public Timer cameraTimer;
    public boolean okt=false;


    public static boolean on = true, manual = false;


    public Turret(HardwareMap hardwareMap, boolean auto) {
        t1=hardwareMap.get(ServoImplEx.class, "t1");
        t2=hardwareMap.get(ServoImplEx.class, "t2");
        t1.setPwmRange(new PwmControl.PwmRange(505,2495));
        t2.setPwmRange(new PwmControl.PwmRange(505,2495));
        if(auto){
            t1.setPosition(0.01);
            t2.setPosition(0.01);
        }
        this.auto=auto;
        p = Controller.pid(kp, 0, kd)
                .plus(Controller.proportionalFeedforward(kf));


        s = Controller.pid(sp,0,sd)
                .plus(Controller.proportionalFeedforward(sf));
        cameraTimer = new Timer();
    }
    public void servoTarget(double radians){
        if(radians>=Math.toRadians(360))radians=radians-Math.toRadians(360);
        if(radians<Math.toRadians(0))radians=radians+Math.toRadians(360);
        t=clamp(radians,Math.toRadians(0),Math.toRadians(360));
    }
    public void setTurretTarget(double ticks) {
        if(ticks>=1900)ticks=ticks-1900;
        if(ticks<0)ticks=ticks+1900;
        t=ticks;
    }
    public double getTurretTarget() {
        return t;
    }



    public double getTurret() {
        return target;
    }


    public void periodic() {
        if(on){
            target=clamp(t/Math.toRadians(369)+0.01,0.01,0.98);
            t1.setPosition(target*0.9607);
            t2.setPosition(target*0.9607);
        }
        else{
            t1.setPosition(0.05);
            t2.setPosition(0.05);
        }


    }
    public void automatic() {
        manual = false;
    }


    public void on() {
        on = true;
    }


    public void off() {
        on = false;
    }


    /** Return yaw in radians */
    public double getYaw() {
        return normalizeAngle(getTurret() * rpt);
    }


    public void setYaw(double radians) {
        radians = normalizeAngle(radians);
        servoTarget(radians);
        // setTurretTarget(radians/rpt+cameraerror);
    }


    public void face(Pose targetPose, Pose robotPose) {
        double angleToTargetFromCenter = (Math.atan2(targetPose.y() - robotPose.y(), targetPose.x() - robotPose.x()));

        double robotAngleDiff = AngleUnit.normalizeRadians(angleToTargetFromCenter - robotPose.heading() + tti);
        setYaw(robotAngleDiff + offset);
    }


    public double Yaw(Pose targetPose, Pose robotPose) {
        double angleToTargetFromCenter = Math.atan2(targetPose.y() - robotPose.y(), targetPose.x() - robotPose.x());
        double robotAngleDiff = AngleUnit.normalizeRadians(angleToTargetFromCenter - robotPose.heading() + tti);
        return robotAngleDiff;
    }


    public void resetTurret() {
        servoTarget(0);
    }




    public static double normalizeAngle(double angleRadians) {
        double angle = angleRadians % (Math.PI * 2D);
        if (angle <= -Math.PI) angle += Math.PI * 2D;
        if (angle > Math.PI) angle -= Math.PI * 2D;
        return angle;
    }
}
