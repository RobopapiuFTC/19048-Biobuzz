package org.firstinspires.ftc.teamcode.TestsAndUtilities;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name="FieldCentric", group="tests")
public class FieldCentricTeleOPDrive extends OpMode {


    Follower f;
    public static Pose startPose = new Pose(133.5, 24.1, Math.toRadians(270));

    @Override
    public void init(){
        f= Constants.create(hardwareMap);
        f.setPose(startPose);
        f.update();
    }
    @Override
    public void loop(){
        DrivePowers powers= ManualDrive.fieldCentric(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                f.pose().heading()
        );
        f.manual(powers);
        f.update();
        telemetry.addData("X", f.pose().x());
        telemetry.addData("y", f.pose().y());
        telemetry.addData("H", Math.toDegrees(f.pose().heading()));
    }
}
