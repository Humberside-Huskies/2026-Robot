package frc.robot.commands.drive;

import frc.robot.commands.LoggingCommand;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDriveCommand extends LoggingCommand {

    private DriveSubsystem driveSubsystem;

    // target distance (absolute value)
    private final double   distance;

    // Drive speed
    private final double   speed;

    // Drive direction
    private final float    direction;

    // current motor distances
    private double         startRDist;
    private double         startLDist;

    public AutoDriveCommand(DriveSubsystem driveSubsystem, double distance, double speed, float direction) {

        this.driveSubsystem = driveSubsystem;
        this.distance       = distance;
        this.speed          = speed;
        this.direction      = Math.signum(direction);

        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        startLDist = driveSubsystem.getLeftEncoderDistanceCM();
        startRDist = driveSubsystem.getRightEncoderDistanceCM();
    }

    @Override
    public void execute() {
        // drive motors
        driveSubsystem.setMotorSpeeds(speed * direction, speed * direction);
    }

    @Override
    public boolean isFinished() {
        double leftTravel  = driveSubsystem.getLeftEncoderDistanceCM() - startLDist;
        double rightTravel = driveSubsystem.getRightEncoderDistanceCM() - startRDist;

        double avgTravel   = Math.abs((leftTravel + rightTravel)) / 2.0;

        double error       = Math.abs(avgTravel - distance);

        return error <= 2;
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
    }

}
