/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.properties;

import nl.hostnet.deadfiles.Activator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Directory Field Editor,a directory chooser much alike the one in the preference page.
 * 
 * @author Hidde Boomsma <hidde@hostnet.nl>
 * @see http://www.eclipse.org/articles/Article-Mutatis-mutandis/overlay-pages.html
 */
public class DirectoryFieldEditor extends
    org.eclipse.jface.preference.DirectoryFieldEditor {

  private IResource element;
  private Activator plugin;
  private boolean isDefaultPresented;

  /**
   * Create a new Directory Field Editor.
   * 
   * @param name name for the Directory Field
   * @param labelText label in the UI for this element
   * @param parent The Composite where on this control should be placed
   * @param element The element that should be used to store the directory in.
   */
  public DirectoryFieldEditor(String name, String labelText, Composite parent,
      IResource element) {
    super(
        name, labelText, parent);
    plugin = Activator.getDefault();
    this.element = element;
  }

  /**
   * Load the current value into the Text Box.
   */
  protected void doLoad() {
    Text textField = getTextControl();
    QualifiedName key = getPropertyName();

    // load the current value
    if (textField != null) {
      try {
        String value = element.getPersistentProperty(key);
        if (value != null) {
          textField.setText(value);
        }
        oldValue = value;
      } catch (CoreException e) {
        plugin.log("Could not load property");
      }
    }
  }

  /**
   * Load the default value into the Text Box.
   */
  protected void doLoadDefault() {
    Text textField = getTextControl();
    if (textField != null) {
      textField.setText("");
    }
    valueChanged();
  }

  /**
   * Save the current directory to the property element.
   */
  protected void doStore() {
    Text textField = getTextControl();
    QualifiedName key = getPropertyName();

    try {
      element.setPersistentProperty(key, textField.getText());
    } catch (CoreException e) {
      plugin.log("Could not save property");
    }
  }

  /**
   * Set the property element where the directory should be saved to.
   * @param element new element.
   */
  public void setElement(IResource element) {
    this.element = element;
  }

  /**
   * Get the current element.
   * 
   * @return the current instance
   */
  protected IResource getElement() {
    return element;
  }

  /**
   * The Qualified Name of the plug-in for use where a reference to this plug-in should be made
   * which is safe and unique.
   * 
   * @return Qualified Name for this plug-in
   */
  protected QualifiedName getPropertyName() {
    return new QualifiedName(
        PropertyConstants.P_QUALIFIED_NAME, getPreferenceName());
  }

  /**
   * Initializes this field editor with the preference value from the preference store.
   */
  public void load() {
    if (element == null) {
      return;
    }
    isDefaultPresented = false;
    doLoad();
    refreshValidState();
  }

  /**
   * Initializes this field editor with the default preference value from the preference store.
   */
  public void loadDefault() {
    if (element == null) {
      return;
    }
    isDefaultPresented = true;
    doLoadDefault();
    refreshValidState();
  }

  /**
   * Stores this field editor's value back into the preference store.
   */
  public void store() {
    if (element == null) {
      return;
    }
    if (isDefaultPresented) {
      doStoreDefault();
    } else {
      doStore();
    }
  }

  /**
   * Use the current element as future default element.
   */
  protected void doStoreDefault() {
    if (element == null) {
      return;
    }

    try {
      element.setPersistentProperty(getPropertyName(), null);
    } catch (CoreException e) {
      plugin.log("Could not remove property");
      e.printStackTrace();
    }
  }
}
