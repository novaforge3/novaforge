var currentDescriptor = null;
var currentVersion = 0;
var X_step = 0.85;
var Y_step = 0.35;
// reset those in the function
var lastX = 0, lastY = 0;
var alreadyAssigned = new Object();
var edgeNumber = 0;

window.onerror = function(msg, url, linenumber) {
    console.log('Error message: '+msg+'\nURL: '+url+'\nLine Number: '+linenumber);
    return true;
}

function resetData(){
	edgeNumber = 0;
	alreadyAssigned = new Object();
	lastX = 0
	lastY = 0;
	
}
function computeNode(parentDescriptor,elements,parentXPos,parentYPos) {

	for(var i = 0; i < parentDescriptor.sons.length; i++) {
		//Current descriptor
		var descriptor = parentDescriptor.sons[i];


		if(descriptor.version == currentVersion){
			// if its a fork -> need to find where to put the node
			if(parentDescriptor.name != descriptor.name){
				var element = {
						"id": descriptor.name+"-"+descriptor.version,
						"label": descriptor.name+"-current",
						"name": descriptor.name,
						"version": descriptor.version,
						"type":"square",
						"x": parentXPos + X_step,
						"y": lastY,
						"size": 3,
						"color": Colors.atos,
						"image": {
							url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
						}
				}	
				
				lastY+=Y_step;
				
			}else{
				var element = {
						"id": descriptor.name+"-"+descriptor.version,
						"label": descriptor.name+"-current",
						"name": descriptor.name,
						"version": descriptor.version,
						"type":"square",
						"x": parentXPos + X_step,
						"y": parentYPos,
						"size": 3,
						"color": Colors.atos,
						"image": {
							url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
						}
				}
			}
			
		}else{

			// if its a fork -> need to find where to put the node
			if(parentDescriptor.name != descriptor.name){
				var element = {
						"id": descriptor.name+"-"+descriptor.version,
						"label": descriptor.name+"-"+descriptor.version,
						"name": descriptor.name,
						"version": descriptor.version,
						"type":"square",
						"x": parentXPos + X_step,
						"y": lastY,
						"size": 3,
						"color": Colors.atos,
						"image": {
							url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
						}
				}
				var curY = lastY;
				lastY+=Y_step;
				computeNode(descriptor,elements,parentXPos + X_step,curY);

			}else{
				var element = {
						"id": descriptor.name+"-"+descriptor.version,
						//"label": descriptor.name+"-"+descriptor.version,
						"label": ""+descriptor.version,
						"name": descriptor.name,
						"version": descriptor.version,
						"type":"square",
						"x": parentXPos + X_step,
						"y": parentYPos,
						"size": 3,
						"color": Colors.atos,
						"image": {
							url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
						}
				}
				computeNode(descriptor,elements,parentXPos + X_step,parentYPos);
			}
		}
		elements.nodes.push(element);
		var edge = {
				"id": "e"+edgeNumber,
				"source": parentDescriptor.name+"-"+parentDescriptor.version,
				"target": descriptor.name+"-"+descriptor.version

		}
		edgeNumber++;
		elements.edges.push(edge);
	}
	
}	


function computeNodes(descriptorsArray,elements) {
	
	for(var i = 0; i < descriptorsArray.length; i++) {
		//Current descriptor
		var descriptor = descriptorsArray[i];

		if(descriptor.version == currentVersion){
			var element = {
					"id": descriptor.name+"-"+descriptor.version,
					"label": descriptor.name+"-current",
					"name": descriptor.name,
					"version": descriptor.version,
					"type":"square",
					"x": 0,
					"y": lastY,
					"size": 3,
					"color": Colors.atos,
					"image": {
						url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
					}
			}
		}else{

			var element = {
					"id": descriptor.name+"-"+descriptor.version,
					"label": descriptor.name+"-"+descriptor.version,
					"name": descriptor.name,
					"version": descriptor.version,
					"type":"square",
					"x": 0,
					"y": lastY,
					"size": 3,
					"color": Colors.atos,
					"image": {
						url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
					}
			}
			var curY = lastY;
			lastY+=Y_step;
			computeNode(descriptor,elements,0,curY);
		}
		lastY+=Y_step;
		elements.nodes.push(element);
	}
	
}	
function initDescriptorTree() {
	$('#graphContainer').bind('contextmenu', function(e){
		e.preventDefault();
	});
	
	//Populate graph from descriptors json
	var descriptorsArray = JSON.parse(document.getElementById("descriptorList").value);

	var elements = {
			"nodes": [],
			"edges": []
	}
	
	computeNodes(descriptorsArray,elements);
	
	createGraph(elements);
};