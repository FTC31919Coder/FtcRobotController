package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTag {

    // AprilTag processor
    private AprilTagProcessor aprilTagProcessor;

    // VisionPortal manages the camera and processor
    private VisionPortal visionPortal;

    // Most recently detected tags
    private List<AprilTagDetection> detections;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;

    /**
     * This is Ethan.  I wrote this method to give the AprilTag object a reference to the
     * hardware map and telemetry so that the object can find the webcam and send info
     * to the driver hub without the OpMode needing to provide this code.
     * @param hardwareMap OpMode must pass the hardwareMap so that webcam can be initialized.
     * @param telemetry OpMode must pass the telemetry object so that AprilTag can send
     *                  telemetry to Driver Hub.
     */
      public void Initialize(HardwareMap hardwareMap, Telemetry telemetry) {
          this.telemetry = telemetry;
          this.hardwareMap = hardwareMap;
        // Create the AprilTag processor.
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(false)
                .setDrawCubeProjection(false)
                .build();

        // Get the webcam from the Robot Configuration.
        WebcamName webcam = hardwareMap.get(WebcamName.class,"Webcam 1");

        // Create the VisionPortal.
        visionPortal = new VisionPortal.Builder()
                .setCamera(webcam)
                .addProcessor(aprilTagProcessor)
                .build();
    }

    /**
     * Update AprilTag detections and display useful information.
     *
     */
    public void displayInfo() {

        if (aprilTagProcessor == null) {
            telemetry.addLine("AprilTag processor not initialized.");
            return;
        }

        // Get the current detections.
        detections = aprilTagProcessor.getDetections();

        telemetry.addData("AprilTags Detected", detections.size());

        // Display information for every detected tag.
        for (AprilTagDetection detection : detections) {

            telemetry.addLine("--------------------------------");

            telemetry.addData(
                    "Tag ID",
                    detection.id
            );

            // Metadata will be null for tags that aren't
            // in the active AprilTag library.
            if (detection.metadata != null) {

                telemetry.addData(
                        "Tag Name",
                        detection.metadata.name
                );
            } else {

                telemetry.addLine(
                        "Tag Metadata: Unknown"
                );
            }

            // Position relative to the camera.
            if (detection.ftcPose != null) {
/*
                telemetry.addData(
                        "X",
                        "%.1f inches",
                        detection.ftcPose.x
                );

                telemetry.addData(
                        "Y",
                        "%.1f inches",
                        detection.ftcPose.y
                );

                telemetry.addData(
                        "Z",
                        "%.1f inches",
                        detection.ftcPose.z
                );
*/
                telemetry.addData(
                        "Range",
                        "%.1f inches",
                        detection.ftcPose.range
                );

                telemetry.addData(
                        "Bearing",
                        "%.1f degrees",
                        detection.ftcPose.bearing
                );

                telemetry.addData(
                        "Yaw",
                        "%.1f degrees",
                        detection.ftcPose.yaw
                );
            }
        }
    }

    /**
     * Return the most recently detected AprilTags.
     */
    public List<AprilTagDetection> getDetections() {

        if (aprilTagProcessor == null) {
            return null;
        }

        detections = aprilTagProcessor.getDetections();

        return detections;
    }

    /**
     * Return the first detected AprilTag with the requested ID.
     * Returns null if the tag isn't currently visible.
     */
    public AprilTagDetection getTag(int tagId) {

        if (aprilTagProcessor == null) {
            return null;
        }

        List<AprilTagDetection> currentDetections =
                aprilTagProcessor.getDetections();

        for (AprilTagDetection detection : currentDetections) {

            if (detection.id == tagId) {
                return detection;
            }
        }

        return null;
    }

    /**
     * Check whether a particular AprilTag is visible.
     */
    public boolean isTagVisible(int tagId) {

        return getTag(tagId) != null;
    }

    /**
     * Get the number of currently visible tags.
     */
    public int getTagCount() {

        if (aprilTagProcessor == null) {
            return 0;
        }

        return aprilTagProcessor.getDetections().size();
    }

    /**
     * Boolean to indicate if any tags are visible
     */
    public boolean areAnyTagsVisible() {

        if (aprilTagProcessor == null) {
            return false;
        }
        return !aprilTagProcessor.getDetections().isEmpty();
    }

    /**
     * Stop the camera when the OpMode is finished.
     */
    public void close() {

        if (visionPortal != null) {
            visionPortal.close();
            visionPortal = null;
        }
    }
}