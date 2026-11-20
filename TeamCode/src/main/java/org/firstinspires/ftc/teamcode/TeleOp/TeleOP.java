package org.firstinspires.ftc.teamcode.TeleOp;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
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
    public static Pose startPose = new Pose(133.5, 24.1, Math.toRadians(270));
    DcMotorEx shooter, shooter2;
    Pose robotPose;




    @Override
    public void init() {
        f= Constants.create(hardwareMap);
        f.setPose(startPose);
        f.update();
        r=new Robot(hardwareMap, f, gamepad1, true);
        //shooter=hardwareMap.get(DcMotorEx.class, "SS");
        //shooter2=hardwareMap.get(DcMotorEx.class, "SD");
        //shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

    }
    @Override
    public void loop() {
        DrivePowers powers= ManualDrive.fieldCentric(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                f.pose().heading()
        );
        f.manual(powers);

        f.update();


        robotPose= f.pose();
        r.rPeriodic();
       // r.i.shooter.setPower(1);
      //  shooter.setPower(0.8);
       // shooter2.setPower(0.8);
        telemetry.addData("x ",robotPose.x());
        telemetry.addData("y ",robotPose.y());
        telemetry.addData("heading ",robotPose.heading());
        telemetry.addData("Forward",r.getFront());
        telemetry.addData("Power  ", r.ShooterPower());
        telemetry.addData("distance ", r.distance);
        telemetry.addData("Flywheel Velocity ", r.getShooterVelocity());
        telemetry.addData("Motor current ", r.i.getFA());
        telemetry.update();
    }




}
