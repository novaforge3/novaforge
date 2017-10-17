package org.novaforge.forge.ui.novadeploy.internal.client.components;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentItemProperty;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DeleteEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReleaseEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ViewEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.main.environments.EnvironmentsPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.EnvironmentState;

import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Vincent Chenal
 */

public class EnvironmentBoxGenerator
{
	/**
	 *
	 */
	private static final long serialVersionUID = -1892675339051616319L;

	private EnvironmentsPresenter environmentPresenter;

	private HorizontalLayout hl;

	private Embedded butView;
	private Embedded butRelease;
	private Embedded butDelete;
	private Label labEnvironmentId;
	private Label labPhase;
	private Label labTimeRefId;
	private Label labUser;
	private Embedded envStatusIcon;
	private Image userImage;
	private Label labEnvironmentDescription;
	private ProgressBar loadingIndicator;

	public EnvironmentBoxGenerator(EnvironmentsPresenter environmentPresenter)
	{
		this.environmentPresenter = environmentPresenter;
	}

	public Component generateItem(Item item)
	{
		hl = new HorizontalLayout();

		butView = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_SEARCH));
		butView.setDescription("View environment details");
		butRelease = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_INSTANCE_VALIDATE));
		butRelease.setDescription("Release this environment");
		butDelete = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_INSTANCE_TRASH));
		butDelete.setDescription("Delete this environment");

		labEnvironmentId = new Label("", ContentMode.PREFORMATTED);
		labPhase = new Label();
		labPhase.setStyleName("environmentPhase");
		labUser = new Label();
		labEnvironmentDescription = new Label();
		labTimeRefId = new Label();
		loadingIndicator = new ProgressBar();
		loadingIndicator.setIndeterminate(true);

		envStatusIcon = new Embedded(null, new ThemeResource(NovaForgeResources.PUCE_GREEN));
		envStatusIcon.setWidth("20px");
		envStatusIcon.setWidth("20px");
		userImage = new Image();

		init();
		fillInformations(item);
		addListeners((String) item.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId())
				.getValue(), Long.parseLong((String) item.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).getValue()));
		refreshLocale();

		return hl;
	}

	private void init()
	{
		hl.setSpacing(true);
		hl.setMargin(true);
		hl.setSizeUndefined();
		hl.setStyleName("environmentBox");
		hl.addStyleName("isbordered");

		// LABELS
		labEnvironmentId.setStyleName(NovaForge.LABEL_H2);
		labEnvironmentDescription.setWidth("200px");

		// LEFT LAYOUT
		VerticalLayout lines = new VerticalLayout();
		lines.setSpacing(true);

		// FIRST LINE
		HorizontalLayout firstLine = new HorizontalLayout();
		firstLine.setSizeUndefined();
		firstLine.setSpacing(true);

		VerticalLayout envIdAndDescriptor = new VerticalLayout();
		envIdAndDescriptor.setSpacing(true);
		envIdAndDescriptor.addComponent(labEnvironmentId);
		envIdAndDescriptor.setWidth("300px");
		firstLine.addComponent(envIdAndDescriptor);
		firstLine.addComponent(loadingIndicator);

		// SECOND LINE
		HorizontalLayout secondLine = new HorizontalLayout();
		secondLine.setSpacing(true);
		VerticalLayout userLayout = new VerticalLayout();
		userLayout.addComponent(userImage);
		userLayout.addComponent(labUser);
		userLayout.setComponentAlignment(labUser, Alignment.TOP_CENTER);

		secondLine.addComponent(userLayout);
		secondLine.addComponent(labEnvironmentDescription);

		// THIRD LINE
		HorizontalLayout thirdLine = new HorizontalLayout();
		thirdLine.setSpacing(true);
		thirdLine.addComponent(labPhase);
		thirdLine.addComponent(envStatusIcon);

		lines.setWidth("400px");
		lines.addComponent(firstLine);
		lines.addComponent(labTimeRefId);
		lines.addComponent(secondLine);
		lines.addComponent(thirdLine);

		// RIGHT LAYOUT
		VerticalLayout buttons = new VerticalLayout();
		buttons.setSpacing(true);
		buttons.setSizeUndefined();
		buttons.setWidth("50px");
		buttons.setHeight("100%");

		butView.setWidth("162px");
		butRelease.setWidth("162px");
		butDelete.setWidth("162px");

		butView.setWidth(NovaForge.ACTION_ICON_SIZE);
		butView.setHeight(NovaForge.ACTION_ICON_SIZE);
		butView.setStyleName(NovaForge.BUTTON_IMAGE);
		butView.addStyleName("linkStyle");

		butRelease.setWidth(NovaForge.ACTION_ICON_SIZE);
		butRelease.setHeight(NovaForge.ACTION_ICON_SIZE);
		butRelease.setStyleName(NovaForge.BUTTON_IMAGE);
		butRelease.addStyleName("linkStyle");

		butDelete.setWidth(NovaForge.ACTION_ICON_SIZE);
		butDelete.setHeight(NovaForge.ACTION_ICON_SIZE);
		butDelete.setStyleName(NovaForge.BUTTON_IMAGE);
		butDelete.addStyleName("linkStyle");

		buttons.addComponent(butView);
		buttons.addComponent(butRelease);
		buttons.addComponent(butDelete);

		buttons.setComponentAlignment(butView, Alignment.MIDDLE_CENTER);
		buttons.setComponentAlignment(butRelease, Alignment.MIDDLE_CENTER);
		buttons.setComponentAlignment(butDelete, Alignment.MIDDLE_CENTER);

		hl.addComponent(lines);
		hl.addComponent(buttons);

	}

	private void fillInformations(final Item environmentItem)
	{
		labEnvironmentId.setValue((String) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).getValue());
		labTimeRefId.setValue("Time Reference Id : "
				+ (String) environmentItem.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId())
						.getValue());
		labEnvironmentDescription.setValue("This environment was deployed to test the new feature about ...");
		setLabPhase((String) environmentItem.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId())
				.getValue().toString());
		userImage.setIcon(new ThemeResource(NovaForgeResources.ICON_USER));
		userImage.setStyleName("environmentUserImage");

		labUser.setValue((String) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_USER.getPropertyId()).getValue());

		switch ((EnvironmentState) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).getValue())
		{

		case CHECK:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_GREY));
			loadingIndicator.setVisible(true);
			break;
		case CONFIGURATION:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_ORANGE));
			loadingIndicator.setVisible(true);
			break;
		case DEPLOYED:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_GREEN));
			loadingIndicator.setVisible(false);
			break;
		case FAILED:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_RED));
			loadingIndicator.setVisible(false);
			break;
		case LIFECYCLE:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_ORANGE));
			loadingIndicator.setVisible(true);
			break;

		case PROVISIONING:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_YELLOW));
			loadingIndicator.setVisible(true);
			break;
		default:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_RED));
			loadingIndicator.setVisible(false);
		}

	}

	private void setLabPhase(String phase)
	{
		labPhase.setValue("Phase : " + phase);
	}

	private void addListeners(final String descriptorName, final long timeRefId)
	{

		butRelease.addClickListener(new MouseEvents.ClickListener()
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				environmentPresenter.getEventBusFromPresenter().publish(
						new ReleaseEnvironmentEvent(descriptorName, timeRefId));
			}
		});
		butDelete.addClickListener(new MouseEvents.ClickListener()
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				environmentPresenter.getEventBusFromPresenter().publish(
						new DeleteEnvironmentEvent(descriptorName, timeRefId));
			}
		});
		butView.addClickListener(new MouseEvents.ClickListener()
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
			{
				environmentPresenter.getEventBusFromPresenter().publish(
						new ViewEnvironmentEvent(descriptorName, timeRefId));
			}
		});
	}

	private void refreshLocale()
	{
		/**/
	}
}
