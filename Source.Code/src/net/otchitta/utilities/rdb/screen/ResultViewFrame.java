/**
 * [Source.Code] ResultViewFrame.java
 */
package net.otchitta.utilities.rdb.screen;

import java.awt.CardLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import net.otchitta.utilities.rdb.source.ActionDataModel;
import net.otchitta.utilities.rdb.source.ColumnListModel;
import net.otchitta.utilities.rdb.source.RecordDataModel;
import net.otchitta.utilities.rdb.source.ResultDataModel;

/**
 * 結果画面エリアクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class ResultViewFrame {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** 正常種別 */
	private static final String SUCCESS = "SUCCESS";
	/** 異常種別 */
	private static final String FAILURE = "FAILURE";

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 並列管理 */
	private static final ExecutorService threads = Executors.newFixedThreadPool(1);
	/** 正常情報 */
	private final JTabbedPane success;
	/** 異常情報 */
	private final JTextArea failure;
	/** 選択処理 */
	private final CardLayout chooser;
	/** 画面情報 */
	private final JPanel display;
	/** 監視一覧 */
	private final EventListenerList listens;
	/** 通知情報 */
	private transient ChangeEvent element;

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 結果画面エリアを生成します。
	 */
	public ResultViewFrame() {
		super();
		// 内部変数生成
		this.success = new JTabbedPane();
		this.failure = new JTextArea();
		this.chooser = new CardLayout();
		this.display = new JPanel(this.chooser);
		this.listens = new EventListenerList();
		this.element = null;
		// 内部変数設定
		// 表示体裁設定
		var cache1 = this.success;
		var cache2 = new JScrollPane(this.failure);
		this.display.add(cache1, SUCCESS);
		this.display.add(cache2, FAILURE);
		this.chooser.show(this.display, SUCCESS);
	}

	// ===================================================================
	// 内部メソッド定義
	// ===================================================================
	/**
	 * 要素画面を登録します。
	 * 
	 * @param registCode 登録番号
	 * @return 要素情報
	 * @see ResultDataModel#invokeList(String, String, java.util.function.IntFunction)
	 */
	private ActionDataModel registView(int registCode) {
		var result = new Controller();
		SwingUtilities.invokeLater(() -> {
			this.chooser.show(this.display, SUCCESS);
			this.success.add(String.format("Result:%03d", Integer.valueOf(registCode + 1)), result.getView());
		});
		return result;
	}
	/**
	 * 要素画面を更新します。
	 * 
	 * @param updateData 例外情報
	 */
	private void updateData(Exception updateData) {
		this.failure.setText(updateData.toString());
		this.chooser.show(this.display, FAILURE);
	}
	/**
	 * 処理終了を通知します。
	 */
	private void notifyHook() {
		if (this.element == null) this.element = new ChangeEvent(this);
		for (var choose : this.listens.getListeners(ChangeListener.class)) {
			choose.stateChanged(this.element);
		}
	}

	// ===================================================================
	// 公開メソッド定義
	// ===================================================================
	/**
	 * 画面情報を取得します。
	 * 
	 * @return 画面情報
	 */
	public JComponent chooseView() {
		return this.display;
	}
	/**
	 * 引数情報を実行します。
	 * 
	 * @param connection 接続情報
	 * @param invokeText 実行構文
	 */
	public void invokeText(String connection, String invokeText) {
		this.success.removeAll();
		this.failure.setText("");
		threads.execute(() -> {
			try {
				ResultDataModel.invokeList(connection, invokeText, this::registView);
			} catch (Exception errors) {
				updateData(errors);
			}
			SwingUtilities.invokeLater(this::notifyHook);
		});
	}
	/**
	 * 終了監視を追加します。
	 * 
	 * @param listenData 終了監視
	 */
	public void registHook(ChangeListener listenData) {
		this.listens.add(ChangeListener.class, listenData);
	}
	/**
	 * 終了監視を削除します。
	 * 
	 * @param listenData 終了監視
	 */
	public void removeHook(ChangeListener listenData) {
		this.listens.remove(ChangeListener.class, listenData);
	}

	/**
	 * 制御処理クラスです。
	 * 
	 * @since   1.0.0
	 * @version 1.0.0
	 * @author  o.chikami
	 */
	private static final class Controller implements ActionDataModel {
		// ===================================================================
		// メンバー定数定義
		// ===================================================================
		/** 成功画面 */
		private static final String SUCCESS = "SUCCESS";
		/** 成功画面 */
		private static final String MESSAGE = "MESSAGE";
		/** 失敗画面 */
		private static final String FAILURE = "FAILURE";

		// ===================================================================
		// メンバー変数定義
		// ===================================================================
		/** 要素情報 */
		private final RecordViewModel records;
		/** 要素情報 */
		private final ColumnViewModel columns;
		/** 一覧画面 */
		private final JTable success;
		/** 文言画面 */
		private final JTextArea message;
		/** 例外画面 */
		private final JTextArea failure;
		/** 体裁情報 */
		private final CardLayout chooser;
		/** 結果画面 */
		private final JPanel display;

		// ===================================================================
		// 生成メソッド定義
		// ===================================================================
		/**
		 * 制御処理を生成します。
		 */
		public Controller() {
			super();
			// 内部変数生成
			this.records = new RecordViewModel();
			this.columns = new ColumnViewModel();
			this.success = new JTable(this.records, this.columns);
			this.message = new JTextArea();
			this.failure = new JTextArea();
			this.chooser = new CardLayout();
			this.display = new JPanel(this.chooser);
			// 内部変数設定
			this.success.setDefaultRenderer(Object.class, new SourceViewFrame());
			this.success.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.failure.setEditable(false);
			// 画面体裁設定
			var cache1 = new JScrollPane(this.success);
			var cache2 = new JScrollPane(this.message);
			var cache3 = new JScrollPane(this.failure);
			this.display.add(cache1, SUCCESS);
			this.display.add(cache2, MESSAGE);
			this.display.add(cache3, FAILURE);
		}

		// ===================================================================
		// 公開メソッド定義
		// ===================================================================
		/**
		 * 画面情報を取得します。
		 * 
		 * @return 画面情報
		 */
		public JComponent getView() {
			return this.display;
		}

		// ===================================================================
		// 継承メソッド定義
		// ===================================================================
		/**
		 * 要素一覧を更新します。
		 * 
		 * @param columnList 要素一覧
		 */
		@Override
		public void updateColumnList(ColumnListModel columnList) {
			SwingUtilities.invokeLater(() -> {
				this.columns.setColumnList(columnList);
				this.chooser.show(this.display, SUCCESS);
			});
		}
		/**
		 * 処理件数を実行します。
		 * 
		 * @param updateSize 処理件数
		 */
		@Override
		public void invokeUpdateSize(int updateSize) {
			SwingUtilities.invokeLater(() -> {
				this.message.setText(String.format("処理件数:%s", Integer.valueOf(updateSize)));
				this.chooser.show(this.display, MESSAGE);
			});
		}
		/**
		 * 要素情報を登録します。
		 * 
		 * @param recordData 要素情報
		 */
		@Override
		public void registRecordData(RecordDataModel recordData) {
			SwingUtilities.invokeLater(() -> this.records.registRecordData(recordData));
		}
		/**
		 * 要素一覧を更新します。
		 * 
		 * @param resultData 例外情報
		 */
		@Override
		public void updateRecordList(Exception resultData) {
			SwingUtilities.invokeLater(() -> {
				this.failure.setText(resultData.toString());
				this.chooser.show(this.display, FAILURE);
			});
		}
	}
}
