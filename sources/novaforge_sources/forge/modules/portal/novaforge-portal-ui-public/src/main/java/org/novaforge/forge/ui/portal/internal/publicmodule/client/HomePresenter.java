/**
 * Copyright ( c ) 2011-2014, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or
 * modify it under the terms of the GNU Alffero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Alffero General Public License for more details.
 * You should have received a copy of the GNU Alffero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.portal.internal.publicmodule.client;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import net.engio.mbassy.listener.Handler;
import org.apache.camel.converter.IOConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.client.util.UserIconGenerator;
import org.novaforge.forge.ui.portal.event.LoginRequestedEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.components.CustomLoginForm;
import org.novaforge.forge.ui.portal.internal.publicmodule.module.PublicModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * THis presenter handle the public view.
 *
 * @author Guillaume Lamirand
 */
public class HomePresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 7423132308821472259L;
  /**
   * Logger element
   */
  private static final Log LOG = LogFactory.getLog(HomePresenter.class);
  /**
   * Default value for French language
   */
  private static final String   FR           = "FR";
  /**
   * Default value for icon filename
   */
  private static final String   DEFAULT_ICON = "home_icon.png";
  /**
   * Content the workspace view
   */
  private final HomeView view;
  /**
   * Contains the User bean
   */
  private User newUser;

  private Window changePwdWindow;

  /**
   * Default constructor.
   *
   * @param pView
   *          the view associated to this presenter
   * @param pPortalContext
   *          the portalContext given by PortalUI
   */
  public HomePresenter(final HomeView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    addListeners();

  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {

    view.getLostAccountButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id used for serialization
       */
      private static final long serialVersionUID = -3744916931048153198L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        final PortalModule module = PublicModule.getPortalModuleService().getModule(PortalModuleId.RECOVERY_PWD
                                                                                        .getId());

        final Map<PortalContext.KEY, String> map = new HashMap<PortalContext.KEY, String>();
        map.put(PortalContext.KEY.IN_SUBWINDOW, Boolean.TRUE.toString());
        final PortalContext buildContext = PublicModule.getPortalModuleService().buildContext(getEventBus(),
                                                                                              view.getLocale(), map);
        final PortalComponent presenter = module.createComponent(buildContext);
        presenter.getComponent().setSizeUndefined();
        final Window subWindow = new Window(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                        Messages.RECOVERY_TITLE));
        subWindow.setModal(true);
        subWindow.setResizable(false);
        subWindow.setContent(presenter.getComponent());
        UI.getCurrent().addWindow(subWindow);
        presenter.init();

      }
    });
    view.getLoginForm().addLoginListener(new CustomLoginForm.LoginListener()
    {
      /**
       *
       */
      private static final long serialVersionUID = 3657432166834687131L;

      @Override
      public void onLogin(final CustomLoginForm.LoginEvent event)
      {
        final String  login     = event.getUserName();
        final String  pwd       = event.getPassword();
        final boolean isMathing = PublicModule.getAuthentificationService().doCredentialsMatch(login, pwd);
        if (isMathing)
        {
          try
          {
            boolean canLogin = true;
            if (PublicModule.getUserPresenter().isAuthorizeCreateUser() && PublicModule.getUserPresenter()
                                                                                       .isAuthorizeUpdateUser())
            {
              // Check the password validity
              final boolean candidateForUpdatingPassword = PublicModule.getUserPresenter()
                                                                       .isCandidateForUpdatingPassword(login);
              if (candidateForUpdatingPassword)
              {
                openChangePwd(login, pwd, PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                      Messages.CHANGEPWD_VALIDITY_DESCRIPTION));
                canLogin = false;
              }
            }
            if (canLogin)
            {
              doLogin(login, pwd);
            }

          }
          catch (final UserServiceException e)
          {
            LOG.error(String.format("Unable to retrieve user according to parameter [user=%s]", login), e);
            ExceptionCodeHandler.showNotificationError(PublicModule.getPortalMessages(), e, UI.getCurrent().getLocale());
          }

        }
        else
        {
          final Notification notif = new Notification(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                                  Messages.ERROR_CREDENTIAL_MATHING_TITLE),
                                                      PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                                  Messages.ERROR_CREDENTIAL_MATHING_DESC),
                                                      Type.ERROR_MESSAGE);
          notif.setHtmlContentAllowed(true);
          notif.show(Page.getCurrent());
        }
      }

    });

    view.getConfirmRegistrationButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id used for serilization
       */
      private static final long serialVersionUID = -4515005491514090298L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent clickEvent)
      {
        UI.getCurrent().removeWindow(view.getConfirmRegistrationWindow());
        doLogin(newUser.getLogin(), newUser.getPassword());
      }

    });

    view.getRegisterForm().getRegisterButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id used for serialization
       */
      private static final long serialVersionUID = -8680202564252621566L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        String password = "";

        try
        {
          view.getRegisterForm().commit();

          password = newUser.getPassword();
          newUser.setPassword(PublicModule.getAuthentificationService().hashPassword(password));
          PublicModule.getUserPresenter().createUser(newUser);
          final UserProfile userProfile = PublicModule.getUserPresenter().getUserProfile(newUser.getLogin());
          final BinaryFile userIcon = PublicModule.getUserPresenter().newUserIcon();
          final FixedWidthGraphics2DImage generatedIcon = UserIconGenerator.getIconFileForUser(newUser.getFirstName(),
                                                                                               newUser.getName());
          userIcon.setFile(IOConverter.toBytes(generatedIcon.getStream()));
          userIcon.setMimeType(generatedIcon.getResource().getMIMEType());
          userIcon.setName(generatedIcon.getResource().getFilename());
          userProfile.setImage(userIcon);
          PublicModule.getUserPresenter().updateUserProfile(userProfile);
          view.showConfirmRegistrationWindow(newUser.getLogin(), newUser.getLanguage().getLocale());
        }
        catch (final UserServiceException e)
        {
          // Has to be done here instead of finaly method otherwise we can't retrieve login from user
          if (PublicModule.getForgeConfigurationService().isUserLoginGenerated())
          {
            newUser.setLogin(null);
          }
          Notification notif;
          if (ExceptionCode.ERR_CREATE_USER_LOGIN_ALREADY_EXISTS.equals(e.getCode()))
          {
            notif = new Notification(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                 Messages.ERROR_USER_LOGIN_EXISTS),
                                     Type.ERROR_MESSAGE);
          }
          else if (ExceptionCode.ERR_CREATE_USER_EMAIL_ALREADY_EXISTS.equals(e.getCode()))
          {
            notif = new Notification(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                 Messages.ERROR_USER_EMAIL_EXISTS),
                                     Type.ERROR_MESSAGE);
          }
          else if (ExceptionCode.ERR_CREATE_USER_FORBIDDEN_LOGIN.equals(e.getCode()))
          {
            notif = new Notification(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                 Messages.ERROR_USER_FORBIDDEN_LOGIN),
                                     Type.ERROR_MESSAGE);
          }
          else
          {
            LOG.error(String.format("Unable to create user according to parameter [user=%s]", newUser), e);
            notif = new Notification(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                 Messages.ERROR_TECHNICAL_TITLE),
                                     PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                 Messages.ERROR_TECHNICAL_DESC),
                                     Type.ERROR_MESSAGE);

          }
          notif.setHtmlContentAllowed(true);
          notif.show(Page.getCurrent());
        }
        catch (final Exception e)
        {
          if (PublicModule.getForgeConfigurationService().isUserLoginGenerated())
          {
            // Has to be done here instead of finaly method otherwise we can't retrieve login from user
            newUser.setLogin(null);
          }
          // Let's the form manage the exception
          view.getRegisterForm().setValidationVisible(true);
        }
        finally
        {
          if (StringUtils.isNotBlank(password))
          {
            newUser.setPassword(password);
          }
        }
      }
    });

  }

  /**
   * This method will open a window which allow the user to update its password
   *
   * @param pLogin
   *          user's login
   * @param pPwd
   *          user's pwd
   * @param pDescription
   *          the description displayed
   */
  private void openChangePwd(final String pLogin, final String pPwd, final String pDescription)
  {

    final PortalModule module = PublicModule.getPortalModuleService().getModule(PortalModuleId.CHANGE_PWD.getId());

    final Map<PortalContext.KEY, String> map = new HashMap<PortalContext.KEY, String>();
    map.put(PortalContext.KEY.IN_SUBWINDOW, Boolean.TRUE.toString());
    map.put(PortalContext.KEY.LOGIN, pLogin);
    map.put(PortalContext.KEY.PWD, pPwd);
    map.put(PortalContext.KEY.DESCRIPTION, pDescription);
    final PortalContext buildContext = PublicModule.getPortalModuleService().buildContext(getEventBus(),
                                                                                          view.getLocale(), map);
    final PortalComponent presenter = module.createComponent(buildContext);
    presenter.getComponent().setSizeUndefined();
    changePwdWindow = new Window(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                             Messages.CHANGEPWD_TITLE));
    changePwdWindow.setModal(true);
    changePwdWindow.setResizable(false);
    changePwdWindow.setContent(presenter.getComponent());
    UI.getCurrent().addWindow(changePwdWindow);

  }

  /**
   * Send event to request a login
   *
   * @param pLogin
   *          user's login
   * @param pClearPasswd
   *          user's pwd
   */
  private void doLogin(final String pLogin, final String pClearPasswd)
  {
    getEventBus().publish(new LoginRequestedEvent(pLogin.toLowerCase(), pClearPasswd));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshContent();
  }

  /**
   * Refresh view elements for create/update change Define for each the user.
   */
  private void refreshVisibleElements()
  {
    initTitle();
    if (PublicModule.getUserPresenter().isAuthorizeCreateUser())
    {
      view.enableCreateUser(true);
      newUser = PublicModule.getUserPresenter().newUser();
      initRegisterForm();
    }
    else
    {
      newUser = null;
      view.enableCreateUser(false);
    }
    view.enableUpdateUser(PublicModule.getUserPresenter().isAuthorizeUpdateUser());
  }

  /**
   * It will init the title with its icon and label
   */
  private void initTitle()
  {
    final StreamResource buildImageResource = ResourceUtils.buildImageResource(PublicModule
                                                                                   .getForgeConfigurationService()
                                                                                   .getDefaultIcon(), DEFAULT_ICON);
    view.getTitleIcon().setSource(buildImageResource);

  }

  /**
   * It will init the register form
   */
  private void initRegisterForm()
  {
    final BeanItem<User> userItem = new BeanItem<User>(newUser);
    view.getRegisterForm().bind(userItem);

    view.getRegisterForm().setGeneratedLogin(PublicModule.getForgeConfigurationService().isUserLoginGenerated());
    view.getRegisterForm().setValidationVisible(false);
    view.refreshLocale(view.getLocale());
    initLanguages(view.getLocale());
  }

  private void initLanguages(final Locale pLocale)
  {
    try
    {
      final ComboBox comboBox = view.getRegisterForm().getLanguages();
      final List<Language> languages = PublicModule.getLanguagePresenter().getAllLanguages();
      for (final Language langue : languages)
      {
        final Locale languageLocal = langue.getLocale();
        if (languageLocal != null)
        {
          comboBox.addItem(langue);
          if (Locale.FRENCH.equals(languageLocal))
          {
            comboBox.setItemCaption(langue, PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LANGUAGE_FR));
          }
          else
          {
            comboBox.setItemCaption(langue, PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LANGUAGE_EN));

          }
          if (languageLocal.equals(pLocale))
          {
            comboBox.select(langue);
          }
        }
      }
    }
    catch (final LanguageServiceException e)
    {
      LOG.error("Unable to retrieve languages", e);
      Notification.show(PublicModule.getPortalMessages().getMessage(view.getLocale(),
                                                                    Messages.PUBLIC_REGISTER_FORM_LANGUAGE_ERROR),
                        Type.ERROR_MESSAGE);
    }
  }

  /**


   * Execute the login stuff on {@link LoginRequestedEvent}
   *
   * @param pLoginRequestedEvent
   *          source event
   */
  @Handler
  public void onLoginRequested(final LoginRequestedEvent pLoginRequestedEvent)
  {
    if ((changePwdWindow != null) && (changePwdWindow.getParent() != null))
    {

      UI.getCurrent().removeWindow(changePwdWindow);
      changePwdWindow = null;
    }

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
    refreshVisibleElements();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale locale)
  {
    initLanguages(locale);
    view.refreshLocale(locale);
  }

  @Override
  protected PortalModuleId getModuleId()
  {
    return PublicModule.getPortalModuleId();
  }

}
