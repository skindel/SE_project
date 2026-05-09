package com.cardio_generator.outputs;

/**
 * Defines the details of data entries for outputing in different ways
*/
public interface OutputStrategy {
    /**
    * Outputs generated datapoint
    * 
    * @param patientId unique patient identificator number
    * @param timestamp time of capture of the datapoint
    * @param label type of data
    * @param data actual values of the data entry
    */
    void output(int patientId, long timestamp, String label, String data);
}
