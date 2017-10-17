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
			touchEnabled: false,
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


}

//2 CUSTOM CONTEXT MENUS
//For versionned descriptor
var menuVersionned = [{
	name: 'Deploy',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/deploy.png',
	title: 'Deploy this descriptor as a new environment',
	fun: function () {
		document.getElementById("whatToDo").value = "deploy";
		$('#serverUpdate').click();
	}
},{
	name: 'Read',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/read.png',
	title: 'Read this descriptor\'s content',
	fun: function () {
		document.getElementById("whatToDo").value = "read";
		$('#serverUpdate').click();
	}
},{
	name: 'Fork',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/fork.png',
	title: 'Fork this descriptor',
	fun: function () {
		document.getElementById("whatToDo").value = "fork";
		$('#serverUpdate').click();
	}
}];

//For unversionned descriptor
var menuUnversionned = [{
	name: 'Deploy',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/deploy.png',
	title: 'Deploy this descriptor as a new environment',
	fun: function () {
		document.getElementById("whatToDo").value = "deploy";
		$('#serverUpdate').click();
	}
},{
	name: 'Read',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/read.png',
	title: 'Read this descriptor\'s content',
	fun: function () {
		document.getElementById("whatToDo").value = "read";
		$('#serverUpdate').click();
	}
},{
	name: 'Edit',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/edit.png',
	title: 'Edit this descriptor\'s content',
	fun: function () {
		document.getElementById("whatToDo").value = "edit";
		$('#serverUpdate').click();
	}
},{
	name: 'Release',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/tick.png',
	title: 'Make this descriptor as a new release version',
	fun: function () {
		document.getElementById("whatToDo").value = "release";
		$('#serverUpdate').click();
	}
}];

//TESTERS MENU
var menuTester = [{
	name: 'Deploy',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/deploy.png',
	title: 'Deploy this descriptor as a new environment',
	fun: function () {
		document.getElementById("whatToDo").value = "deploy";
		$('#serverUpdate').click();
	}
},{
	name: 'Read',
	img: 'VAADIN/themes/novaforge/modules/novadeploy/images/read.png',
	title: 'Read this descriptor\'s content',
	fun: function () {
		document.getElementById("whatToDo").value = "read";
		$('#serverUpdate').click();
	}
}];


//EVENT CALLBACKS

function onClickNode(event){
	document.getElementById("currentDescriptor").value = event.data.node.name;
	document.getElementById("currentDescriptorVersion").value = event.data.node.version;

	var isTester = false;

	if(document.getElementById("isTester").value == "true"){
		isTester = true;
	}

	if(isTester){
		$('#graphContainer').contextMenu(menuTester);
	}else{
		if(event.data.node.version != 0){
			$('#graphContainer').contextMenu(menuVersionned);
		}else{
			$('#graphContainer').contextMenu(menuUnversionned);
		}
	}

	$('#graphContainer').contextMenu('open',{top:event.data.captor.clientY,left:event.data.captor.clientX});
}

function onClick(event){
	$('#graphContainer').contextMenu('close');
}