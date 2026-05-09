package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated health data for a specific patient.
 */
public interface PatientDataGenerator {
    /**
   * Triggers the generation of data for a single patient.
   *
   * @param patientId the unique identifier of the patient
   * @param outputStrategy format and types of outout
   */
    void generate(int patientId, OutputStrategy outputStrategy);
}
