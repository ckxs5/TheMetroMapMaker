package djf.components;

import java.io.IOException;

/**
 * This interface provides the structure for file components in our
 * applications. This lets us call save and load methods from the framework.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public interface AppFileComponent {

    /**
     * This function must be overridden in the actual component and would read
     * app data from a file in the necessary format.
     *
     * @param data
     * @param filePath
     * @throws java.io.IOException
     */
    public void loadData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would write
     * app data to a file in the necessary format.
     *
     * @param data
     * @param filePath
     * @throws java.io.IOException
     */
    public void saveData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would be
     * used for importing app data from another format.
     *
     * 
     * @param data
     * @param filePath
     * @throws java.io.IOException
     */
    public void saveAsData(AppDataComponent data, String filePath) throws IOException;

    /**
     * This function must be overridden in the actual component and would be
     * used for exporting app data into another format.
     *
     * @param data
     * @param filePath
     * @throws java.io.IOException
     */
    public void exportData(AppDataComponent data, String filePath) throws IOException;

}
