
import java.awt.*;
import java.util.Vector;
import guidoengine.*;

public class factorysample extends Canvas
{
	static {
        try {
			System.loadLibrary("jniGUIDOEngine");
			guido.Init("Guido2", "Times");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
		}
	}
	                                                                                               
	public guidoscore fScore;
	
	public factorysample(Vector mesure[] , int size) {
		fScore = makeScore(mesure , size);
	}

	public void addEvent(guidofactory f, String note, int n, int d, int accident, int octave, int dots) {
		f.OpenEvent(note);
		f.SetDuration(n, d);
		f.SetOctave(octave);
		f.SetEventDots(dots);
		f.SetEventAccidentals(accident);
		f.CloseEvent();
	}

	public void addTag(guidofactory f, String name) {
		f.OpenTag (name, 0);
		f.CloseTag();
		f.EndTag();
	}

	public void addTag(guidofactory f, String name, int param) {
		f.OpenTag (name, 0);
		f.AddTagParameterInt(param);
		f.CloseTag();
		f.EndTag();
	}

	public void addTag(guidofactory f, String name, String param) {
		f.OpenTag (name, 0);
		f.AddTagParameterString(param);
		f.CloseTag();
		f.EndTag();
	}

	public guidoscore makeScore(Vector mesure[] , int size) {
		guidofactory f = new guidofactory();
		f.Open();
		f.OpenMusic();

		f.OpenVoice();
		addTag (f, "key", "C");
		addTag (f, "clef", "g");
		addTag (f, "meter", "C");
		
		
		f.CloseVoice();


		f.OpenVoice();
		addTag (f, "key", "C");
		addTag (f, "clef", "f");
		addTag (f, "meter", "C");
		
		for(int i = 0 ; i < size ; i++){
			int accordsize = mesure[i].size();
			f.OpenChord();
			for(int j = 0 ; j < accordsize ; j++){
				addEvent(f, mesure[i].get(j).toString(), 1, 4, 0, 1, 0);
				
			}
			f.CloseChord();
		}
		f.CloseVoice();
		
		guidoscore score = new guidoscore(f.CloseMusic());
		int err = score.AR2GR();
		if (err != guido.guidoNoErr)
			System.out.println("score.AR2GR error  : " + guido.GetErrorString (err));
		score.ResizePageToMusic();
		f.Close();
		return score;
	}

    public	void background_paint(Graphics g) {
		Color c1 = new Color (255,255,255);
		Color c2 = new Color (255,255,255);
		int n = 7, border=10;
		int w = (getSize().width-(2*border)) / n;
		int h = (getSize().height-(2*border)) / n;
		int x = border, y = border;
		for (int i=0; i<n*n; i++) {
			if ( (i%n) == 0 ) {
				x = border;
				y = border + (h * i/n);
			}
			g.setColor( (i & 1) == 0 ? c1 : c2 );
			g.fillRect (x, y, w, h);
			x += w;
		}
	}

    public	void paint(Graphics g) {
		background_paint (g);
		guidodrawdesc desc = new guidodrawdesc(getSize().width, getSize().height);
		int ret = fScore.Draw (g, getSize().width, getSize().height, desc, new guidopaint(), new Color(0,0,0,255));
		if (ret != guido.guidoNoErr)
			System.err.println("error drawing score: " + guido.GetErrorString(ret));
		g.setColor(new Color (255,0,0,80));
		//g.fillOval (getSize().width/2-40, getSize().height/2-40, 80, 80);
	}

	
    public static void main (String args[]) {
		
    }
}
