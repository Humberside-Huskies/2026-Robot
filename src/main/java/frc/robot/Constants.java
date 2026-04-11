// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose.
 * <p>
 * All constants should be declared globally (i.e. public static).
 * <br>
 * Do not put anything functional in this class.
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final double DEFAULT_COMMAND_TIMEOUT_SECONDS = 5;

    public static final class OperatorInputConstants {

        public static final int    DRIVER_CONTROLLER_PORT       = 1;
        public static final double DRIVER_CONTROLLER_DEADBAND   = .2;
        public static final int    OPERATOR_CONTROLLER_PORT     = 0;
        public static final double OPERATOR_CONTROLLER_DEADBAND = .2;
    }

    public static final class AutoConstants {

        public static enum AutoPattern {
            DO_NOTHING, DRIVE_FORWARD, BOX, PATH_TEST_THING, DRIVE_FORWARD_AND_OUTAKE_L1,
            DRIVE_FORWARD_AND_SHOOT, LEFT_AUTO, RIGHT_AUTO, AUTO_CLIMB;
        }
    }

    public final class ShooterConstants {

        // Shooter Speeds
        public static final double MIN_SPEED              = 2800;
        // Centre Auto Shoot Speed
        public static final double LOW_SPEED              = 3000;

        public static final double MEDIUM_SPEED           = 3200;
        public static final double HIGH_SPEED             = 3400;
        public static final double MAX_SPEED              = 5000;

        // --------Tester Speeds-----------------------
        public static final double SPEED1                 = 3400;

        // Consistent
        public static final double SPEED2                 = 3600;
        public static final double SPEED3                 = 3800;

        // filter speed
        public static final double SPEED4                 = 4000;
        // -------------------------------------------

        // Motor IDs
        public static final int    KICKER_MOTOR_CAN_ID    = 34;
        public static final int    SHOOTER_MOTOR_CAN_ID   = 42;
        public static final int    SHOOTER_MOTOR_CAN_ID_B = 61;
    }

    public static final class DriveConstants {

        public static enum DriveMode {
            TANK, ARCADE, SINGLE_STICK_LEFT, SINGLE_STICK_RIGHT;
        }

        // NOTE: Follower motors are at CAN_ID+1
        public static final int     LEFT_MOTOR_CAN_ID    = 20;
        public static final int     RIGHT_MOTOR_CAN_ID   = 10;

        public static final boolean LEFT_MOTOR_INVERTED  = false;
        public static final boolean RIGHT_MOTOR_INVERTED = true;



        // 6 inches
        public static final double  ROBOT_WHEEL_DIAMETER_CM       = 15.24;
        public static final double  ROBOT_WHEEL_CIRCUMFERENCE     = ROBOT_WHEEL_DIAMETER_CM * Math.PI;
        /*
         * // NEO motors have 42 encoder counts per motor revolution
         * public static final double NEO_CPR = 42;
         * // # of motor revs per 1 full wheel rev
         * public static final double DRIVE_GEAR_RATIO = 10.71;
         * // Encoder counts per 1 full wheel rev
         * public static final double ENCODER_COUNTS_PER_REVOLUTION = NEO_CPR * DRIVE_GEAR_RATIO;
         */

        // previous code
        public static final double  ENCODER_COUNTS_PER_REVOLUTION = 8.46;

        // Turn directions for encoder command; positive is left, negative is right
        public static final float   LEFT_TURN                     = 1;
        public static final float   RIGHT_TURN                    = -1;

        // Remove *2
        public static final double  CM_PER_ENCODER_COUNT          = (ROBOT_WHEEL_DIAMETER_CM * Math.PI)
            / ENCODER_COUNTS_PER_REVOLUTION;

        // 26.5 inches
        public static final double  CM_TRACK_WIDTH                = 67.31;

        public static final boolean GYRO_INVERTED                 = false;

        /** Proportional gain for gyro pid tracking */
        public static final double  GYRO_PID_KP                   = 0.01;

        public static final double  DRIVE_SCALING_BOOST           = 1;
        public static final double  DRIVE_SCALING_NORMAL          = .5;
        public static final double  DRIVE_SCALING_SLOW            = .3;

        public static final double  ROBOT_WIDTH                   = .6;
    }

    public static final class ClimbConstants {
        public static final int    RIGHT_MOTOR_PORT          = 40;
        public static final int    LEFT_MOTOR_PORT           = 41;

        public static final double CLIMBER_MOTOR_SPEED       = 0.5;
        public static final double RETRACT_MOTOR_SPEED       = -0.65;

        public static final double MIN_HEIGHT_INCHES         = 0.0;
        public static final double MAX_HEIGHT_INCHES         = 7.125;

        public static final double CLIMB_GEAR_RATIO          = 16.0;

        public static final double DIAMETER_INCHES           = 1.3125;

        public static final double CIRCUMFERENCE             = Math.PI * DIAMETER_INCHES;

        public static final double INCHES_PER_MOTOR_ROTATION = CIRCUMFERENCE / CLIMB_GEAR_RATIO;
    }

    public static final class LightsConstants {

        public static final int LED_STRING_PWM_PORT = 0;
        public static final int LED_STRING_LENGTH   = 22;
    }

    public static final class VisionConstants {
        public static final double AMBIGUITY_THRESHOLD_MEGATAG = 0.3;
        public static final double mountedAngleDegrees         = 18.0;
        public static final double mountedHeightMeters         = 0.160655;

        // distance from the target to the floor
        public static final double StationHeightMeters         = 1.49;
        public static final double ReefHeightMeters            = 0.308;
        public static final double ProcessorHeightMeters       = 1.301;
        public static final double BargeHeightMeters           = 1.868;

    }
}
