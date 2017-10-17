// Define the namespace
var descriptorTreeLibrary = descriptorTreeLibrary || {};

descriptorTreeLibrary.DescriptorTree = function (element) {
	element.innerHTML =
		"<div id='graphContainer'>" +
		"</div>" +
		"<div>" +
		"<input id='whatToDo' type='hidden' name='whatToDo'/>" +
		"<input id='currentDescriptor' type='hidden' name='currentDescriptor'/>" +
		"<input id='serverUpdate' type='hidden' value='Click'/>" +
		"</div>";

	// Style it
	element.style.border = "thin solid black";
	element.style.display = "inline-block";

	// Getter and setter for the value property
	this.getCurrentDescriptor = function () {
		return document.getElementById("currentDescriptor").value;
	};
	
	this.setCurrentDescriptor = function (value) {
		document.getElementById("currentDescriptor").value = value;
	};
	
	this.getWhatToDo = function () {
		return document.getElementById("whatToDo").value;
	};
	
	this.setWhatToDo = function (value) {
		document.getElementById("whatToDo").value = value;
	};

	// Default implementation of the click handler
	this.click = function () {
		alert("Error: Must implement click() method");
	};

	// Set up button click
	var button = document.getElementById("serverUpdate");
	var self = this; // Can't use this inside the function
	button.onclick = function () {
		self.click();
	};
};