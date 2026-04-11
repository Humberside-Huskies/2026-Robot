package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.AutoConstants.AutoPattern;
import frc.robot.Constants.DriveConstants.DriveMode;
import frc.robot.Constants.OperatorInputConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.CancelCommand;
import frc.robot.commands.GameController;
import frc.robot.commands.shooter.EjectCommand;
import frc.robot.commands.shooter.IntakeCommand;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

/**
 * The DriverController exposes all driver functions
 * <p>
 * Extend SubsystemBase in order to have a built in periodic call to support SmartDashboard updates
 */
public class OperatorInput extends SubsystemBase {

    private final GameController driverController;
    private final GameController operatorController;

    // Auto Setup Choosers
    SendableChooser<AutoPattern> autoPatternChooser = new SendableChooser<>();
    SendableChooser<Integer>     waitTimeChooser    = new SendableChooser<>();
    SendableChooser<DriveMode>   driveModeChooser   = new SendableChooser<>();

    /**
     * Construct an OperatorInput class that is fed by a DriverController and optionally an
     * OperatorController.
     */
    public OperatorInput() {

        driverController   = new GameController(OperatorInputConstants.DRIVER_CONTROLLER_PORT,
            OperatorInputConstants.DRIVER_CONTROLLER_DEADBAND);

        operatorController = new GameController(OperatorInputConstants.OPERATOR_CONTROLLER_PORT,
            OperatorInputConstants.OPERATOR_CONTROLLER_DEADBAND);

        // Initialize the dashboard selectors
        autoPatternChooser.setDefaultOption("Do Nothing", AutoPattern.DO_NOTHING);
        SmartDashboard.putData("Auto Pattern", autoPatternChooser);
        // autoPatternChooser.addOption("Drive Forward", AutoPattern.DRIVE_FORWARD);
        // autoPatternChooser.addOption("Box", AutoPattern.BOX);
        // autoPatternChooser.addOption("Path Test", AutoPattern.PATH_TEST_THING);
        // autoPatternChooser.addOption("Actual Auto", AutoPattern.DRIVE_FORWARD_AND_OUTAKE_L1);
        autoPatternChooser.addOption("Drive Forward and Shoot", AutoPattern.DRIVE_FORWARD_AND_SHOOT);
        autoPatternChooser.addOption("Left Auto", AutoPattern.LEFT_AUTO);
        autoPatternChooser.addOption("Right Auto", AutoPattern.RIGHT_AUTO);
        autoPatternChooser.addOption("Drive Forward", AutoPattern.DRIVE_FORWARD);


        waitTimeChooser.setDefaultOption("No wait", 0);
        SmartDashboard.putData("Auto Wait Time", waitTimeChooser);
        waitTimeChooser.addOption("1 second", 1);
        waitTimeChooser.addOption("3 seconds", 3);
        waitTimeChooser.addOption("5 seconds", 5);

        driveModeChooser.setDefaultOption("Arcade", DriveMode.ARCADE);
        SmartDashboard.putData("Drive Mode", driveModeChooser);
        driveModeChooser.addOption("Tank", DriveMode.TANK);
        driveModeChooser.addOption("Single Stick (L)", DriveMode.SINGLE_STICK_LEFT);
        driveModeChooser.addOption("Single Stick (R)", DriveMode.SINGLE_STICK_RIGHT);
    }

    /**
     * Configure the button bindings for all operator commands
     * <p>
     * NOTE: This routine requires all subsystems to be passed in
     * <p>
     * NOTE: This routine must only be called once from the RobotContainer
     *
     * @param driveSubsystem
     */
    public void configureButtonBindings(DriveSubsystem drive, ShooterSubsystem shooter, ClimbSubsystem climb) {

        // Cancel Command
        new Trigger(() -> isCancel())
            .onTrue(new CancelCommand(this, drive));

        // Intake
        new Trigger(() -> operatorController.getYButton())
            .onTrue(new IntakeCommand(this, shooter));

        // Eject
        new Trigger(() -> operatorController.getAButton())
            .onTrue(new EjectCommand(this, shooter));
    }

    /*
     * Auto Pattern Selectors
     */
    public AutoPattern getAutoPattern() {
        return autoPatternChooser.getSelected();
    }

    public Integer getAutoDelay() {
        return waitTimeChooser.getSelected();
    }

    public boolean isExtend() {
        return operatorController.getRightBumperButton();
    }

    public boolean isRetract() {
        return operatorController.getLeftBumperButton();
    }

    /*
     * Cancel Command support
     * Do not end the command while the button is pressed
     */
    public boolean isCancel() {
        return driverController.getStartButton();
    }

    /*
     * The following routines are used by the default commands for each subsystem
     *
     * They allow the default commands to get user input to manually move the
     * robot elements.
     */
    /*
     * Drive Subsystem
     */
    public DriveMode getSelectedDriveMode() {
        return driveModeChooser.getSelected();
    }

    public boolean isBoost() {
        return driverController.getLeftBumperButton();
    }

    public boolean isSlowDown() {
        return driverController.getRightBumperButton();
    }



    public double getLeftSpeed() {
        return driverController.getLeftY();
    }

    public double getRightSpeed() {
        return driverController.getRightY();
    }

    public double getSpeed() {

        if (driveModeChooser.getSelected() == DriveMode.SINGLE_STICK_RIGHT) {
            return driverController.getRightY();
        }

        return driverController.getLeftY();
    }

    public double getTurn() {

        if (driveModeChooser.getSelected() == DriveMode.SINGLE_STICK_LEFT) {
            return driverController.getLeftX();
        }

        return driverController.getRightX();
    }

    /*
     * Support for haptic feedback to the driver
     */
    public void startVibrate() {
        driverController.setRumble(GenericHID.RumbleType.kBothRumble, 1);
    }

    public void stopVibrate() {
        driverController.setRumble(GenericHID.RumbleType.kBothRumble, 0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Driver Controller", driverController.toString());
    }

    public boolean isResetEncoders() {
        return driverController.getBackButton();
    }

    public boolean stopShooter() {
        return operatorController.getXButton();
    }

    public double getTargetRPM() {

        // accepts RPM values
        if (operatorController.getPOV() == 0) {
            return ShooterConstants.SPEED1;
        }

        // consistent
        else if (operatorController.getPOV() == 90) {
            return ShooterConstants.SPEED2;
        }

        // consistent
        else if (operatorController.getPOV() == 180) {
            return ShooterConstants.SPEED3;
        }

        else if (operatorController.getPOV() == 270) {
            return ShooterConstants.SPEED4;
        }

        return operatorController.getLeftTriggerAxis() * ShooterConstants.MAX_SPEED;

        // unjam
        // else if (driverController.getPOV() == 45) {
        // shootSpeed = -.4;
        // }
    }

    /*
     * public double getShooterSpeed() {
     * 
     * double shootSpeed = 0;
     * 
     * if (driverController.getPOV() == 0) {
     * shootSpeed = -.6;
     * // return .4;
     * }
     * 
     * else if (driverController.getPOV() == 90) {
     * shootSpeed = -.65;
     * // return .5;
     * }
     * 
     * else if (driverController.getPOV() == 180) {
     * shootSpeed = -.7;
     * // return .55;
     * }
     * 
     * else if (driverController.getPOV() == 270) {
     * shootSpeed = -.75;
     * // return 0.6;
     * }
     * 
     * // unjam
     * else if (driverController.getPOV() == 45) {
     * shootSpeed = -.4;
     * }
     * 
     * return shootSpeed;
     * 
     * // return -driverController.getLeftTriggerAxis();
     * }
     */

    public boolean kickerOn() {
        return operatorController.getRightTriggerAxis() > .3;
    }

}
