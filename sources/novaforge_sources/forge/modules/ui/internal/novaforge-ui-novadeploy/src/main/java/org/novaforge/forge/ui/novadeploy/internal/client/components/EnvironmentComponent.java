package org.novaforge.forge.ui.novadeploy.internal.client.components;

import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentItemProperty;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
* @author Vincent Chenal
*/

public class EnvironmentComponent extends CustomComponent
{

	private static final long serialVersionUID = -8649470178989304147L;

	private HorizontalLayout hp;

	private Button butView;
	private Button butRelease;
	private Button butDelete;
	private Label labEnvironmentId;
	private Label envStatusIcon;
	private Label labInfos;
	private Item environmentItem;

	public EnvironmentComponent(Item environmentItem)
	{
		butView = new Button();
		butRelease = new Button();
		butDelete = new Button();
		labEnvironmentId = new Label();
		envStatusIcon = new Label();
		labInfos = new Label();

		// Data
		this.environmentItem = environmentItem;

		init();
		refreshLocale();
	}

	private void init()
	{
		this.setStyleName("environmentBox");
		this.setWidth("80%");
		this.setHeight("150px");

		hp = new HorizontalLayout();
		hp.setHeight("100%");
		hp.setWidth("100%");
		hp.setStyleName("isbordered");

		// First component
		VerticalLayout firstComponent = new VerticalLayout();
		firstComponent.setWidth("100%");
		firstComponent.setHeight("100%");

		HorizontalLayout firstLine = new HorizontalLayout();
		firstLine.setWidth("100%");
		firstLine.setHeight("100%");

		HorizontalLayout secondLine = new HorizontalLayout();
		secondLine.setWidth("100%");
		secondLine.setHeight("100%");

		labEnvironmentId.setValue((String) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).getValue());

		labInfos.setValue((String) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).getValue());

		envStatusIcon.setValue((String) environmentItem.getItemProperty(
				EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).getValue());

		firstLine.addComponent(labEnvironmentId);
		firstLine.setComponentAlignment(labEnvironmentId, Alignment.MIDDLE_LEFT);
		firstLine.addComponent(envStatusIcon);
		firstLine.setComponentAlignment(envStatusIcon, Alignment.MIDDLE_RIGHT);

		firstLine.setExpandRatio(labEnvironmentId, 0.8f);
		firstLine.setExpandRatio(envStatusIcon, 0.2f);

		secondLine.addComponent(labInfos);
		secondLine.setComponentAlignment(labInfos, Alignment.MIDDLE_CENTER);

		firstComponent.addComponent(firstLine);
		firstComponent.addComponent(secondLine);

		// Second component
		VerticalLayout secondComponent = new VerticalLayout();
		secondComponent.setWidth("100%");
		secondComponent.setHeight("100%");

		butView.setStyleName(Reindeer.BUTTON_DEFAULT);
		butView.setWidth("100%");

		butRelease.setStyleName(Reindeer.BUTTON_DEFAULT);
		butRelease.setWidth("100%");

		butDelete.setStyleName(Reindeer.BUTTON_DEFAULT);
		butDelete.setWidth("100%");

		secondComponent.addComponent(butView);
		secondComponent.addComponent(butRelease);
		secondComponent.addComponent(butDelete);

		hp.addComponent(firstComponent);
		hp.setExpandRatio(firstComponent, 0.8f);
		hp.addComponent(secondComponent);
		hp.setExpandRatio(secondComponent, 0.2f);

		setCompositionRoot(hp);
	}

	private void refreshLocale()
	{
		butView.setCaption("View");
		butRelease.setCaption("Release Version");
		butDelete.setCaption("Delete");
	}

	public Button getButView()
	{
		return butView;
	}

	public void setButView(Button butView)
	{
		this.butView = butView;
	}

	public Button getButRelease()
	{
		return butRelease;
	}

	public void setButRelease(Button butRelease)
	{
		this.butRelease = butRelease;
	}

	public Button getButDelete()
	{
		return butDelete;
	}

	public void setButDelete(Button butDelete)
	{
		this.butDelete = butDelete;
	}
}
