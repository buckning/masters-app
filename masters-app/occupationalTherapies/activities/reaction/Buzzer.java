package occupationalTherapies.activities.reaction;

import demos.MidiSynthesizer;

public class Buzzer extends MidiSynthesizer{
	private boolean disabled;
	public Buzzer(){
		this.setVoice(15);
		disabled = false;
	}
	
	public void playSound(){
		if(!disabled)super.playSound(73, 1000);
	}
	
	public void disable(){
		disabled = true;
	}
}
