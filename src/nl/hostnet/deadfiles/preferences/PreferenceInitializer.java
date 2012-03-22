/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;

import nl.hostnet.deadfiles.Activator;

/**
 * Class used to initialize default preference values.
 * 
 * @author Hidde Boomsma <hidde@hostnet.nl>
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  private static final int COLOR_LIGHT = 220;
  private static final int COLOR_DARK = 180;

  /**
   * Initialize the preference store and load all default preferences.
   * 
   * {@inheritDoc}
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    store.setDefault(PreferenceConstants.P_DECORATION_STYLE, "foreground");

    Color red = new Color(null, COLOR_LIGHT, 0, 0);
    Color darkRed = new Color(null, COLOR_DARK, 0, 0);
    Color green = new Color(null, 0, COLOR_LIGHT, 0);
    Color darkGreen = new Color(null, 0, COLOR_DARK, 0);

    PreferenceConverter.setDefault(
        store,
        PreferenceConstants.P_DEAD_COLOR,
        red.getRGB());
    PreferenceConverter.setDefault(
        store,
        PreferenceConstants.P_START_COLOR,
        darkRed.getRGB());
    PreferenceConverter.setDefault(
        store,
        PreferenceConstants.P_LIVE_COLOR,
        green.getRGB());
    PreferenceConverter.setDefault(
        store,
        PreferenceConstants.P_END_COLOR,
        darkGreen.getRGB());
  }
}
