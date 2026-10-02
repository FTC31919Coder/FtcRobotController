package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
/*
This class manages the intake mechanism of the BIOBUZZ StarterBot
 */
public class Intake {

    private DcMotor intakeDCMotor = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;

    private double Power;

    //Constructor method; not currently used
    public Intake ()
    {

    }
    public void Initialize (HardwareMap hardwareMap)
    {
        /*
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step.
         */
        intakeDCMotor = hardwareMap.get(DcMotor.class, "intake");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");
        /*
         * Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to
         * slow down much faster when it is coasting. This creates a much more controllable
         * drivetrain. As the robot stops much quicker.
         */
        intakeDCMotor.setZeroPowerBehavior(BRAKE);

        /*
         * set Feeders to an initial value to initialize the servo controller
         */
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);

        /*
         * Much like our drivetrain motors, we set the right intake servo to reverse so that both
         * servos work to pull elements into the intake.
         */
        leftIntakeServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void Activate (Gamepad gamepad, Telemetry telemetry)
    {
        /*
         * Set the intake power variable to equal the right trigger, minus the left trigger.
         * Each trigger outputs a signal from 0-1, with 0 as fully released, and 1 fully depressed.
         * This gives us proportional control of the intake speed. The speed increases as we pull
         * the right trigger further. It's occasionally helpful to be able to reverse the intake,
         * so we also factor in the left trigger. If the left trigger is fully depressed,
         * the intakePower variable will be -1. If the right trigger is fully depressed, the variable
         * will be 1. If the driver pulls both triggers, the intake will remain off.
         * We use this technique (creating a variable, and setting it to our control inputs) to
         * allow us to avoid setting the same motors/servos power more than once per loop. That can
         * create erratic behavior.
         */
        Power = gamepad.right_trigger - gamepad.left_trigger;
        /*
         * Here we set our intake motor and servos to their intake power. The order of operations
         * here is important though. The gamepad triggers define the starting point for the intake
         * power variable in each loop of our code, but inside our launch function we also sometimes
         * change the intake power. So we need to give our launch function a chance to modify the
         * variable before we write it to our motor and servos.
         */

        intakeDCMotor.setPower(Power);
        leftIntakeServo.setPower(Power);
        rightIntakeServo.setPower(Power);
    }
    public void SetPower (double PowerLevel)
    {
    /* Set the intake power to a power level specified programmatically

     */
    intakeDCMotor.setPower(PowerLevel);
    leftIntakeServo.setPower(PowerLevel);
    rightIntakeServo.setPower(PowerLevel);

    }
    public void IncreasePower (double powerLevelIncrease, Telemetry telemetry)
    {
        /* Increase the intake power by a power level specified programmatically

         */
        telemetry.addData("Intake Motor Power at beginning of Increase Power",
                          "%.2f", Power);
        telemetry.addLine();
        telemetry.update();

        double PowerLevel = Power + powerLevelIncrease;
        intakeDCMotor.setPower(PowerLevel);
        leftIntakeServo.setPower(PowerLevel);
        rightIntakeServo.setPower(PowerLevel);
    }
}
