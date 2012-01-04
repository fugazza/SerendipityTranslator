/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 *
 * @author Vláďa
 */
public class JGitProgressMonitor extends Thread implements org.eclipse.jgit.lib.ProgressMonitor {
    int totalTasks = 0;
    int totalWork = 0;
    int completedTasks = -1;
    int actualState = 0;
    boolean stopped = false;
    String textToDisplay = "";
    
    ProgressMonitorDialog monitorDialog = null;
    
    public void start(int totalTasks) {
        System.out.println("start - totalTasks = "+totalTasks);
    }

    public void beginTask(String title, int totalWork) {
        boolean isUnknown = (totalWork == ProgressMonitor.UNKNOWN);
        System.out.println("beginTask "+title+"; totalWork = "+totalWork+", isUNKNOWN = " + isUnknown);
        completedTasks++;
        if (monitorDialog != null && !isUnknown) {
            monitorDialog.setMaxValue(totalWork);
        }
        actualState = 0;
        textToDisplay = title;
        this.totalWork = totalWork;
    }

    public void update(int completed) {
        System.out.println("update - completed = "+completed);
        actualState = actualState+completed;
    }

    public void endTask() {
        System.out.println("endTask");
        completedTasks++;
        actualState = 0;
    }

    public boolean isCancelled() {
        stopped = (monitorDialog == null) || !monitorDialog.isVisible();
        return stopped;
    }

    @Override
    public void run() {
        System.out.println("thread runs");
        monitorDialog = new ProgressMonitorDialog(null, false);
        monitorDialog.setVisible(true);
        while (!stopped) {
            try {
                if (totalWork != ProgressMonitor.UNKNOWN) {
                    monitorDialog.setProgress(completedTasks*100 + 100*actualState/totalWork);
                } else {
                    monitorDialog.setProgress(completedTasks*100);
                }
                monitorDialog.setText(textToDisplay);
                monitorDialog.repaint();
                //System.out.println("thread sleeping");
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProgressMonitorDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        monitorDialog.setVisible(false);
        System.out.println("thread stopped");
    }
    public void stopMonitor() {
        stopped = true;
    }
    
}
