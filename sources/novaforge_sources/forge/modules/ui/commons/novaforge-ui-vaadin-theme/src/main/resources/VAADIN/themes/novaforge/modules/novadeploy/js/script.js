var currentDescriptor = null;

function initDescriptorTree() {
	$('#graphContainer').bind('contextmenu', function(e){
	    e.preventDefault();
	});
	
	  var elements = {
				"nodes": [
				          {
				        	  "id": "a1",
				        	  "label": "descriptorA-1"+document.getElementById("whatToDo").value,
				        	  "type":"square",
				        	  "x": 0,
				        	  "y": 0,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          },
				          {
				        	  "id": "a2",
				        	  "label": "descriptorA-2"+document.getElementById("whatToDo").value,
				        	  "type":"square",
				        	  "x": 0.5,
				        	  "y": 0,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          },
				          {
				        	  "id": "a3",
				        	  "label": "descriptorA-3"+document.getElementById("whatToDo").value,
				        	  "type":"square",
				        	  "x": 1,
				        	  "y": 0,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          },
				          {
				        	  "id": "b1",
				        	  "label": "descriptorB-1",
				        	  "type":"square",
				        	  "x": 1,
				        	  "y": 0.5,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          },
				          {
				        	  "id": "b2",
				        	  "label": "descriptor-B-2",
				        	  "type":"square",
				        	  "x": 1.5,
				        	  "y": 0.5,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          },
				          {
				        	  "id": "c3",
				        	  "label": "descriptor-C-3",
				        	  "type":"square",
				        	  "x": 2,
				        	  "y": 1,
				        	  "size": 3,
				        	  "color": Colors.atos,
				        	  "image": {
				        		  url:'VAADIN/themes/novaforge/modules/novadeploy/images/descriptor.png'
				        	  }
				          }
				          ],
		          "edges": [
		                    {
		                    	"id": "e0",
		                    	"source": "a1",
		                    	"target": "a2",
		                    	"color": Colors.black
		                    },
		                    {
		                    	"id": "e1",
		                    	"source": "a2",
		                    	"target": "a3",
		                    	"color": Colors.black
		                    },
		                    {
		                    	"id": "e2",
		                    	"source": "a2",
		                    	"target": "b1",
		                    	"color": Colors.black
		                    },
		                    {
		                    	"id": "e3",
		                    	"source": "b1",
		                    	"target": "b2",
		                    	"color": Colors.black
		                    },
		                    {
		                    	"id": "e4",
		                    	"source": "b2",
		                    	"target": "c3",
		                    	"color": Colors.black
		                    }
		                    ]
		}

	  createGraph(elements);
};
