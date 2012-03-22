/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import nl.hostnet.deadfiles.Activator;
import nl.hostnet.deadfiles.decorators.DeadFileDecorator;

/**
 * Workspace wide preference page.
 */

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private Activator plugin;

	/**
	 * Get the current plugin instance this preference page belongs to.
	 * @return the plug-in instance.
	 */
	public Activator getPlugin() {
	  return plugin;
	}
	
	/**
	 * Get new preference page  instance.
	 */
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		// setDescription("Dead File Decorator Preferences");
		plugin = Activator.getDefault();
	}

	/**
	 * Handle click on OK/Apply button.
	 * @return success
	 */
	public boolean performOk() {
		if (super.performOk()) {
			DeadFileDecorator.refresh();
			plugin.log("PreferencePage.performOK()");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_DECORATION_STYLE, "Decoration style", 1,
				new String[][] { { "&Foreground", "foreground" },
						{ "&Background", "background" } },
				getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.P_DEAD_COLOR,
				"&Dead Color", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.P_LIVE_COLOR,
				"&Live Color", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.P_START_COLOR,
				"&Start Color", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceConstants.P_END_COLOR,
				"&End Color", getFieldEditorParent()));
	}

	/**
	 * Called by Eclipse for the initialization of the preference page.
	 * 
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}