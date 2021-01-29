package speed;

import java.util.ArrayList;
import java.util.Collections;

/**
 * mainメソッドを持つ
 * @author 平田
 */

public class Speed extends Thread {
	/**
	 * <ol>
	 * <li>Tableオブジェクトを作成する。
	 * コンストラクタの引数には台札のカード(2枚)を渡す。</li>
	 * <li>ManualPlayerオブジェクトとAutoPlayerオブジェクトを1つずつ作成する。
	 * idはそれぞれ0, 1とする。
	 * コンストラクタの引数には始めに作成したTableオブジェクトと
	 * 各プレイヤーに配るカード(25枚ずつ)を渡す。</li>
	 * <li>tableにplayersを登録する。</li>
	 * <li>Speedオブジェクトを作成し実行する。
	 * コンストラクタの引数には先に作成したtableを渡す。</li>
	 * <li>Speedが終了すればプログラム終了。</li>
	 * </ol>
	 * @param args 未使用
	 * @author 平田
	 */

	/**
	 * フレーム(追加:原田)
	 */


	public static final UserFrame userFrame = new UserFrame("Speed");

	public static void main(final String[] args) {
		Speed.log("begin");

		ArrayList<Card> deck = new ArrayList<Card>();

		for (int i=1; i <= 13; i++) {
			for (Card.Suit suit: Card.Suit.values()) {
				deck.add(new Card(suit, i));
			}
		}
		Collections.shuffle(deck);

		Table table = new Table(new Card[] {deck.get(0), deck.get(1)});

		Player players[] = new Player[] {new ManualPlayer(table, deck.subList(2, 2 + 25).toArray(new Card[25]), 0, userFrame),
				new AutoPlayer(table, deck.subList(2 + 25, 2 + 25 + 25).toArray(new Card[25]), 1, userFrame)};

		table.setPlayer(players[0]);
		table.setPlayer(players[1]);

		Speed speed = new Speed(table);
		speed.run();

		Speed.log("end");
	}

	/**
	 * テーブル
	 */
	private Table table;

	/**
	 * プレイヤー
	 */
	private Player[] players;

	/**
	 * Speedクラスのスレッドが動作中かを表すフラグ
	 */
	private boolean isActive = true;

	public Speed(final Table table) {
		this.table = table;
		this.players = new Player[] {table.getPlayer(0), table.getPlayer(1)};
	}

	/**
	 * <ul>
	 * <li>対戦結果を表示する.</li>
	 * </ul>
	 * @author 平田
	 */
	public void printResult() {
		if (this.players[0].hasNoCard()) {
			System.out.println("player 0 win. \n");
		} else if (this.players[1].hasNoCard()) {
			System.out.println("player 1 win. \n");
		}
	}

	/**
	 * <ul>
	 * <li>自身を含めた3つのスレッド(ManualPlayer, AutoPlayer, Speed)を終了させる。</li>
	 * </ul>
	 * @author 平田
	 */
	public void stopAllThread() {
		this.isActive = false;
		this.players[0].stopThread();
		this.players[1].stopThread();
	}

	/**
	 * <ul>
	 * <li>どちらかのプレイヤーのカードが無くなっているか調べる. </li>
	 * </ul>
	 * @return どちらかのプレイヤーのカードが無くなっていればtrue, そうでなければfalse.
	 * @author 平田
	 */
	public boolean isEnd() {
		return this.players[0].hasNoCard() || this.players[1].hasNoCard();
	}

	/**
	 * <ul>
	 * <li>2人のプレイヤーが共にどのカードも出せない状態にあるか調べる. </li>
	 * </ul>
	 * @return 2人のプレイヤーが共にどのカードも出せない状態ならばtrue, そうでなければfalse.
	 * @author 平田
	 */
	public boolean isFrozen() {
		return cannotPutCard(players[0]) && cannotPutCard(players[1]);
	}

	/**
	 * <ul>
	 * <li>引数で受け取ったプレイヤーがどのカードも出せない状態にあるか調べる. </li>
	 * </ul>
	 * @param p 対象のプレイヤー
	 * @return プレイヤーがどのカードも出せない状態ならばtrue, そうでなければfalse.
	 * @author 平田
	 */
	public boolean cannotPutCard(Player p) {
		Card centralCards[] = new Card[] {this.table.getCentralCard(0), this.table.getCentralCard(1)};

		for (int i=0; i < Player.AT_HAND_CARDS_SIZE; i++) {
			Card c = p.getAtHandCard(i);
			if (canStack(centralCards[0], c) || canStack(centralCards[1], c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <ul>
	 * <li> 引数で台札と場札を受け取り, 台札の上に場札を置けるか調べる. </li>
	 * </ul>
	 * @param center 台札
	 * @param hand 場札
	 * @return カードを置けるならtrue, そうでなければfalse.
	 * @author 平田
	 */
	public boolean canStack(Card center, Card hand) {
		int handnum = hand.getNumber();
		int centernum = center.getNumber();
		int gap = Math.abs(handnum - centernum);

		if ((gap == 1) || (gap == 12)) {
			return true;
		} else return false;
	}

	/**
	 * <ul>
	 * <li> 2人のプレイヤーの場札からランダムにカードを選んで台札に置く. </li>
	 * <li> 同期処理が必要な箇所がある. </li>
	 * </ul>
	 * @author 平田
	 */
	public void unfreeze() {
		Speed.log("begin");

		// 少しだけ待つ
		try {
			this.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int places[] = new int[] {selectRandomCard(this.players[0]), selectRandomCard(this.players[1])};

		if (places[0] == -1 || places[1] == -1) {
			//エラー処理
			//おそらくいらない
		}

		// ログに出力
		final int tableCardNumber0 = table.getCentralCard(0).getNumber();
		final int tableCardNumber1 = table.getCentralCard(1).getNumber();
		final int playerCardNumber0 = this.players[0].getAtHandCard(places[0]).getNumber();
		final int playerCardNumber1 = this.players[1].getAtHandCard(places[1]).getNumber();
		Speed.log(String.format(
			"unfreeze ManualPlayer %d -> %d, table(%d), atHand(%d)",
			tableCardNumber0, playerCardNumber0, 0, places[0]
		));
		Speed.log(String.format(
			"unfreeze AutoPlayer %d -> %d, table(%d), atHand(%d)",
			tableCardNumber1, playerCardNumber1, 1, places[1]
		));

		this.table.putCard(this.players[0].getAtHandCard(places[0]), 0);
		this.table.putCard(this.players[1].getAtHandCard(places[1]), 1);
		this.players[0].removeCard(places[0]);
		this.players[0].removeCard(places[1]);

		// 画面更新
		userFrame.update(table);

		/* TODO
		 * 上4つの操作中にplayerは動作してはいけない
		 * バリア同期で解決できるかも
		 */

		Speed.log("end");
	}

	/**
	 * <ul>
	 * <li> 引数で受け取ったプレイヤーの場札からランダムにカードを選んで, その位置を返す. </li>
	 * </ul>
	 * @param p 対象のプレイヤー.
	 * @return ランダムに選ばれた場札の位置.
	 * @author 平田
	 */
	public int selectRandomCard(Player p) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i < Player.AT_HAND_CARDS_SIZE; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		for (int place : list) {
			Card c = p.getAtHandCard(place);
			if (c.getNumber() != Player.EMPTYCARD_NUMBER) {
				return place;
			}
		}
		return -1;
	}

	/**
	 * <ul>
	 * <li>最初に2つのplayerを実行する。</li>
	 * <li>2人のプレイヤーが共にどのカードも出せない状態に陥ったら、
	 * 2人のプレイヤーの場札からランダムにカードを選んで台札に置く。</li>
	 * <li>どちらかのプレイヤーのカードが無くなれば対戦結果を表示して
	 * 自身を含めた3つのスレッド(ManualPlayer, AutoPlayer, Speed)を終了させる。</li>
	 * </ul>
	 * @author 平田
	 */
	@Override
	public void run() {

		this.players[0].start();
		this.players[1].start();

		while (this.isActive) {
			if (isEnd()) { //対戦終了
				printResult();
				stopAllThread();
			} else if (isFrozen()) {
				unfreeze();
			}

			// 動作軽減
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ログ出力
	public static void log(final Object object) {
		final StackTraceElement elem = new Throwable().getStackTrace()[1];
		System.out.println(
			object +
			" in " +
			elem.getClassName() +
			"." +
			elem.getMethodName() +
			" at line " +
			elem.getLineNumber()
		);
	}
}
