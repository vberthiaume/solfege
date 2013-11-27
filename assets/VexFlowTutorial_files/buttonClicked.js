	function buttonClicked(){
		
		//generate random chord from mainGauche
		var chord = maingauche.genereAccordAbc(Math.floor((Math.random()*7)+1));

		//write chord in button just for fun
		document.getElementById('myButton').value = chord;
		
		//get that chord in array form, splitting on spaces
		var chordArray = chord.split(" ");
		
		// Create the notes
		var notes = [
		  new Vex.Flow.StaveNote({ keys: chordArray, duration: "w" })
		];
		
		// Create a voice in 4/4
		var voice = new Vex.Flow.Voice({
		  num_beats: 4,
		  beat_value: 4,
		  resolution: Vex.Flow.RESOLUTION
		});
		
		// Add notes to voice
		voice.addTickables(notes);
		
		// Format and justify the notes to 200 pixels
		var formatter = new Vex.Flow.Formatter().joinVoices([voice]).format([voice], 200);
		
		// Render voice... this is weird because the staves are defined in another file, in vexFlowTutorial...
		voice.draw(ctx, stave);
		voice.draw(ctx, stave2);
		
	}