var editor = null;

initEditor = function(){
	
	console.log('ace init !');
	
	editor = ace.edit("aceEditor");
    editor.setTheme("ace/theme/cobalt");
    editor.getSession().setMode("ace/mode/xml");

};