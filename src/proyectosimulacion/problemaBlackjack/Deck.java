package proyectosimulacion.problemaBlackjack;

import java.util.ArrayList;

public class Deck {
	
	static Deck deck;
    
	ArrayList<Card> cards;
	ArrayList<Card> deckYard;
    
	/**
	 * Constructor el cual crea un @ArrayList de objetos @Card (Carta) y un
	 * @ArrayList de enteros el cual representaria las cartas que se desechan
	 * para cuando se acabe el mazo nuevamente se agregen a este.
	 * Y se ejecuta la funcion @shuffDeck la cual baraja el par de mazos.
	 */
    public Deck() {
    	cards = new ArrayList<Card>();
    	deckYard = new DeckYard();
    	
    	shuffDeck();
    }
    
    /**
     * La funcion @shuffDeck toma un numero aleatorio del tamano del mazo de cartas
     * descartadas y lo agrega al mazo de cartas a usar de esta manera se barajan
     * las cartas.
     */
    public void shuffDeck(){
    	int j;
    	for(int i=cards.size(); i<104; i++){
    		j = getRandom(deckYard.size());
    		cards.add(deckYard.get(j));
    		deckYard.remove(j);
    	}
    }
    
    /**
     * La funcion @getCard retorna la primera carta en el mazo y agrega su codigo
     * a la pila de cartas descartadas.
     * @return
     */
    public Card getCard(){
    	Card mw = cards.get(0);
    	cards.remove(0);
    	deckYard.add(mw);
    	return mw;
    }
    
    /**
     * Funcion @getRandom devuelte un numero aleatorio de rango 0 hasta el parametro insertado.
     * @param l es el rango maximo del numero aleatorio
     * @return retorna un numero aleatorio
     */
    private int getRandom(int l) {
		return (int) (Math.random()*l);
	}

    /**
     * Metodo @Singleton del mazo @Deck
     * @return (Retorna un solo mazo siempre)
     */
	public static Deck getDeck() {
        if(deck == null){
            deck = new Deck();
            return deck;
        }
        return deck;
    }
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	public ArrayList<Card> getDeckYard() {
		return deckYard;
	}
	
}
