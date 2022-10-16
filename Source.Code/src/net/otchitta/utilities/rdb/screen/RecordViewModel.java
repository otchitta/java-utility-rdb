/**
 * [Source.Code] RecordViewModel.java
 */
package net.otchitta.utilities.rdb.screen;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.otchitta.utilities.rdb.source.RecordDataModel;

/**
 * 要素画面モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
final class RecordViewModel implements TableModel {
	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 監視一覧 */
	private final EventListenerList listenList;
	/** 要素一覧 */
	private final ArrayList<RecordDataModel> recordList;
	/** 要素個数 */
	private int columnSize;

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 要素画面モデルを生成します。
	 */
	public RecordViewModel() {
		this.listenList = new EventListenerList();
		this.recordList = new ArrayList<>();
		this.columnSize = 0;
	}

	// ===================================================================
	// 公開メソッド定義
	// ===================================================================
	/**
	 * 要素一覧を削除します。
	 */
	public void removeRecordList() {
		this.columnSize = 0;
		this.recordList.clear();
		var values = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
		for (var choose : this.listenList.getListeners(TableModelListener.class)) {
			choose.tableChanged(values);
		}
	}
	/**
	 * 要素情報を追加します。
	 * 
	 * @param recordData 要素情報
	 */
	public void registRecordData(RecordDataModel recordData) {
		var offset = this.recordList.size();
		var before = this.columnSize;
		this.columnSize = Math.max(this.columnSize, recordData.getSize());
		this.recordList.add(recordData);
		var values = this.columnSize == before?
		             new TableModelEvent(this, offset, offset, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT): // 情報追加
		             new TableModelEvent(this, TableModelEvent.HEADER_ROW); // 構造変更
		for (var choose : this.listenList.getListeners(TableModelListener.class)) {
			choose.tableChanged(values);
		}
	}

	// ===================================================================
	// 実装メソッド定義
	// ===================================================================
	/**
	 * 列個数を取得します。
	 * 
	 * @return 列個数
	 * @see TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.columnSize;
	}
	/**
	 * 列名称を取得します。
	 * 
	 * @param columnCode 列番号
	 * @return 列名称
	 * @see TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnCode) {
		return String.format("%04d", Integer.valueOf(columnCode));
	}
	/**
	 * 列種別を取得します。
	 * 
	 * @param columnCode 列番号
	 * @return 列種別
	 * @see TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnCode) {
		return Object.class;
	}
	/**
	 * 行個数を取得します。
	 * 
	 * @return 行個数
	 * @see TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.recordList.size();
	}
	/**
	 * 編集可否を判定します。
	 * 
	 * @param recordCode 行番号
	 * @param columnCode 列番号
	 * @return 編集可否
	 * @see TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int recordCode, int columnCode) {
		return false;
	}
	/**
	 * 値情報を取得します。
	 * 
	 * @param recordCode 行番号
	 * @param columnCode 列番号
	 * @return 値情報
	 * @see TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int recordCode, int columnCode) {
		var choose = this.recordList.get(recordCode);
		return choose.getData(columnCode);
	}
	/**
	 * 値情報を設定します。
	 * 
	 * @param importData 値情報
	 * @param recordCode 行番号
	 * @param columnCode 列番号
	 * @see TableModel#setValueAt(Object, int, int)
	 */
	@Override
	public void setValueAt(Object importData, int recordCode, int columnCode) {
		// 処理なし
	}
	/**
	 * 監視処理を追加します。
	 * 
	 * @param listenHook 監視処理
	 * @see TableModel#addTableModelListener(TableModelListener)
	 */
	@Override
	public void addTableModelListener(TableModelListener listenHook) {
		this.listenList.add(TableModelListener.class, listenHook);
	}
	/**
	 * 監視処理を追加します。
	 * 
	 * @param listenHook 監視処理
	 * @see TableModel#removeTableModelListener(TableModelListener)
	 */
	@Override
	public void removeTableModelListener(TableModelListener listenHook) {
		this.listenList.remove(TableModelListener.class, listenHook);
	}
}
