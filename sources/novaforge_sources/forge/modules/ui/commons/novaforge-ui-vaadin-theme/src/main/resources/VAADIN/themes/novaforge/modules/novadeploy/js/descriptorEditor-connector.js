window.org_novaforge_forge_ui_novadeploy_internal_client_components_DescriptorEditor = function() {
    
	// Create the component
    var descriptorEditor = new descriptorEditorLibrary.DescriptorEditor(this.getElement());
    
    initEditor();
    
    // Handle changes from the server-side
    this.onStateChange = function() {
    	console.log("onStateChange "+this.getState());
    	descriptorEditor.setDescriptorContent(this.getState().descriptorContent);
    };

    // Pass user interaction to the server-side
    var self = this;
    
    descriptorEditor.click = function() {
    	self.onClick(descriptorEditor.getDescriptorContent());
    };
};
