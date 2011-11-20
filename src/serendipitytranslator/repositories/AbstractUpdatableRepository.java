/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

/**
 *
 * @author Vláďa
 */
public abstract class AbstractUpdatableRepository extends SimpleFileRepository {
    
    public abstract void updateFiles(String folderPath, String language);
    
    public abstract void setRemoteURL(String remoteURL);

    public abstract String getRemoteURL();

    public abstract void updateFileList(String folderPath);
    
    @Override
    public boolean isUpdatable() {
        return true;
    }
    
}
