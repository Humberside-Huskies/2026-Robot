package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.AutoConstants.AutoPattern;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.OperatorInput;
import frc.robot.commands.climb.ClimbDownCommand;
import frc.robot.commands.climb.ClimbUpCommand;
import frc.robot.commands.drive.AutoDriveCommand;
import frc.robot.commands.drive.DriveOnHeadingCommand;
import frc.robot.commands.drive.DriveToTargetCommand;
import frc.robot.commands.drive.EncoderRotateCommand;
import frc.robot.commands.shooter.AutoShootCommand;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LightsSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.VisionSubsystem;


public class AutoCommand extends SequentialCommandGroup {

    public AutoCommand(OperatorInput operatorInput,
        DriveSubsystem driveSubsystem,
        LightsSubsystem lightsSubsystem,
        VisionSubsystem visionSubsystem,
        ClimbSubsystem climbSubsystem,
        ShooterSubsystem shooterSubsystem) {

        // Default is to do nothing.
        // If more commands are added, the instant command will end and
        // the next command will be executed.
        addCommands(new InstantCommand());

        AutoPattern autoPattern = operatorInput.getAutoPattern();
        double      autoDelay   = operatorInput.getAutoDelay();

        Alliance    alliance    = DriverStation.getAlliance().orElse(null);

        if (alliance == null) {
            System.out.println("*** ERROR **** unknown Alliance ");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Auto Selections");
        sb.append("\n   Alliance      : ").append(alliance);
        sb.append("\n   Auto Pattern  : ").append(autoPattern);
        sb.append("\n   Delay         : ").append(autoDelay);

        System.out.println(sb.toString());

        // If any inputs are null, then there was some kind of error.
        if (autoPattern == null) {
            System.out.println("*** ERROR - null found in auto pattern builder ***");
            return;
        }

        /*
         * Delay
         */
        if (autoDelay != 0) {
            addCommands(new WaitCommand(autoDelay));
        }

        /*
         * Compose the appropriate auto commands
         */
        switch (autoPattern) {

        case DO_NOTHING:
            return;


        case AUTO_CLIMB:
            // Drive forward 1m at .25 speed
            addCommands(new DriveOnHeadingCommand(0, .25, 100, driveSubsystem));
            // run autonomous shoot command
            addCommands(new AutoShootCommand(shooterSubsystem, ShooterConstants.MEDIUM_SPEED));
            // extend climber
            addCommands(new ClimbDownCommand(climbSubsystem));
            // drive to tower
            addCommands(new DriveOnHeadingCommand(0, .2, 300, driveSubsystem));
            // delay 2 seconds
            addCommands(new WaitCommand(2.0));
            // retract climber
            addCommands(new ClimbUpCommand(climbSubsystem));
            return;

        case DRIVE_FORWARD_AND_SHOOT:

            // Set the current heading to zero, the gyro could have drifted while
            // waiting for auto to start.
            driveSubsystem.setGyroHeading(0);

            // Drive forward 1m at .25 speed
            addCommands(new DriveOnHeadingCommand(0, .25, 100, driveSubsystem));

            // run autonomous shoot command
            addCommands(new AutoShootCommand(shooterSubsystem, ShooterConstants.LOW_SPEED + 30));
            return;

        case LEFT_AUTO:
            // Set the current heading to zero, the gyro could have drifted while
            // waiting for auto to start.
            driveSubsystem.setGyroHeading(0);

            // Drive forward 1m at .25 speed
            addCommands(new DriveOnHeadingCommand(0, .25, 150, driveSubsystem));

            // rotate; final parameter is direction, negative (left) is (+) positive(right) is (-)
            addCommands(new EncoderRotateCommand(driveSubsystem, 45, 0.3, DriveConstants.RIGHT_TURN));
            // run autonomous shoot command
            addCommands(new AutoShootCommand(shooterSubsystem, ShooterConstants.SPEED2 - 60));
            return;

        case RIGHT_AUTO:

            driveSubsystem.setGyroHeading(0);

            // Drive forward 1m at .25 speed
            addCommands(new DriveOnHeadingCommand(0, .25, 150, driveSubsystem));

            // rotate; final parameter is direction, negative (left) is (+) positive(right) is (-)
            addCommands(new EncoderRotateCommand(driveSubsystem, 45, 0.3, DriveConstants.LEFT_TURN));

            // run autonomous shoot command
            addCommands(new AutoShootCommand(shooterSubsystem, ShooterConstants.SPEED2 - 60));
            return;

        case DRIVE_FORWARD:
            // Drive forward at .2 speed
            addCommands(new AutoDriveCommand(driveSubsystem, DriveConstants.ROBOT_WHEEL_CIRCUMFERENCE, 0.2, 1));
            return;

        case PATH_TEST_THING:
            addCommands(new DriveToTargetCommand(1, 0, driveSubsystem));
            addCommands(new DriveToTargetCommand(1, 1, driveSubsystem));

            return;

        case BOX:

            // Set the current heading to zero, the gyro could have drifted while
            // waiting for auto to start.
            driveSubsystem.setGyroHeading(0);

            // Drive out and then one box
            addCommands(new DriveOnHeadingCommand(0, .4, 200, false, driveSubsystem));
            addCommands(new DriveOnHeadingCommand(270, .4, 100, false, driveSubsystem));
            addCommands(new DriveOnHeadingCommand(180, .4, 100, false, driveSubsystem));
            addCommands(new DriveOnHeadingCommand(90, .4, 100, false, driveSubsystem));
            addCommands(new DriveOnHeadingCommand(0, .4, 100, driveSubsystem));
            return;

        case DRIVE_FORWARD_AND_OUTAKE_L1:

            addCommands(new DriveOnHeadingCommand(0, .2, 350, true, driveSubsystem));
            addCommands(new WaitCommand(2.5));
            addCommands(new WaitCommand(1));


            return;
        }


    }
}