package org.novaforge.forge.ui.novadeploy.internal.client.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
* @author Vincent Chenal
*/

public class DescriptorWindow extends Window
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7595744067165202645L;

	private Form f;
	private TextField txtDescriptorName;
	private DescriptorEditor descriptorEditor;
	private Button butOkCreate;
	private Button butCancelCreate;

	private String descriptorName;

	public DescriptorWindow()
	{
		initContent();
	}

	private void initContent()
	{
		f = new Form();
		txtDescriptorName = new TextField();
		descriptorEditor = new DescriptorEditor();
		butCancelCreate = new Button();
		butOkCreate = new Button();

		initDescriptorEditor();

		// Initialize root layout
		VerticalLayout vertical = new VerticalLayout();
		vertical.setMargin(true);
		vertical.setSpacing(true);

		// Create components
		f.getLayout().addComponent(txtDescriptorName);
		HorizontalLayout buttonBar = new HorizontalLayout();

		butCancelCreate.setStyleName(Reindeer.BUTTON_DEFAULT);
		butOkCreate.setStyleName(Reindeer.BUTTON_DEFAULT);

		buttonBar.addComponent(butCancelCreate);
		buttonBar.addComponent(butOkCreate);

		// Fill root layout
		vertical.addComponent(f);
		vertical.setComponentAlignment(f, Alignment.MIDDLE_LEFT);
		vertical.addComponent(descriptorEditor);
		vertical.setComponentAlignment(descriptorEditor, Alignment.MIDDLE_CENTER);
		vertical.addComponent(buttonBar);
		vertical.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);

		refreshLocale();
		this.setContent(vertical);
		// this.setModal(true);
		this.setResizable(true);
		this.setDraggable(true);
	}

	private void initDescriptorEditor()
	{
		descriptorEditor.setWidth("800px");
		descriptorEditor.setHeight("500px");
		descriptorEditor.setStyleName("descriptorEditor");

		// Process a value input by the user from the client-side
		descriptorEditor.addValueChangeListener(new DescriptorEditor.ValueChangeListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = 54993721819727243L;

			@Override
			public void valueChange()
			{
				Notification.show("FROM SERVER : CurrentDescriptor: " + descriptorEditor.getDescriptorContent());
			}
		});
	}

	private void refreshLocale()
	{
		txtDescriptorName.setCaption("Descriptor Name");
		f.setDescription("<h2>Create New Descriptor</h2><p>Choose a descriptor name and fill its content in the corresponding area</p>");
		butOkCreate.setCaption("Create");
		butCancelCreate.setCaption("Cancel");
	}

	public Button getOkCreateButton()
	{
		return butOkCreate;
	}

	public Button getCancelCreateButton()
	{
		return butCancelCreate;
	}
}
