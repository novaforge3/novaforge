window.org_novaforge_forge_ui_novadeploy_internal_client_components_DescriptorTree = function() {
    
	// Create the component
    var descriptorTree = new descriptorTreeLibrary.DescriptorTree(this.getElement());
    console.log("tree connector");
    
    // Handle changes from the server-side
    this.onStateChange = function() {
    	descriptorTree.setCurrentDescriptor(this.getState().currentDescriptor);
    	descriptorTree.setCurrentDescriptorVersion(this.getState().currentDescriptorVersion);
    	descriptorTree.setWhatToDo(this.getState().whatToDo);
    	descriptorTree.setDescriptorList(this.getState().descriptorList);
    	descriptorTree.setIsTester(this.getState().isTester);
    };
    
    // Pass user interaction to the server-side
    var self = this;
    
    descriptorTree.click = function() {
    	
    	var state = new Array();
    	state.push(descriptorTree.getCurrentDescriptor());
    	state.push(descriptorTree.getCurrentDescriptorVersion());
    	state.push(descriptorTree.getWhatToDo());
    	
    	console.log(state);
    	
    	self.onClick(state);
    };
};
