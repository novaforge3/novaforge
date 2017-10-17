//Define the namespace
var descriptorEditorLibrary = descriptorEditorLibrary || {};

descriptorEditorLibrary.DescriptorEditor = function (element) {
	element.innerHTML = 
		"<div id='aceEditor'></div>"+
		"<input id='descriptorContent' type='hidden' name='descriptorContent'/>"+
		"<input id='serverUpdate' type='hidden'/>";

	// Style it
	element.style.border = "thin solid black";
	element.style.display = "inline-block";

	// Getter and setter for the value property
	this.getDescriptorContent = function () {
		//return document.getElementById("aceEditor").innerHTML;
		return editor.getValue();
	};

	this.setDescriptorContent = function (descriptorContent) {
		//var obj = document.getElementById("aceEditor").innerHTML = descriptorContent;
		editor.setValue(descriptorContent);
	};

	// Default implementation of the click handler
	this.click = function () {
		alert("Error: Must implement click() method");
	};

	// Set up button click
	var button = document.getElementById("serverUpdate");
	console.log(button);
	var self = this; // Can't use this inside the function
	button.onclick = function () {
		self.click();
	};
};