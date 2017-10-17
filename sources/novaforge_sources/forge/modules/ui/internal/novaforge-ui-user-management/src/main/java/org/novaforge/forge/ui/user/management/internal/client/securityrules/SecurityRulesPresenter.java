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

import com.google.common.base.Strings;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.user.management.internal.client.components.PasswordFormatType;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author caseryj
 */
public class SecurityRulesPresenter extends AbstractPortalPresenter implements Serializable
{

  /**
   * Default serial version UID
   */
  private static final long       serialVersionUID = 2871014056521205655L;
  /**
   * Content of project view
   */
  private final SecurityRulesView view;

  /**
   * Default constructor
   * 
   * @param pView
   *          {@link SecurityRulesView} the associated view
   * @param pPortalContext
   *          {@link PortalContext} initial
   */
  public SecurityRulesPresenter(final SecurityRulesView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    addListeners();

  }

  /**
   * Add listeners to view
   */
  private void addListeners()
  {
    view.getRulePwdFormatCheckbox().addValueChangeListener(new CheckBox.ValueChangeListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 5355986021595165651L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        final boolean isChecked = view.getRulePwdFormatCheckbox().getValue();
        view.getRulePwdFormatComboBox().setEnabled(isChecked);
        final PasswordFormatType formatValue = (PasswordFormatType) view.getRulePwdFormatComboBox()
            .getValue();
        if (isChecked && PasswordFormatType.CUSTOM.equals(formatValue))
        {
          view.getPwdFormatCustomLayout().setVisible(true);
        }
        else
        {
          view.getPwdFormatCustomLayout().setVisible(false);
        }
        if (isChecked && (formatValue == null))
        {
          view.getPwdFormatDescriptionLayout().setVisible(false);
        }
        else
        {
          view.getPwdFormatDescriptionLayout().setVisible(isChecked);
        }
      }
    });
    view.getRulePwdLifeTimeCheckbox().addValueChangeListener(new CheckBox.ValueChangeListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 8368891182024686436L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        view.getLifeTimePeriodField().setEnabled(view.getRulePwdLifeTimeCheckbox().getValue());
      }
    });
    view.getRulePwdAlertTimeCheckbox().addValueChangeListener(new CheckBox.ValueChangeListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 1633165487214564546L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        view.getAlertTimePeriodField().setEnabled(view.getRulePwdAlertTimeCheckbox().getValue());
      }
    });
    view.getRulePwdFormatComboBox().addValueChangeListener(new ComboBox.ValueChangeListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -3090637880765927234L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        final PasswordFormatType pwdFormat = (PasswordFormatType) view.getRulePwdFormatComboBox().getValue();
        if (pwdFormat != null)
        {
          if (PasswordFormatType.CUSTOM.equals(pwdFormat))
          {
            view.getPwdFormatCustomLayout().setVisible(true);
          }
          else
          {
            view.getPwdFormatCustomLayout().setVisible(false);
          }
          view.getRulePwdFormatDescription().setValue(
              AdminModule.getPortalMessages().getMessage(view.getLocale(), pwdFormat.getI18NDescription()));
          view.getPwdFormatDescriptionLayout().setVisible(true);
        }
        else
        {
          view.getPwdFormatDescriptionLayout().setVisible(false);
        }
      }
    });
    view.getRulePwdFormatTestButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1235072209339853224L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getTestPwdFormatField().setValue(view.getRulePwdFormatCustomValue().getValue());
        UI.getCurrent().addWindow(view.getTestPwdFormatWindow());
      }
    });
    view.getTestPwdCloseWindowButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 6309436302128992144L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getTestPwdFormatWindow());
      }
    });
    view.getTestPwdUpdateAndCloseWindowButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 2106515564262274299L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getRulePwdFormatCustomValue().setValue(view.getTestPwdFormatField().getValue());
        UI.getCurrent().removeWindow(view.getTestPwdFormatWindow());
      }
    });
    view.getTestPwdTestButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 2836191426970544835L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final String regexValue = view.getTestPwdFormatField().getValue();
        final String pwdToTest = view.getTestPwdToTestField().getValue();
        if (!regexValue.isEmpty() && !pwdToTest.isEmpty())
        {
          final boolean isMatching = Pattern.matches(regexValue, pwdToTest);
          if (isMatching)
          {
            view.getTestResultValueLabel().setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
            view.getTestResultValueLabel().setCaption(
                AdminModule.getPortalMessages().getMessage(view.getLocale(),
                    Messages.USERMANAGEMENT_TESTPWD_RESULT_OK));
          }
          else
          {
            view.getTestResultValueLabel().setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));
            view.getTestResultValueLabel().setCaption(
                AdminModule.getPortalMessages().getMessage(view.getLocale(),
                    Messages.USERMANAGEMENT_TESTPWD_RESULT_KO));
          }
        }
        else
        {
          view.getTestResultValueLabel().setCaption("");
          view.getTestResultValueLabel().setIcon(null);
        }
      }
    });
    view.getUpdateSecurityRulesButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 1449791517025265596L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getLifeTimePeriodField().validate();
        view.getAlertTimePeriodField().validate();
        final boolean isLoginGenerated = view.getLoginGeneratedCheckbox().getValue();
        AdminModule.getForgeConfigurationService().setUserLoginGenerated(isLoginGenerated);

        int ruleLifeTime = 0;
        int ruleAlertTime = 0;
        String rulePwdFormat = "";

        // Password LifeTime
        if (view.getRulePwdLifeTimeCheckbox().getValue())
        {
          ruleLifeTime = view.getLifeTimePeriodField().getValue();
        }
        // Password AlertTime
        if (view.getRulePwdAlertTimeCheckbox().getValue())
        {
          ruleAlertTime = view.getAlertTimePeriodField().getValue();
        }

        // Password Format
        if (view.getRulePwdFormatCheckbox().getValue())
        {
          if (PasswordFormatType.NOVAFORGE.equals(view.getRulePwdFormatComboBox().getValue()))
          {
            rulePwdFormat = PasswordFormatType.NOVAFORGE.getValue();
          }
          else
          {
            rulePwdFormat = view.getRulePwdFormatCustomValue().getValue();
          }
        }
        AdminModule.getForgeConfigurationService().setPasswordLifeTime(ruleLifeTime);
        AdminModule.getForgeConfigurationService().setPasswordModificationTime(ruleAlertTime);
        AdminModule.getForgeConfigurationService().setPasswordValidationRegex(rulePwdFormat);

        final TrayNotification notification = new TrayNotification(AdminModule.getPortalMessages()
            .getMessage(view.getLocale(), Messages.USERMANAGEMENT_UPDATE_SECURITY_RULES), AdminModule
            .getPortalMessages().getMessage(view.getLocale(),
                Messages.USERMANAGEMENT_UPDATE_SECURITY_RULES_SUCCESS), TrayNotificationType.SUCCESS);

        notification.show(Page.getCurrent());

      }
    });
  }

  /**
   * Will refresh users information
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return AdminModule.getPortalModuleId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    final boolean isLoginGenerated = AdminModule.getForgeConfigurationService().isUserLoginGenerated();
    view.getLoginGeneratedCheckbox().setValue(isLoginGenerated);

    final int passwordLifeTime = AdminModule.getForgeConfigurationService().getPasswordLifeTime();
    if (passwordLifeTime == 0)
    {
      view.getLifeTimePeriodField().setValue(passwordLifeTime);
      view.getRulePwdLifeTimeCheckbox().setValue(false);
      view.getLifeTimePeriodField().setEnabled(false);
    }
    else
    {
      view.getLifeTimePeriodField().setValue(passwordLifeTime);
      view.getRulePwdLifeTimeCheckbox().setValue(true);
      view.getLifeTimePeriodField().setEnabled(true);
    }
    final int passwordAlertTime = AdminModule.getForgeConfigurationService().getPasswordModificationTime();
    if (passwordAlertTime == 0)
    {
      view.getAlertTimePeriodField().setValue(passwordAlertTime);
      view.getRulePwdAlertTimeCheckbox().setValue(false);
      view.getAlertTimePeriodField().setEnabled(false);
    }
    else
    {
      view.getAlertTimePeriodField().setValue(passwordAlertTime);
      view.getRulePwdAlertTimeCheckbox().setValue(true);
      view.getAlertTimePeriodField().setEnabled(true);
    }
    final String passwordRegex = AdminModule.getForgeConfigurationService().getPasswordValidationRegex();
    if (Strings.isNullOrEmpty(passwordRegex))
    {
      view.getRulePwdFormatCheckbox().setValue(false);
      view.getPwdFormatCustomLayout().setVisible(false);
      view.getRulePwdFormatCustomValue().setValue("");
      view.getPwdFormatDescriptionLayout().setVisible(false);
      view.getRulePwdFormatComboBox().setEnabled(false);
    }
    else
    {
      view.getRulePwdFormatCheckbox().setValue(true);
      if (PasswordFormatType.NOVAFORGE.getValue().equals(passwordRegex))
      {
        view.getRulePwdFormatComboBox().select(PasswordFormatType.NOVAFORGE);
        view.getRulePwdFormatCustomValue().setValue("");
        view.getRulePwdFormatDescription().setValue(
            AdminModule.getPortalMessages().getMessage(view.getLocale(),
                PasswordFormatType.NOVAFORGE.getI18NDescription()));
      }
      else
      {
        view.getPwdFormatCustomLayout().setVisible(true);
        view.getRulePwdFormatComboBox().select(PasswordFormatType.CUSTOM);
        view.getRulePwdFormatCustomValue().setValue(passwordRegex);
        view.getRulePwdFormatDescription().setValue(
            AdminModule.getPortalMessages().getMessage(view.getLocale(),
                PasswordFormatType.CUSTOM.getI18NDescription()));
      }
      view.getPwdFormatDescriptionLayout().setVisible(true);
      view.getRulePwdFormatComboBox().setEnabled(true);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

}
