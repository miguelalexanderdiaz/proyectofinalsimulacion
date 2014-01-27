package proyectosimulacion.problemaBlackjack;

import java.util.ArrayList;

public class Card {
	
	/**
     * Cada 'Carta' tiene su codigo de 6 bits (hasta 63 en decimal)
     * lo cual identifica el numero de la carta y su pinta:
     * (Las cartas solo van de 0 a 51)
     * 
     * 'A'  : '0000'
     * '2'  : '0001'
     * '3'  : '0010'
     * '4'  : '0011'
     * '5'  : '0100'
     * '6'  : '0101'
     * '7'  : '0110'
     * '8'  : '0111'
     * '9'  : '1000'
     * '1'  : '1001'                 --> seria 10
     * 'J'  : '1010'
     * 'Q'  : '1011'
     * 'K'  : '1100'
     * 
     * y las pintas:
     * 
     * 'Corazon'  : '00'
     * 'Trebol'   : '01'
     * 'Diamante' : '10'
     * 'Pica'     : '11'
     * 
     * Un codigo al azar: '001101' siendo la carta: '4' de 'Trebol'
     */
	
	int code;
	String v_code;
	
	/**
	 * Constructor el cual solo valida un numero de codigo entre 0 y 51
	 * @param code
	 */
	public Card(int code) {
		if(code >=0 || code < 52)
			this.code = code;
		else
			this.code = 0;
		
		this.v_code = this.getCard();
	}
	
	/**
	 * Funciona para retornar el codigo de la carta
	 * @return
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * Funcion la cual retorna un @String con el valor de la carta como se denotaron
	 * al principio.
	 * @return
	 */
	public String getCard(){
		char c, p;
		
		String tmp = Integer.toBinaryString(this.code);
		
		if(tmp.length() < 2) tmp = "00".concat(tmp);
		if(tmp.length() < 3) tmp = "0".concat(tmp);
		
		switch(Integer.parseInt(tmp.substring(tmp.length()-2), 2)){
		case 0:
			p = 'C';
			break;
		case 1:
			p = 'T';
			break;
		case 2:
			p = 'D';
			break;
		default:
			p = 'P';
		}
		
		int num = Integer.parseInt(tmp.substring(0, tmp.length()-2),2);
		
		if(num == 0) c = 'A';
		else c = String.valueOf(num + 1).charAt(0);
		if(num == 10) c = 'J';
		if(num == 11) c = 'Q';
		if(num == 12) c = 'K';
		
		tmp = String.valueOf(c);
		tmp = tmp + String.valueOf(p);
		
		return tmp;
	}
	
	/**
	 * Suma un arraylist de cartas y retorna el resultado
	 * @param c ArrayList de @Card
	 * @return @int
	 */
	public static int sumaCartas(ArrayList<Card> c){
		int sum = 0;
		for (int i = 0; i < c.size(); i++) {
			sum += c.get(i).getValue();
		}
		return sum;
	}
	
	/**
	 * Suma un carta y un valor entero previamente sumado
	 * @param c
	 * @param val
	 * @return
	 */
	public static int sumaCartas(Card c, int val){
		return val + c.getValue();
	}
	
	/**
	 * Retorna el valor de la carta segun el juego de BlackJack
	 * (NOTA: Solo retorna 1 para el A's)
	 * @return
	 */
	public int getValue(){
		if(this.v_code.charAt(0) == 'A') return 1;
		if(this.v_code.charAt(0) == '2') return 2;
		if(this.v_code.charAt(0) == '3') return 3;
		if(this.v_code.charAt(0) == '4') return 4;
		if(this.v_code.charAt(0) == '5') return 5;
		if(this.v_code.charAt(0) == '6') return 6;
		if(this.v_code.charAt(0) == '7') return 7;
		if(this.v_code.charAt(0) == '8') return 8;
		if(this.v_code.charAt(0) == '9') return 9;
		else return 10;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.v_code;
	}
}