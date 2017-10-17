/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.user.management.internal.client.securityrules;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.user.management.internal.client.components.PasswordFormatType;
import org.novaforge.forge.ui.user.management.internal.client.components.PeriodField;
import org.novaforge.forge.ui.user.management.internal.client.components.PeriodRange;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.util.Locale;

/**
 * @author caseryj
 */
public class SecurityRulesViewImpl extends VerticalLayout implements SecurityRulesView
{

  /**
   * Default serial version UID
   */
  private static final long  serialVersionUID = 6414799395946451518L;
  /**
   * Define subwindow size
   */
  private static final float SUBWINDOW_SIZE   = 500;
  /**
   * Define regex field size
   */
  private static final float REGEX_FIELD_SIZE = 400;
  /**
   * {@link Form} containing user rules
   */
  private final Form         userRulesForm;
  /**
   * {@link Form} containing security rules
   */
  private final Form         securityRulesForm;
  /**
   * The generated login checkbox
   */
  private CheckBox         generatedLoginCheckbox;
  /**
   * {@link Form} containing password lifetime security
   */
  private Form             passwordLifeTimeForm;
  /**
   * {@link Form} containing password format security
   */
  private Form             passwordFormatForm;
  /**
   * The password lifetime checkbox
   */
  private CheckBox         rulePwdLifeTimeCheckbox;
  /**
   * The password alertime checkbox
   */
  private CheckBox         rulePwdAlertTimeCheckbox;
  /**
   * The password format checkbox
   */
  private CheckBox         rulePwdFormatCheckbox;
  /**
   * The password format combobox
   */
  private ComboBox         rulePwdFormatComboBox;
  /**
   * the lifetime PeriodField
   */
  private PeriodField      lifeTimePeriodField;
  /**
   * the alerttime PeriodField
   */
  private PeriodField      alertTimePeriodField;
  /**
   * The test pwd regex window
   */
  private Window           testPwdFormatWindow;
  /**
   * The pwd format to test field
   */
  private TextArea         testPwdFormatField;
  /**
   * The test pwd button
   */
  private Button           testPwdTestButton;
  /**
   * The pwd to test field
   */
  private TextField        testPwdToTestField;
  /**
   * The close test pwd format window button
   */
  private Button           testPwdCloseWindowButton;
  /**
   * The update and close button
   */
  private Button           testPwdUpdateAndCloseWindowButton;
  /**
   * The test result label caption
   */
  private Label            testResultCaptionLabel;
  /**
   * The test result value label
   */
  private Label            testResultValueLabel;
  /**
   * The password format test it button
   */
  private Button           rulePwdFormatTestButton;
  /**
   * The pwd format custom value layout
   */
  private HorizontalLayout pwdFormatCustomLayout;
  /**
   * The rule pwd format custom textfield
   */
  private TextField        rulePwdFormatCustomValue;
  /**
   * The rule pwd format description
   */
  private Label            rulePwdFormatDescription;
  /**
   * The password format description layout
   */
  private HorizontalLayout pwdFormatDescriptionLayout;
  /**
   * The update pwd rules button
   */
  private Button           updateSecurityRulesButton;
  /**
   * The update security rules layout
   */
  private HorizontalLayout updateSecurityRulesLayout;
  private GridLayout       lifetimeGrid;

  /**
   * Default constructor
   */
  public SecurityRulesViewImpl()
  {
    // Init view
    setMargin(true);

    // Init user account rules
    userRulesForm = new Form();
    final Component generatedLogin = initGeneratedLogin();
    userRulesForm.getLayout().addComponent(generatedLogin);

    // Init security rules content
    securityRulesForm = new Form();
    final Component formPwdLifeTime    = initPasswordLifeTimeForm();
    final Component formPwdFormat      = initPasswordFormatForm();
    final Component securityRuleButton = initUpdateSecurityRulesButton();
    securityRulesForm.getLayout().addComponent(formPwdLifeTime);
    securityRulesForm.getLayout().addComponent(formPwdFormat);
    securityRulesForm.getFooter().setWidth(100, Unit.PERCENTAGE);
    securityRulesForm.getFooter().addComponent(securityRuleButton);
    addComponent(userRulesForm);
    addComponent(securityRulesForm);
    initPopups();
  }

  /**
   * Initialize generated login layout
   */
  private Component initGeneratedLogin()
  {
    generatedLoginCheckbox = new CheckBox();
    generatedLoginCheckbox.setImmediate(true);
    return generatedLoginCheckbox;

  }

  /**
   * Initialize password lifetime rule layout
   */
  private Component initPasswordLifeTimeForm()
  {
    passwordLifeTimeForm = new Form();
    rulePwdLifeTimeCheckbox = new CheckBox();
    rulePwdAlertTimeCheckbox = new CheckBox();
    lifeTimePeriodField = new PeriodField();
    alertTimePeriodField = new PeriodField();
    rulePwdLifeTimeCheckbox.setImmediate(true);
    rulePwdAlertTimeCheckbox.setImmediate(true);
    lifeTimePeriodField.setMinValue(1);
    lifeTimePeriodField.setRange(PeriodRange.MONTH);
    lifeTimePeriodField.setImmediate(true);
    alertTimePeriodField.setMinValue(1);
    alertTimePeriodField.setRange(PeriodRange.DAY);
    alertTimePeriodField.setImmediate(true);
    lifetimeGrid = new GridLayout(2, 2);
    lifetimeGrid.addComponent(rulePwdLifeTimeCheckbox, 0, 0);
    lifetimeGrid.addComponent(lifeTimePeriodField, 1, 0);
    lifetimeGrid.addComponent(rulePwdAlertTimeCheckbox, 0, 1);
    lifetimeGrid.addComponent(alertTimePeriodField, 1, 1);
    lifetimeGrid.setComponentAlignment(rulePwdLifeTimeCheckbox, Alignment.MIDDLE_CENTER);
    lifetimeGrid.setComponentAlignment(lifeTimePeriodField, Alignment.MIDDLE_LEFT);
    lifetimeGrid.setComponentAlignment(rulePwdAlertTimeCheckbox, Alignment.MIDDLE_CENTER);
    lifetimeGrid.setComponentAlignment(alertTimePeriodField, Alignment.MIDDLE_LEFT);
    passwordLifeTimeForm.getLayout().addComponent(lifetimeGrid);
    return passwordLifeTimeForm;
  }

  /**
   * Initialize password format rule layout
   */
  private Component initPasswordFormatForm()
  {
    passwordFormatForm = new Form();
    rulePwdFormatCheckbox = new CheckBox();
    rulePwdFormatTestButton = new Button();
    rulePwdFormatCustomValue = new TextField();
    pwdFormatCustomLayout = new HorizontalLayout();
    pwdFormatDescriptionLayout = new HorizontalLayout();
    rulePwdFormatDescription = new Label();
    rulePwdFormatComboBox = new ComboBox();
    final VerticalLayout pwdFormatLayout = new VerticalLayout();
    final HorizontalLayout pwdFormatCheckBoxLayout = new HorizontalLayout();
    rulePwdFormatCheckbox.setImmediate(true);
    pwdFormatCheckBoxLayout.addComponent(rulePwdFormatCheckbox);
    final HorizontalLayout pwdFormatValueLayout = new HorizontalLayout();
    rulePwdFormatTestButton.setStyleName(NovaForge.BUTTON_LINK);
    rulePwdFormatCustomValue.setWidth(REGEX_FIELD_SIZE, Unit.PIXELS);
    final Label doublePointLabel = new Label(" : ");
    pwdFormatCustomLayout.addComponent(doublePointLabel);
    pwdFormatCustomLayout.addComponent(rulePwdFormatCustomValue);
    pwdFormatCustomLayout.addComponent(rulePwdFormatTestButton);
    pwdFormatCustomLayout.setComponentAlignment(doublePointLabel, Alignment.MIDDLE_LEFT);
    pwdFormatCustomLayout.setComponentAlignment(rulePwdFormatCustomValue, Alignment.MIDDLE_LEFT);
    pwdFormatCustomLayout.setComponentAlignment(rulePwdFormatTestButton, Alignment.MIDDLE_LEFT);
    pwdFormatCustomLayout.setSpacing(true);
    pwdFormatCustomLayout.setVisible(false);
    pwdFormatValueLayout.setSpacing(true);
    pwdFormatValueLayout.addComponent(rulePwdFormatComboBox);
    pwdFormatValueLayout.addComponent(pwdFormatCustomLayout);
    pwdFormatValueLayout.setComponentAlignment(rulePwdFormatComboBox, Alignment.MIDDLE_LEFT);
    pwdFormatValueLayout.setComponentAlignment(pwdFormatCustomLayout, Alignment.MIDDLE_LEFT);
    pwdFormatValueLayout.setMargin(new MarginInfo(false, false, false, true));
    rulePwdFormatDescription.setIcon(new ThemeResource(NovaForgeResources.ICON_INFORMATION));
    pwdFormatDescriptionLayout.setVisible(false);
    pwdFormatDescriptionLayout.setMargin(new MarginInfo(true));
    pwdFormatDescriptionLayout.addComponent(rulePwdFormatDescription);
    pwdFormatLayout.addComponent(pwdFormatCheckBoxLayout);
    pwdFormatLayout.addComponent(pwdFormatValueLayout);
    pwdFormatLayout.addComponent(pwdFormatDescriptionLayout);
    passwordFormatForm.getLayout().addComponent(pwdFormatLayout);
    return passwordFormatForm;
  }

  /**
   * Initialize update security rules button layout
   */
  private Component initUpdateSecurityRulesButton()
  {
    updateSecurityRulesLayout = new HorizontalLayout();
    updateSecurityRulesButton = new Button();
    updateSecurityRulesLayout.setWidth(100, Unit.PERCENTAGE);
    updateSecurityRulesButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    updateSecurityRulesButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    updateSecurityRulesLayout.addComponent(updateSecurityRulesButton);
    updateSecurityRulesLayout.setComponentAlignment(updateSecurityRulesButton, Alignment.BOTTOM_CENTER);
    return updateSecurityRulesLayout;
  }

  /**
   * Initialize the popups of the view
   */
  private void initPopups()
  {
    initTestPwdFormatWindow();
  }

  /**
   * Initialize Test regex windows
   */
  private void initTestPwdFormatWindow()
  {
    testPwdFormatWindow = new Window();
    testPwdFormatWindow.setModal(true);
    testPwdFormatWindow.setResizable(false);
    testPwdFormatWindow.setWidth(SUBWINDOW_SIZE, Unit.PIXELS);
    testPwdFormatWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE));

    testPwdFormatField = new TextArea();
    testPwdCloseWindowButton = new Button();
    testPwdUpdateAndCloseWindowButton = new Button();
    testPwdToTestField = new TextField();
    testResultCaptionLabel = new Label();
    testResultValueLabel = new Label();

    testPwdFormatField.setWidth(REGEX_FIELD_SIZE, Unit.PIXELS);

    final VerticalLayout testPwdFormatWindowLayout = new VerticalLayout();
    testPwdFormatWindowLayout.setWidth(100, Unit.PERCENTAGE);
    testPwdFormatWindowLayout.setMargin(true);
    testPwdFormatWindowLayout.setSpacing(true);
    testPwdFormatWindowLayout.addComponent(testPwdFormatField);

    final HorizontalLayout testPwdToTestLayout = new HorizontalLayout();
    testPwdToTestLayout.setSpacing(true);

    testPwdTestButton = new Button();
    testPwdTestButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    testPwdToTestLayout.addComponent(testPwdToTestField);
    testPwdToTestLayout.addComponent(testPwdTestButton);
    testPwdToTestLayout.setComponentAlignment(testPwdToTestField, Alignment.BOTTOM_LEFT);
    testPwdToTestLayout.setComponentAlignment(testPwdTestButton, Alignment.BOTTOM_LEFT);
    testPwdFormatWindowLayout.addComponent(testPwdToTestLayout);
    final HorizontalLayout testResultMatchingLayout = new HorizontalLayout();
    testResultMatchingLayout.addComponent(testResultCaptionLabel);
    testResultMatchingLayout.addComponent(testResultValueLabel);
    testPwdFormatWindowLayout.addComponent(testResultMatchingLayout);
    testPwdCloseWindowButton.setStyleName(NovaForge.BUTTON_LINK);
    testPwdUpdateAndCloseWindowButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    final HorizontalLayout testPwdWindowButtonsLayout = new HorizontalLayout();
    testPwdWindowButtonsLayout.setSpacing(true);
    testPwdWindowButtonsLayout.addComponent(testPwdCloseWindowButton);
    testPwdWindowButtonsLayout.addComponent(testPwdUpdateAndCloseWindowButton);
    testPwdWindowButtonsLayout.setComponentAlignment(testPwdCloseWindowButton, Alignment.MIDDLE_CENTER);
    testPwdWindowButtonsLayout.setComponentAlignment(testPwdUpdateAndCloseWindowButton,
        Alignment.MIDDLE_CENTER);
    testPwdFormatWindowLayout.addComponent(testPwdWindowButtonsLayout);
    testPwdFormatWindowLayout.setComponentAlignment(testPwdWindowButtonsLayout, Alignment.BOTTOM_CENTER);
    testPwdFormatWindow.setContent(testPwdFormatWindowLayout);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final PortalMessages portalMessages = AdminModule.getPortalMessages();

    userRulesForm.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT));
    generatedLoginCheckbox.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT_CAPTION));
    generatedLoginCheckbox.setDescription(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_SECURITY_USERACCOUNT_DESCRIPTION));

    securityRulesForm.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_MENU_SECURITY));
    passwordLifeTimeForm.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_LIFETIME));
    passwordFormatForm
        .setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_RULE_PWD_FORMAT));
    lifeTimePeriodField.setCaptionBefore(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_LIFETIME_CAPTION));
    alertTimePeriodField.setCaptionBefore(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_ALERTTIME));
    alertTimePeriodField.setCaptionAfter(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_ALERTTIME_CAPTION));
    rulePwdFormatCheckbox.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_CAPTION));
    rulePwdFormatCustomValue.setInputPrompt(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_INPUTPROMPT));
    rulePwdFormatDescription.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_DESC_CAPTION));
    // Init combobox I18N values
    initPwdFormatComboBoxLabel(pLocale, portalMessages);
    // Test custom password format window
    testPwdFormatWindow.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_TESTIT));
    testPwdFormatField.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_TESTPWD_CUSTOMFORMAT));
    testPwdToTestField.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_TESTPWD_PASSWORDTOTEST));
    testPwdTestButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_TESTIT));
    testResultCaptionLabel.setValue(portalMessages
        .getMessage(pLocale, Messages.USERMANAGEMENT_TESTPWD_RESULT));
    testPwdCloseWindowButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CLOSE));
    testPwdUpdateAndCloseWindowButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_TESTPWD_UPDATEANDCLOSE));
    rulePwdFormatTestButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_RULE_PWD_FORMAT_TESTIT));

    updateSecurityRulesButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_UPDATE_SECURITY_RULES));
  }

  /**
   * Initialize the password format combobox
   */
  private void initPwdFormatComboBoxLabel(final Locale pLocale, final PortalMessages pPortalMessage)
  {
    rulePwdFormatComboBox.setImmediate(true);
    rulePwdFormatComboBox.setNullSelectionAllowed(false);
    rulePwdFormatComboBox.setTextInputAllowed(false);
    rulePwdFormatComboBox.setInputPrompt(pPortalMessage.getMessage(pLocale,
                                                                   Messages.USERMANAGEMENT_RULE_PWD_FORMAT_SELECTONE));
    rulePwdFormatComboBox.addItem(PasswordFormatType.NOVAFORGE);
    rulePwdFormatComboBox.setItemCaption(PasswordFormatType.NOVAFORGE, pPortalMessage.getMessage(pLocale,
                                                                                                 PasswordFormatType.NOVAFORGE
                                                                                                     .getI18NName()));
    rulePwdFormatComboBox.addItem(PasswordFormatType.CUSTOM);
    rulePwdFormatComboBox.setItemCaption(PasswordFormatType.CUSTOM, pPortalMessage.getMessage(pLocale,
                                                                                              PasswordFormatType.CUSTOM
                                                                                                  .getI18NName()));
  }

  /**
   * @return the generatedLoginCheckbox
   */
  @Override
  public CheckBox getLoginGeneratedCheckbox()
  {
    return generatedLoginCheckbox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getRulePwdFormatDescription()
  {
    return rulePwdFormatDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getRulePwdFormatCustomValue()
  {
    return rulePwdFormatCustomValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRulePwdFormatTestButton()
  {
    return rulePwdFormatTestButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getTestResultValueLabel()
  {
    return testResultValueLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getTestPwdUpdateAndCloseWindowButton()
  {
    return testPwdUpdateAndCloseWindowButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getTestPwdCloseWindowButton()
  {
    return testPwdCloseWindowButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getTestPwdToTestField()
  {
    return testPwdToTestField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getTestPwdTestButton()
  {
    return testPwdTestButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextArea getTestPwdFormatField()
  {
    return testPwdFormatField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getTestPwdFormatWindow()
  {
    return testPwdFormatWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PeriodField getAlertTimePeriodField()
  {
    return alertTimePeriodField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PeriodField getLifeTimePeriodField()
  {
    return lifeTimePeriodField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getRulePwdFormatComboBox()
  {
    return rulePwdFormatComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getRulePwdFormatCheckbox()
  {
    return rulePwdFormatCheckbox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getRulePwdAlertTimeCheckbox()
  {
    return rulePwdAlertTimeCheckbox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getRulePwdLifeTimeCheckbox()
  {
    return rulePwdLifeTimeCheckbox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getPwdFormatCustomLayout()
  {
    return pwdFormatCustomLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getPwdFormatDescriptionLayout()
  {
    return pwdFormatDescriptionLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUpdateSecurityRulesButton()
  {
    return updateSecurityRulesButton;
  }

}
