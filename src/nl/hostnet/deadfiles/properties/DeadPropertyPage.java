/*******************************************************************************
 * (c) Hostnet B.V.
 * Original author: Hidde Boomsma <hidde@hostnet.nl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package nl.hostnet.deadfiles.properties;

import nl.hostnet.deadfiles.decorators.DeadFileDecorator;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page at project level where it is possible to select the location where will be looked
 * for the colors.txt file.
 * 
 * @author Hidde Boomsma <hidde@hostnet.nl>
 * 
 */
public class DeadPropertyPage extends PropertyPage {

  private static final String EXPLAIN =
      "Leave the field blank to use the project root";
  private DirectoryFieldEditor dir;
  private static final int EXPLAIN_NUM_COLUMNS = 1;
  private static final int DIR_NUM_COLUMNS = 3;

  /**
   * Constructor for SamplePropertyPage.
   */
  public DeadPropertyPage() {
    super();
  }

  /**
   * Add explanation before the directory field.
   * 
   * @param parent Composite where the section should be placed on
   */
  private void addExplainSection(Composite parent) {
    Composite composite = createDefaultComposite(parent, EXPLAIN_NUM_COLUMNS);
    Label explainLabel = new Label(composite, SWT.NONE);
    explainLabel.setText(EXPLAIN);
  }

  /**
   * Add the section with the directory chooser.
   * 
   * @param parent Composite where the section should be placed on
   */
  private void addPathSection(Composite parent) {
    Composite composite = createDefaultComposite(parent, DIR_NUM_COLUMNS);
    IResource resource = (IResource) getElement();
    dir =
        new DirectoryFieldEditor(PropertyConstants.P_PATH_PROPERTY,
            "Color file path:", composite, resource);
    dir.load();
  }

  /**
   * Add the properties to parent.
   * 
   * @return composite Composite with the properties, placed on parent
   * @param parent the composite where on the properties composite will be placed
   * @see PreferencePage#createContents(Composite)
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    composite.setLayout(layout);
    GridData data = new GridData(GridData.FILL);
    data.grabExcessHorizontalSpace = true;
    composite.setLayoutData(data);

    addPathSection(composite);
    addExplainSection(composite);
    return composite;
  }

  private Composite createDefaultComposite(Composite parent, int columns) {
    Composite composite = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    layout.numColumns = columns;
    composite.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    composite.setLayoutData(data);

    return composite;

  }

  /**
   * Handle click on load defaults button. 
   * {@inheritDoc}
   */
  protected void performDefaults() {
    super.performDefaults();
    // Populate the owner text field with the default value
    dir.loadDefault();
  }

  /**
   * Handle click on OK / Apply button.
   * {@inheritDoc}
   */
  public boolean performOk() {
    // store the value in the owner text field
    dir.store();
    DeadFileDecorator.refresh();
    return true;
  }
}
