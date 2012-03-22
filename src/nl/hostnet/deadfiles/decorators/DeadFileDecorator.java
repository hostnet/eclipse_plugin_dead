/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.decorators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import nl.hostnet.deadfiles.Activator;
import nl.hostnet.deadfiles.preferences.PreferenceConstants;
import nl.hostnet.deadfiles.properties.PropertyConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IDecoratorManager;

/**
 * 
 * @see ILightweightLabelDecorator
 */
public class DeadFileDecorator implements ILightweightLabelDecorator {

  private Activator plugin;
  private IPreferenceStore preferences;

  private boolean foreground;
  private boolean background;

  private Color dead;
  private Color live;
  private Color start;
  private Color end;

  private static final int PCT_ALL = 100;
  private static final int PCT_NONE = 0;

  private final List<ILabelProviderListener> listenerList =
      new ArrayList<ILabelProviderListener>();

  /**
   * Create new DeadFileDecorator instance.
   */
  public DeadFileDecorator() {
    plugin = Activator.getDefault();
    plugin.log("Decorator loaded");
    preferences = plugin.getPreferenceStore();
    loadPreferences();
  }

  /**
   * Reload the preferences and re-decorate the files with the new settings.
   */
  protected void refreshPreferences() {
    plugin.getDeadcolors().clear();
    loadPreferences();
    fireLabelChangedEvent();
  }

  /**
   * Tell eclipse that the colors hava changed and that the files should be re-decorated.
   */
  protected void fireLabelChangedEvent() {
    LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
    for (ILabelProviderListener listener : listenerList) {
      listener.labelProviderChanged(event);
    }
  }

  /**
   * Load the preferences from eclipse into class variables.
   */
  protected void loadPreferences() {
    foreground =
        preferences.getString(PreferenceConstants.P_DECORATION_STYLE) == "foreground";
    background = !foreground;

    dead =
        new Color(null, PreferenceConverter.getColor(
            preferences,
            PreferenceConstants.P_DEAD_COLOR));
    live =
        new Color(null, PreferenceConverter.getColor(
            preferences,
            PreferenceConstants.P_LIVE_COLOR));
    start =
        new Color(null, PreferenceConverter.getColor(
            preferences,
            PreferenceConstants.P_START_COLOR));
    end =
        new Color(null, PreferenceConverter.getColor(
            preferences,
            PreferenceConstants.P_END_COLOR));
  }

  /**
   * Perform the decoration of an element. Eclipse will call this function on the registered element
   * types. {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang .Object,
   *      org.eclipse.jface.viewers.IDecoration)
   */
  public void decorate(Object element, IDecoration decoration) {
    /**
     * Checks that the element is an IResource with the 'Read-only' attribute and adds the decorator
     * based on the specified image description and the integer representation of the placement
     * option.
     */

    IResource resource = (IResource) element;
    if (foreground) {
      decoration.setForegroundColor(getColor(resource));
    } else if (background) {
      decoration.setBackgroundColor(getColor(resource));
    }
  }

  /**
   * Add listener to listener group for receiving notifications about color updates.
   * 
   * @{@inheritDoc}
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
   *      jface.viewers.ILabelProviderListener)
   */
  public void addListener(ILabelProviderListener listener) {
    if (!listenerList.contains(listener)) {
      listenerList.add(listener);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
   */
  public void dispose() {
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang .Object,
   *      java.lang.String)
   */
  public boolean isLabelProperty(Object element, String property) {
    return false;
  }

  /**
   * Remove listener from listener group for receiving notifications about color updates.
   * {@inheritDoc}
   * 
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
   *      .jface.viewers.ILabelProviderListener)
   */
  public void removeListener(ILabelProviderListener listener) {
    listenerList.remove(listener);
  }

  /**
   * Get the decorator instance.
   * 
   * @return instance of the dead file decorator
   */
  public static DeadFileDecorator getInstance() {
    IDecoratorManager decoratorManager =
        Activator.getDefault().getWorkbench().getDecoratorManager();
    return (DeadFileDecorator) decoratorManager
        .getBaseLabelProvider("nl.hostnet.deadfiles.decorators.deadfiledecorator");
  }

  /**
   * re-read colors.txt and refresh the interface.
   */
  public static void refresh() {
    DeadFileDecorator decorator = getInstance();
    if (decorator != null) {
      decorator.refreshPreferences();
    }
  }

  /**
   * Convert a percentage into a color usable for decoration.
   * 
   * @param pct integer value from 0 to 100 including 0 and 100.
   * @return corresponding color
   */
  private Color pctToColor(int pct) {
    int red, green, blue;
    double ratio;
    Color value = null;

    assert (pct >= PCT_NONE && pct <= PCT_ALL);

    if (pct == PCT_NONE) {
      value = live;
    } else if (pct == PCT_ALL) {
      value = dead;
    } else if (pct > PCT_NONE && pct < PCT_ALL) {
      ratio = pct / (double) PCT_ALL;
      red = (int) (ratio * start.getRed() + (1 - ratio) * end.getRed());
      green = (int) (ratio * start.getGreen() + (1 - ratio) * end.getGreen());
      blue = (int) (ratio * start.getBlue() + (1 - ratio) * end.getBlue());
      value = new Color(Display.getCurrent(), red, green, blue);
    } else {
      plugin.log("Wrong Percentage Given, could not turn it into a color");
    }

    return value;
  }

  /**
   * Get the dead color structure.
   * @param project current eclipse project
   * @return hash map linking file path and color
   */
  private synchronized Map<String, Color> getDeadColors(final IPath project) {
    Map<String, Color> app = plugin.getDeadcolors().get(project.toString());
    if (app == null) {
      app = readDead(project);
    }

    return app;
  }

  /**
   * Read and parse colors.txt.
   * @param path path where to look for colors.txt
   * @return hash map linking file path and color
   */
  private Map<String, Color> readDead(IPath path) {
    plugin.log("Try File " + path.append("colors.txt"));
    if (plugin.getDeadcolors().get(path) == null) {
      plugin.getDeadcolors().put(path.toString(), new HashMap<String, Color>());
    }

    try {

      BufferedReader in =
          new BufferedReader(new FileReader(path.append("colors.txt").toFile()));
      Scanner scanner;
      String line;
      Map<String, Color> app;

      app = plugin.getDeadcolors().get(path.toString());

      while ((line = in.readLine()) != null) {
        scanner = new Scanner(line);
        int pct = scanner.nextInt();
        String file = scanner.next();
        app.put(file, pctToColor(pct));

      }
      plugin.log("Read File " + path.append("colors.txt"));
      return app;
    } catch (FileNotFoundException e) {
      // System.out.println("File not found: " + e.getLocalizedMessage());
      return null;
    } catch (IOException e) {
      return null;
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  /**
   * Get the color for a resource element, for example a file in the navigator.
   * @param element the element for which you want to know the color
   * @return the color of the element
   */
  public Color getColor(IResource element) {
    IProject project = element.getProject();

    if (project != null) {
      IPath projectPath;
      IPath propertyPath;
      String property;
      Map<String, Color> app;
      QualifiedName key;

      projectPath = project.getLocation();
      key =
          new QualifiedName(PropertyConstants.P_QUALIFIED_NAME,
              PropertyConstants.P_PATH_PROPERTY);
      try {
        property = project.getPersistentProperty(key);
      } catch (CoreException e) {
        property = null;
      }

      if (property != null && property != "") {
        propertyPath = new Path(property);
      } else {
        propertyPath = null;
      }

      if (propertyPath != null) {
        app = getDeadColors(propertyPath);
      } else {
        app = getDeadColors(projectPath);
      }

      if (app != null) {
        String search = element.getProjectRelativePath().toString();
        if (search == "") {
          search = "/";
        }
        return app.get(search);
      }
    }

    return null;
  }
}
