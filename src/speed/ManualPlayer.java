package speed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

/**
 * ユーザが手動で操作するプレイヤー(GUI)<br>
 * プロDで作成したプログラムが参考になりそう<br>
 * TODO<br>
 * ボタン・ラジオボタンの配置、ボタンが押された際の処理など
 * @author 原田
 */

public class ManualPlayer extends Player {
	private UserFrame userFrame;

	public ManualPlayer(final Table table, final Card[] cards, final int playerID, UserFrame userFrame) {
		super(table, cards, playerID);
		this.userFrame = userFrame;
	}

	/**
	 * このフレームにボタンなどを登録する
	 */
	private CardView[] PlayerView;
	private CardView[] TableView;
	private ButtonAction[] buttonAction;

	/**
	 * TODO<br>
	 * スレッドの実行開始時に呼び出される
	 * @author 原田
	 */
	@Override
	public void run() {

		init(); //初期化
		while(!hasNoCard()) {} //カードがなくなるまでループ
		stopThread(); //スレッドを終了

	}

	public void init() {
		PlayerView = userFrame.getPlayerView();
		TableView = userFrame.getTableView();
		buttonAction = userFrame.getButtonAction();

		/* プレイヤーエリアを初期化 */
		for(int i=0 ; i<4 ; i++) {
			buttonAction[i].setPlayerInformation(this);
			buttonAction[i].setPlace(i);
			PlayerView[i].setNumber(atHandCards[i].getNumber());
			PlayerView[i].updated();
		}

		/* テーブルを初期化 */
		for(int i=0 ; i<2 ; i++) {
			TableView[i].setNumber(table.getCentralCard(i).getNumber());
			TableView[i].updated();
		}
	}
}

class ButtonAction implements ActionListener{
	private int place;
	ManualPlayer manual;
	CardView PlayerView;
	JRadioButton[] radio;
	CardView[] TableView;

	public ButtonAction(CardView PlayerView, JRadioButton[] radio, CardView[] TableView){
		this.PlayerView = PlayerView;
		this.radio = radio;
		this.TableView = TableView;
	}

	public void setPlayerInformation(ManualPlayer manual) {
		this.manual = manual;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (radio[0].isSelected()) { //左のラジオボタンが選択
			
			final int atHandCardNumber = manual.getAtHandCard(place).getNumber();
			final int tableCardNumber = manual.table.getCentralCard(0).getNumber();

			if (manual.tryPutCard(manual.getAtHandCard(place), 0)) {
				
				Speed.log(
					String.format(
						"ManualPlayer put card %d -> %d, table(%d), atHand(%d)",
						tableCardNumber, atHandCardNumber, 0, place
					)
				);

				manual.removeCard(place);

				// 描画を更新
				Speed.userFrame.update(manual.table);
			}
		} else { //右のラジオボタンが選択

			final int atHandCardNumber = manual.getAtHandCard(place).getNumber();
			final int tableCardNumber = manual.table.getCentralCard(0).getNumber();

			if (manual.tryPutCard(manual.getAtHandCard(place), 1)) {

				Speed.log(String.format(
					"ManualPlayer put card %d -> %d, table(%d), atHand(%d)",
					tableCardNumber, atHandCardNumber, 1, place
				));

				manual.removeCard(place);

				// 描画を更新
				Speed.userFrame.update(manual.table);
			}
		}
	}
}