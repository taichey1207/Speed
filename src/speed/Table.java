package speed;

/**
 * テーブルを表すクラス<br>
 * 共有メモリに相当
 */

public class Table {
	/** 台札 */
	private Card[] centralCards;

	/** プレイヤー */
	private Player[] players = new Player[2];

	/**
	 * @param id 0か1
	 * @return player
	 */
	public Player getPlayer(final int id) {
		return players[id];
	}

	/**
	 * @param player 登録するプレイヤー
	 */
	public void setPlayer(final Player player) {
		players[player.getPlayerID()] = player;
	}

	/**
	 * @param place カードの位置(0か1)
	 * @return カード
	 */
	public Card getCentralCard(final int place) {
		return new Card(centralCards[place]);
	}

	/**
	 * カードを置く<br>
	 * 置ける条件を満たす(カードの数字が1だけ違う)かどうかは無視
	 * @param card 置くカード
	 * @param place カードの位置(0か1)
	 */
	public void putCard(final Card card, final int place) {
		centralCards[place] = new Card(card);
	}

	/**
	 * @param cards 長さは2
	 */
	public Table(final Card[] cards) {
		centralCards = cards;
	}
}
