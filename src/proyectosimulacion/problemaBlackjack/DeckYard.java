package proyectosimulacion.problemaBlackjack;

import java.util.ArrayList;

public class DeckYard extends ArrayList<Card> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeckYard() {
		for (int i = 0; i < 52; i++) {
			super.add(new Card(i));
			super.add(new Card(i));
		}
	}
	
}
