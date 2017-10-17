// Define the namespace
var descriptorTreeLibrary = descriptorTreeLibrary || {};

descriptorTreeLibrary.DescriptorTree = function (element) {
	element.innerHTML =
		"<div id='graphContainer'>" +
		"</div>" +
		"<div>" +
		"<input id='whatToDo' type='hidden' name='whatToDo'/>" +
		"<input id='currentDescriptor' type='hidden' name='currentDescriptor'/>" +
		"<input id='currentDescriptorVersion' type='hidden' name='currentDescriptorVersion'/>" +
		"<input id='descriptorList' type='hidden' name='descriptorList'/>" +
		"<input id='isTester' type='hidden' name='isTester'/>" +
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
	
	this.getCurrentDescriptorVersion = function () {
		return document.getElementById("currentDescriptorVersion").value;
	};
	
	this.setCurrentDescriptorVersion = function (value) {
		document.getElementById("currentDescriptorVersion").value = value;
	};
	
	this.getWhatToDo = function () {
		return document.getElementById("whatToDo").value;
	};
	
	this.setWhatToDo = function (value) {
		document.getElementById("whatToDo").value = value;
	};
	
	this.getDescriptorList = function () {
		return document.getElementById("descriptorList").value;
	};
	
	this.setDescriptorList = function (value) {
		document.getElementById("descriptorList").value = value;
		initDescriptorTree();
	};
	
	this.getIsTester = function () {
		return document.getElementById("isTester").value;
	};
	
	this.setIsTester = function (value) {
		console.log("Coucou "+value);
		
		document.getElementById("isTester").value = value;
		initDescriptorTree();
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