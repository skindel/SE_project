package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage - removed to fit new architecture. 
     * @throws IOException if there is an error reading the data
     */
    void readData() throws IOException;
}
