window.org_novaforge_forge_ui_novadeploy_internal_client_components_DescriptorTree = function() {
    
	// Create the component
    var descriptorTree = new descriptorTreeLibrary.DescriptorTree(this.getElement());
    
    // Handle changes from the server-side
    this.onStateChange = function() {
    	descriptorTree.setCurrentDescriptor(this.getState().currentDescriptor);
    	descriptorTree.setWhatToDo(this.getState().whatToDo);
    };
    
    initDescriptorTree();

    // Pass user interaction to the server-side
    var self = this;
    
    descriptorTree.click = function() {
    	
    	var state = new Array();
    	state.push(descriptorTree.getCurrentDescriptor());
    	state.push(descriptorTree.getWhatToDo());
    	
    	console.log(state);
    	
    	self.onClick(state);
    };
};
