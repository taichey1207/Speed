package speed;

/**
 * トランプのカードを表すクラス
 *
 * @author 渡辺
 */

public class Card {
  /** トランプのマーク */
  public enum Suit {
    /** ♣ */
    CLUBS,
    /** ♦ */
    DIAMONDS,
    /** ♥ */
    HEARTS,
    /** ♠ */
    SPADES
  }

  /** カードの数字(1から13) */
  private int number;

  /** カードのマーク */
  private Suit suit;

  /**
   * 数字を返す
   *
   * @return number
   */
  public int getNumber() {
    return this.number;
  }

  /**
   * マークを返す
   *
   * @return suit
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * 数字を設定する
   *
   * @param number 数字
   */
  public void setNumber(int number) {
    this.number = number;
  }

  /**
   * マークを設定する
   *
   * @param suit マーク
   */
  public void setSuit(Suit suit) {
    this.suit = suit;
  }

  /**
   * @param suit   マーク
   * @param number 数字
   */
  public Card(final Suit suit, final int number) {
    this.suit = suit;
    this.number = number;
  }

  /** 
   * コピーコンストラクタ
   * Cardオブジェクトが複製される
   * @param card 複製元のカード
   */
  public Card(final Card card) {
	  this.suit = card.suit;
	  this.number = card.number;
  }

  /**
   * TODO<br>
   * カードの数字を文字列に変換<br>
   * (e.g. 1 → "A", 2 → "2", 11 → "J")
   *
   * @return 変換後の文字列
   * @author 渡辺
   */
  public String numberToString() {
    /* 未実装 */
	String num;

	if(this.number==1) {
		num = "A";
	}else if(this.number==11) {
		num = "J";
	}else if(this.number==12) {
		num = "Q";
	}else if(this.number==13) {
		num = "K";
	}else {
		num = String.valueOf(this.number);
	}

	return  num;
  }

  /**
   * TODO<br>
   * カードのマークを文字列に変換<br>
   * (e.g. Suit.CLUBS → "♣")
   *
   * @return 変換後の文字列
   * @author 渡辺
   */
  public String suitToString() {
    /* 未実装 */
	String suit;
 
	if(this.suit.equals(Suit.CLUBS)) {
		suit = "♣";
	}else if(this.suit.equals(Suit.DIAMONDS)) {
		suit = "♦";
	}else if(this.suit.equals(Suit.HEARTS)) {
		suit = "♥";
	}else {
		suit = "♠";
	}
 
	return suit;
  }
}
