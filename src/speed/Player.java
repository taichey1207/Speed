package speed;

import java.util.ArrayList;

import speed.Card.Suit;

/**
 * プレイヤーを表す抽象クラス
 * 
 * @author 井上
 */

public abstract class Player extends Thread {

  /**
   * 場札のカードの枚数(={@value})
   */
  public static final int AT_HAND_CARDS_SIZE = 4;

  /**
   * 場札
   */
  protected Card[] atHandCards = new Card[AT_HAND_CARDS_SIZE];

  /**
   * 手札
   */
  protected ArrayList<Card> stackedCards = new ArrayList<Card>();

  /**
   * テーブル(共有メモリ)
   */
  protected Table table;

  /**
   * 0か1
   */
  protected int playerID;

  /**
   * スレッドの実行状態を表すフラグ(true=実行,false＝停止)
   */
  private boolean running = true;

  /**
   * 場札において空となったカードの数字
   */
  public static final int EMPTYCARD_NUMBER = -12;

  /**
   * 状態を表す変数(テキスト2.6節準拠)
   */
  protected static boolean A1 = false;
  protected static boolean A2 = false;
  protected static boolean B1 = false;
  protected static boolean B2 = false;

  /**
   * 各プレイヤーがカードを出せるかを表すフラグ(true...可,false...不可)
   */
  protected static boolean Acanputcard = true;
  protected static boolean Bcanputcard = true;

  /**
   * 場札において空となったカードの数字
   */
  public static final int WAITTIME = 10000;

  /** @return playerID */
  public int getPlayerID() {
    return this.playerID;
  }

  /**
   * TODO<br>
   * カードを置けるか調べ、置けるなら置く。<br>
   * 教科書p15のプログラムを実装する。<br>
   * playerIDが0ならAlice, 1ならBobのプログラムを実行。<br>
   * (必要に応じてフィールド・メソッドを追加して下さい。 メッセージの代わりに変数を使って実現できると思います。)
   * 
   * @param card  置こうとするカード
   * @param place カードの位置(0か1)
   * @return カードを置けたかどうか
   * @author 井上
   */
  public boolean tryPutCard(final Card card, final int place) {
    int cardnum = card.getNumber();
    int centernum = this.table.getCentralCard(place).getNumber();
    int gap = Math.abs(cardnum - centernum);
    if ((gap == 1) || (gap == 12)) {
      if (this.playerID == 0) { // Alice
        A1 = true;
        if (B2) {
          A2 = true;
        } else {
          A2 = false;
        }

        while (B1 && ((A2 && B2) || (!A2 && !B2))) {
          continue;
        }

        synchronized (this) {
          A1 = false;
          if ((gap == 1) || (gap == 12)) {
            this.table.putCard(card, place);
            Acanputcard = true;
            return true;
          } else {
            Acanputcard = false;
            return false;
          }
        }

      } else { // Bob
        B1 = true;
        if (!A2) {
          B2 = true;
        } else {
          B2 = false;
        }

        while (A1 && ((A2 && !B2) || (!A2 && B2))) {
          continue;
        }

        synchronized (this) {
          B1 = false;
          if ((gap == 1) || (gap == 12)) {
            this.table.putCard(card, place);
            Bcanputcard = true;
            return true;
          } else {
            Bcanputcard = false;
            return false;
          }
        }

      }
    } else {
      return false;
    }
  }

  /**
   * TODO<br>
   * カードを置けるか調べ、置けるなら置く。<br>
   * playerIDが0ならAlice, 1ならBobのプログラムを実行。<br>
   * (必要に応じてフィールド・メソッドを追加して下さい。 メッセージの代わりに変数を使って実現できると思います。)
   * 
   * @param card  置こうとするカード
   * @param place カードの位置(0か1)
   * @return カードを置けたかどうか
   * @author 井上
   */
  public boolean tryPutCard_prototype(final Card card, final int place) {
    int cardnum = card.getNumber();
    int centernum = this.table.getCentralCard(place).getNumber();
    int gap = Math.abs(cardnum - centernum);
    if ((gap == 1) || (gap == 12)) {
      if (this.playerID == 0) { // Alice
        if ((gap == 1) || (gap == 12)) {
          try {
            Thread.sleep(WAITTIME);
          } catch (InterruptedException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
          }
          this.table.putCard(card, place);
          Acanputcard = true;
          return true;
        } else {
          Acanputcard = false;
          return false;
        }
      } else { // Bob
        if ((gap == 1) || (gap == 12)) {
          this.table.putCard(card, place);
          Bcanputcard = true;
          return true;
        } else {
          Bcanputcard = false;
          return false;
        }
      }
    } else {
      return false;
    }
  }

  /**
   * TODO<br>
   * 場札のカードを削除する<br>
   * 必要なら手札からカードを1枚補充する
   * 
   * @param place カードの位置(0からAT_HAND_CARDS_SIZE-1)
   * @author 井上
   */
  public void removeCard(final int place) {
    int len = this.getStackedCardsLength();
    if (len > 0) {
      Card topcard = this.stackedCards.remove(len - 1);
      int number = topcard.getNumber();
      Suit suit = topcard.getSuit();
      this.atHandCards[place].setNumber(number);
      this.atHandCards[place].setSuit(suit);
    } else {
      this.atHandCards[place].setNumber(EMPTYCARD_NUMBER);
    }
  }

  /**
   * TODO
   * 
   * @return 場札と手札を合わせてカードを1枚も持っていなければtrue
   * @author 井上
   */
  public boolean hasNoCard() {
    if (this.getStackedCardsLength() > 0) {
      return false;
    } else {
      for (Card c : this.atHandCards) {
        if (c.getNumber() > 0) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * @param place カードの位置(0からAT_HAND_CARDS_SIZE-1)
   * @return 場札のカード
   */
  public Card getAtHandCard(final int place) {
    return this.atHandCards[place];
  }

  /**
   * @return 手札の枚数
   */
  public int getStackedCardsLength() {
    return this.stackedCards.size();
  }

  /**
   * @return Alice(ID=0)がカードを置ける状況か
   */
  public boolean checkIfACanPutCard() {
    return Acanputcard;
  }

  /**
   * @return Bob(ID=1)がカードを置ける状況か
   */
  public boolean checkIfBCanPutCard() {
    return Bcanputcard;
  }

  /**
   * 配られたカードをatHandCardsとstackedCardsに割り振る
   * 
   * @param table    テーブル
   * @param cards    配られたカード
   * @param playerID 0か1
   */
  public Player(final Table table, final Card[] cards, final int playerID) {
    this.table = table;
    for (int i = 0; i < cards.length; i++) {
      if (i < AT_HAND_CARDS_SIZE) {
        this.atHandCards[i] = cards[i];
      } else {
        this.stackedCards.add(cards[i]);
      }
    }
    this.playerID = playerID;
  }

  @Override
  public abstract void run();

  public void stopThread() {
    this.running = false;
  }
}
