package org.firstinspires.ftc.teamcode.TeleOp;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import org.firstinspires.ftc.teamcode.Hardware.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;


@TeleOp(name = "TeleOP", group = "TeleOp")
public class TeleOP extends OpMode {


    Robot r;
    private Follower f;
    public static Pose startPose = new Pose(130, 8, Math.toRadians(0));
    public Pose robotPose;




    @Override
    public void init() {
        f = Constants.create(hardwareMap);
        f.setPose(startPose);
        f.update();
        r=new Robot(hardwareMap, f, gamepad1, true,startPose);
        r.tInit();
    }
    @Override
    public void loop() {
        f.update();

        r.rPeriodic();

        telemetry.addData("x ",r.currentPose.x());
        telemetry.addData("y ",r.currentPose.y());
        telemetry.addData("heading ",r.currentPose.heading());
        telemetry.addData("Forward",r.i.direction);
        telemetry.addData("Distance ", r.distance);
        telemetry.addData("Flywheel Velocity ", r.getShooterVelocity());
        telemetry.addData("Front Amps ", r.i.getFA());
        telemetry.addData("Rear Amps ", r.i.getRA());
        telemetry.update();
    }




}
