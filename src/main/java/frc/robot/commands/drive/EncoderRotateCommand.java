package frc.robot.commands.drive;

//import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.DriveConstants;
import frc.robot.commands.LoggingCommand;
import frc.robot.subsystems.DriveSubsystem;

public class EncoderRotateCommand extends LoggingCommand {
    // private final Timer timer = new Timer();
    private DriveSubsystem driveSubsystem;
    private final double   targetDistance;
    private final double   speed;
    private final float    turnDirection;

    // initial read encoder values
    private double         startLeft;
    private double         startRight;

    public EncoderRotateCommand(DriveSubsystem driveSubsystem, double degrees, double speed, float turnDirection) {
        this.driveSubsystem = driveSubsystem;
        this.speed          = speed;

        // obtain negative or positive direction
        this.turnDirection  = Math.signum(turnDirection);

        // percentage of total turn radius is target
        targetDistance      = Math.PI * DriveConstants.CM_TRACK_WIDTH * (degrees / 360.0);
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize() {
        startLeft  = driveSubsystem.getLeftEncoderDistanceCM();
        startRight = driveSubsystem.getRightEncoderDistanceCM();
        // timer.reset();
        // timer.start();
    }

    @Override
    public void execute() {
        // drive motors
        driveSubsystem.setMotorSpeeds(-speed * turnDirection, speed * turnDirection);
    }

    @Override
    public boolean isFinished() {
        double leftTravel    = driveSubsystem.getLeftEncoderDistanceCM() - startLeft;
        double rightTravel   = driveSubsystem.getRightEncoderDistanceCM() - startRight;

        double completedTurn = (Math.abs(leftTravel) + Math.abs(rightTravel)) / 2.0;

        double turnError     = Math.abs(completedTurn - targetDistance);

        return turnError <= 2;
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
    }
}
