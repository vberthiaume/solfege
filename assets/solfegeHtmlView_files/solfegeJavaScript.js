//this function creates and adds a root note to the stave
var curStave = null;
var root = null;
var rootArray = null;

function createRoot(){
	try{
		//get current root note
		root = rightHand.getCurrentMidiRootNote();
		//console.log("root: " + root);
//		alert("root: " + root);
//		document.getElementById('myButton').value = "rootArray";

		curStave = rightHand.getCurrentStave();

		if ( curStave == "treble"){
			
			//get that root in array form, splitting on spaces
			root = root + "/4"
			//console.log("root: " + root);
			rootArray = root.split(" ");
			//console.log("rootArray: " + rootArray);
			
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

			// Format and justify the notes
			var formatter = new Vex.Flow.Formatter().joinVoices([voice]).format([voice], canvas.width*.99);
			
			// Render voice 
			voice.draw(ctx, stave);

		} else {
			
			//get that root in array form, splitting on spaces
			root = root + "/3"
			rootArray = root.split(" ");
		
			var bassNotes = [
		   		 new Vex.Flow.StaveNote({ keys: rootArray, duration: "q", clef: "bass" })
		   	];
			
			// Create a voice in 1/4
			var bassVoice = new Vex.Flow.Voice({
			  num_beats: 1,
			  beat_value: 4,
			  resolution: Vex.Flow.RESOLUTION
			});
			
			// Add notes to voice
			bassVoice.addTickables(bassNotes);
			
			// Format and justify the notes
			var formatter = new Vex.Flow.Formatter().joinVoices([bassVoice]).format([bassVoice], canvas.width*.99);
			
			// Render voice 
			bassVoice.draw(ctx, stave2);
		}
		
	
	} catch (e) {
		console.log("createRoot(): " + e); 
	}
}

//this function creates and adds a root note to the stave
function createGuessNote(guessNote){
	try{
		//get current guess note
		var guessNote = rightHand.getCurrentMidiGuessNote();
		//get that root in array form, splitting on spaces

		if ( curStave == "treble"){
			
			guessNote = guessNote + "/4";
			guessNoteArray = guessNote.split(" ");
			
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
		} else {
			
			guessNote = guessNote + "/3";
			guessNoteArray = guessNote.split(" ");
			
			// Create the notes
			var notes = [
			  new Vex.Flow.StaveNote({ keys: rootArray, duration: "q", clef: "bass" }),
			  new Vex.Flow.StaveNote({ keys: guessNoteArray, duration: "q", clef: "bass" })
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
			voice.draw(ctx, stave2);
		}
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
			
		stave = new Vex.Flow.Stave(0, - canvas.height*.15, canvas.width*.99);
		stave.addClef("treble").setContext(ctx).draw();
		
		stave2 = new Vex.Flow.Stave(0, canvas.height*.4, canvas.width*.99);
		stave2.addClef("bass").setContext(ctx).draw();
		


	} catch (e) {
		console.log("reset(): " + e); 
	}
}


//this function creates and adds chords to the stave(s)
function createChord(){
	try{
		
		//generate random chord from mainGauche
		var chord = leftHand.genereAccordAbc(Math.floor((Math.random()*7)+1));
	
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