function Colors(){}
Colors.atos = '#01659F';
Colors.black = '#000';

function createGraph(elements){

	sigma.utils.pkg('sigma.canvas.nodes');
	
	$('#graphContainer').empty();

	g = {
			nodes: [],
			edges: []
	};

	//Add nodes to graph
	$.each(elements.nodes, function(key, value) {

		node = value;
		g.nodes.push(node);
	});

	//Add edges to graph
	$.each(elements.edges, function(key, value) {
		
		edge = value;
		
		g.edges.push(edge);
	});


	//Create sigma graph
	graph = new sigma({
		graph: g,
		renderer: {
			container: document.getElementById('graphContainer'),
			type: 'canvas'
		},
		settings: {
			minNodeSize: 8,
			maxNodeSize: 16,
			zoomMin: 0,
			zoomMax: 0,
			doubleClickEnabled: false,
			mouseWheelEnabled: false,
			touchEnbaled: false,
			defaultLabelColor: Colors.atos,
			labelAlignment: 'top',
			scalingMode: 'outside',
			verbose: true
		}
	});

	graph.camera.goTo({ratio: 2.5});
	
	//GRAPH EVENTS
	graph.bind('clickNode',onClickNode);
	graph.bind('click',onClick);
	
	CustomShapes.init(graph);
	graph.refresh();
	
	//CUSTOM CONTEXT MENUS
	var menu = [{
		name: 'Deploy',
		img: 'VAADIN/themes/novaforge/modules/novadeploy/images/deploy.png',
		title: 'create button',
		fun: function () {
			document.getElementById("whatToDo").value = "deploy";
			$('#serverUpdate').click();
		}
	},{
		name: 'Read',
		img: 'VAADIN/themes/novaforge/modules/novadeploy/images/read.png',
		title: 'create button',
		fun: function () {
			document.getElementById("whatToDo").value = "read";
			$('#serverUpdate').click();
		}
	},{
		name: 'Fork',
		img: 'VAADIN/themes/novaforge/modules/novadeploy/images/fork.png',
		title: 'create button',
		fun: function () {
			document.getElementById("whatToDo").value = "fork";
			$('#serverUpdate').click();
		}
	}, {
		name: 'Delete',
		img: 'VAADIN/themes/novaforge/modules/novadeploy/images/delete.png',
		title: 'update button',
		fun: function () {
			document.getElementById("whatToDo").value = "delete";
			$('#serverUpdate').click();
		}
	}, {
		name: 'Delete Sub',
		img: 'VAADIN/themes/novaforge/modules/novadeploy/images/deletesub.png',
		title: 'create button',
		fun: function () {
			document.getElementById("whatToDo").value = "delete sub";
			$('#serverUpdate').click();
		}
	}];
	//Calling context menu
	$('#graphContainer').contextMenu(menu);

}

//EVENT CALLBACKS

function onClickNode(event){
	document.getElementById("currentDescriptor").value = event.data.node.label; 
	$('#graphContainer').contextMenu('open',{top:event.data.captor.clientY,left:event.data.captor.clientX});
}

function onClick(event){
	$('#graphContainer').contextMenu('close');
}