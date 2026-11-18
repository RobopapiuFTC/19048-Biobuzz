package org.firstinspires.ftc.teamcode;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;


@TeleOp(name = "TeleOP", group = "TeleOp")
public class TeleOP extends OpMode {


    Robot r;
    private Follower f;
    public static Pose startPose = new Pose(83.4, 8, Math.toRadians(90));
    DcMotorEx shooter, shooter2;
    Pose robotPose;




    @Override
    public void init() {
        f= Constants.create(hardwareMap);
        f.setPose(startPose);
        f.update();
        r=new Robot(hardwareMap, f, gamepad1, true);
        shooter=hardwareMap.get(DcMotorEx.class, "SS");
        shooter2=hardwareMap.get(DcMotorEx.class, "SD");
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

    }
    @Override
    public void loop() {
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
        telemetry.addData("Power  ", r.s.Power);
        telemetry.addData("distance ", r.distance);
        telemetry.update();
        telemetry.addData("Motor current ", r.i.getFA());
    }




}
