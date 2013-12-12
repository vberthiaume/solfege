//button clicked, we determine what we'll do based on current state
function htmlButtonClicked(){
	try{
		switch (currentState) {
			case stateEnum.INITIAL:
				createRoot();
				break;
			case stateEnum.GOT_ROOT:
				createGuessNote();
				break;
			case stateEnum.GOT_GUESS_NOTE:
				reset();
				break;
			default:
				document.getElementById('myButton').value = "DEFAULT CASE - ERROR";
				break;
		}
	} catch (e) {
		console.log("htmlButtonClicked(): " + e);
	}
}



//this function creates and adds a root note to the stave
function createRoot(){
	try{
		//generate random root note
		var root = righthand.getNewRootNote();
	
		//get that root in array form, splitting on spaces
		rootArray = root.split(" ");
		
		//document.getElementById('myButton').value = "rootArray";
		
		// Create the notes
		var notes = [
		  new Vex.Flow.StaveNote({ keys: rootArray, duration: "q" })
		];
		
		// Create a voice in 1/4
		var voice = new Vex.Flow.Voice({
		  num_beats: 1,
		  beat_value: 4,
		  resolution: Vex.Flow.RESOLUTION
		});
		
		// Add notes to voice
		voice.addTickables(notes);
		
		// Format and justify the notes to 200 pixels
		var formatter = new Vex.Flow.Formatter().joinVoices([voice]).format([voice], 200);
		
		// Render voice... this is weird because the staves are defined in another file, in vexFlowTutorial...
		voice.draw(ctx, stave);
		//voice.draw(ctx, stave2);
		
		//update current state and button
		currentState = stateEnum.GOT_ROOT;
		document.getElementById('myButton').value = currentState;
	
	} catch (e) {
		console.log("createRoot(): " + e); 
	}
}

//this function creates and adds a root note to the stave
function createGuessNote(){
	
	try{
		//generate random guess note
		var guessNote = righthand.getNewGuessNote();
	
		//get that root in array form, splitting on spaces
		guessNoteArray = guessNote.split(" ");
		
		//document.getElementById('myButton').value = "guessNoteArray";
		
		// Create the notes
		var notes = [
		  new Vex.Flow.StaveNote({ keys: rootArray, duration: "q" }),
		  new Vex.Flow.StaveNote({ keys: guessNoteArray, duration: "q" })
		];
		
		// Create a voice in 4/4
		var voice = new Vex.Flow.Voice({
		  num_beats: 2,
		  beat_value: 4,
		  resolution: Vex.Flow.RESOLUTION
		});
		
		// Add notes to voice
		voice.addTickables(notes);
		
		// Format and justify the notes to 200 pixels
		var formatter = new Vex.Flow.Formatter().joinVoices([voice]).format([voice], 200);
		
		// Render voice... this is weird because the staves are defined in another file, in vexFlowTutorial...
		voice.draw(ctx, stave);
		//voice.draw(ctx, stave2);
		
		//update current state and button
		currentState = stateEnum.GOT_GUESS_NOTE;
		document.getElementById('myButton').value = currentState;
	
	} catch (e) {
		console.log("createGuessNote(): " + e); 
	}
}


function reset(){
	try{
		//clear the canvas
		canvas = document.getElementById('vexFlowCanvas');
	    var context = canvas.getContext('2d');
	    context.clearRect(0, 0, canvas.width, canvas.height);
		

	    //start over
		ctx = renderer.getContext();
			
		stave = new Vex.Flow.Stave(0, 0, canvas.width-10);
		stave.addClef("treble").setContext(ctx).draw();
		
		//update current state and button
		currentState = stateEnum.INITIAL;
		document.getElementById('myButton').value = currentState;
		
		//reset notes
		righthand.resetNotes();
	
	} catch (e) {
		console.log("reset(): " + e); 
	}
}


//this function creates and adds chords to the stave(s)
function createChord(){
	try{
		
		//generate random chord from mainGauche
		var chord = lefthand.genereAccordAbc(Math.floor((Math.random()*7)+1));
	
		//write chord in button just for fun
		//document.getElementById('myButton').value = chord;
		
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
		//voice.draw(ctx, stave2);
	
	} catch (e) {
		console.log("createChord(): " + e); 
	}
	
}