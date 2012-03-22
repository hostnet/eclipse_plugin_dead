/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.actions;

import nl.hostnet.deadfiles.decorators.DeadFileDecorator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Our sample action implements workbench action delegate. The action proxy will be created by the
 * workbench and shown in the UI. When the user tries to use the action, this delegate will be
 * created and execution will be delegated to it.
 * 
 * @author Hidde Boomsma <hidde@hostnet.nl>
 * @see IWorkbenchWindowActionDelegate
 */
public class RefreshAction implements IWorkbenchWindowActionDelegate {
  /**
   * The constructor.
   */
  public RefreshAction() {
  }

  /**
   * The action has been activated. The argument of the method represents the 'real' action sitting
   * in the workbench UI.
   * 
   * @see IWorkbenchWindowActionDelegate#run
   * {@inheritDoc}
   */
  public void run(IAction action) {
    DeadFileDecorator.refresh();
  }

  /**
   * Selection in the workbench has been changed. We can change the state of the 'real' action here
   * if we want, but this can only happen after the delegate has been created.
   * {@inheritDoc}
   * @see IWorkbenchWindowActionDelegate#selectionChanged
   */
  public void selectionChanged(IAction action, ISelection selection) {
  }

  /**
   * We can use this method to dispose of any system resources we previously allocated.
   * @see IWorkbenchWindowActionDelegate#dispose
   */
  public void dispose() {
  }

  /**
   * We will cache window object in order to be able to provide parent shell for the message dialog.
   {@inheritDoc}
   * @see IWorkbenchWindowActionDelegate#init
   */
  public void init(IWorkbenchWindow window) {
  }
}
