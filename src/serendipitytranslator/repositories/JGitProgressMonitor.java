/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import java.beans.PropertyChangeSupport;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 *
 * @author Vláďa
 */
public class JGitProgressMonitor implements org.eclipse.jgit.lib.ProgressMonitor {
    private int totalTasks = 0;
    private int totalWork = 0;
    private int completedTasks = -1;
    private int actualState = 0;
    private boolean stopped = false;
    private String textToDisplay = "";
    private String name = "";
    
    private PropertyChangeSupport propertyChange = null;

    public JGitProgressMonitor(String name) {
        this.name = name;
    }
    
    public void start(int totalTasks) {
        System.out.println("start - totalTasks = "+totalTasks);
    }

    public void beginTask(String title, int totalWork) {
        boolean isUnknown = (totalWork == ProgressMonitor.UNKNOWN);
        System.out.println("beginTask "+title+"; totalWork = "+totalWork+", isUNKNOWN = " + isUnknown);
        completedTasks++;
        this.totalWork = totalWork;
        if (propertyChange != null) {
            propertyChange.firePropertyChange("progressMax", -1, totalWork);
            propertyChange.firePropertyChange("progressValue", -1, 0);
            propertyChange.firePropertyChange("progressText", textToDisplay, generateTitle(title));
            //System.out.println("beginTask - properties fired");
        }
        actualState = 0;
        textToDisplay = generateTitle(title);
    }

    public void update(int completed) {
        //System.out.println("update - completed = "+completed);
        if (propertyChange != null) {
            propertyChange.firePropertyChange("progressValue", actualState, actualState+completed);
            //System.out.println("update - properties fired");
        }
        actualState = actualState+completed;
    }

    public void endTask() {
        System.out.println("endTask");
        completedTasks++;
        actualState = 0;
    }

    public boolean isCancelled() {
        stopped = stopped || Thread.interrupted();
        //System.out.println("isCancelled request = "+stopped);
        return stopped;
    }

    public void stopMonitor() {
        stopped = true;
    }

    public void setPropertyChange(PropertyChangeSupport propertyChange) {
        this.propertyChange = propertyChange;
    }
    
    private String generateTitle(String title) {
        if (name.length() > 0) {
            return "Git - " + name + " - " + title;
        } else {
            return "Git - " + title;
        }                
    }
}
