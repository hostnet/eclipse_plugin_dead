/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * @author Hidde Boomsma <hidde@hostnet.nl>
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "deadfiles"; //$NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  // Log used for plugin wide Eclipse console log
  private MessageConsoleStream log;

  // Hash map used to store the % dead per file and directory
  private final Map<String, Map<String, Color>> deadcolors =
      new HashMap<String, Map<String, Color>>();

  /**
   * Construction of the dead file plugin.
   */
  public Activator() {
    plugin = this;

    MessageConsole console = new MessageConsole(
        "Dead Files Console", null);
    console.activate();
    ConsolePlugin.getDefault().getConsoleManager().addConsoles(
        new IConsole[] { console });
    log = console.newMessageStream();
  }

  /**
   * Add log message when this bundle is loaded. This method should be called by the Eclipse loader
   * mechanism and not by hand. Subclasses may extend this method, but must send super <b>first</b>.
   * {@inheritDoc}
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    log("Dead File Plugin Loaded");
  }

  /**
   * Dispose plugin instance when the bundle is stopped.
   * 
   * {@inheritDoc}
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
   * */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance of this plugin which exposes common methods for logging and the
   * retrieval mechanism for the percentage of dead file for a directory or file.
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /**
   * Method shared for all files in the plugin, used to put a simple message in the Eclipse UI.
   * 
   * @param message The message to be send to the Eclipse Console Log.
   */
  public void log(String message) {
    log.println(message);
  }

  /**
   * Return the structure in which the color data is saved in the plugin.
   * 
   * @return dead color data for the files in the project
   */
  public Map<String, Map<String, Color>> getDeadcolors() {
    return deadcolors;
  }
}
