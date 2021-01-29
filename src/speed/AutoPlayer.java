package speed;


/**
 * コンピュータが自動で操作するプレイヤー
 * @author 渡辺
 */

public class AutoPlayer extends Player implements Runnable{
	private UserFrame userFrame;
	public AutoPlayer(final Table table, final Card[] cards, final int playerID, UserFrame userFrame) {
		super(table, cards, playerID);
		this.userFrame = userFrame;
	}


	/**
	 * TODO<br>
	 * スレッドの実行開始時に呼び出される
	 * @author 渡辺
	 */

	private boolean running = true;
	private CardView[] CpuView;
	private CardView[] TableView;

	public void run() {
		/* 未実装 */
		/*int handCard[] = new int[4];
		int centralCard[] = new int [2];

		System.out.println("handcard");
		for(int i=0; i<4; i++) {  //場札
			handCard[i] = getAtHandCard(i).getNumber();
			System.out.println(handCard[i]);
		}

		System.out.println("centralcard");
		for(int i=0; i<2; i++) {    //台札
			centralCard[i] = this.table.getCentralCard(i).getNumber();
			System.out.println(centralCard[i]);
		}*/

		CpuView = userFrame.getCpuView();
		TableView = userFrame.getTableView();

		for(int i=0 ; i<4 ; i++) {
			CpuView[i].setNumber(atHandCards[i].getNumber());
			CpuView[i].updated();
		}

		//AutoPlayerの単体テストの際にON
		/*
		for(int i=0 ; i<2 ; i++) {
			TableView[i].setNumber(table.getCentralCard(i).getNumber());
			TableView[i].updated();
		}
		*/

		try {
			this.sleep(5000);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		while(running) {
			outside:
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 2; j++) {
					final int atHandCardNumber = getAtHandCard(i).getNumber();
					final int tableCardNumber = table.getCentralCard(j).getNumber();

					if (tryPutCard(getAtHandCard(i), j)) { //カードが置けた
						Speed.log(String.format(
							"AutoPlayer put card %d -> %d, table(%d), atHand(%d)",
							tableCardNumber, atHandCardNumber, j, i
						));
						

						removeCard(i);

						// 描画を更新
						Speed.userFrame.update(table);

						// 5秒スリープ
						try {
							this.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						break outside;
					}
				}
			}

			// 動作軽減
			try {
				this.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopThread() {
		running = false;
	}
}
