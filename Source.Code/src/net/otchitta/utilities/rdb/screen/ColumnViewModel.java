/**
 * [Source.Code] ColumnViewModel.java
 */
package net.otchitta.utilities.rdb.screen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.otchitta.utilities.rdb.source.ColumnListModel;

/**
 * 要素画面モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
final class ColumnViewModel implements TableColumnModel, ListSelectionListener, PropertyChangeListener, Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 既定描画 */
	private static final TableCellRenderer DEFAULT_RENDERER = new SourceViewFrame();
	/** 要素一覧 */
	private final ArrayList<TableColumn> columnList;
	/** 余白情報 */
	private int marginData;
	/** 合計横幅 */
	private int totalWidth;
	/** 選択可否 */
	private boolean selectFlag;
	/** 選択情報 */
	private ListSelectionModel selectData;
	/** 監視一覧 */
	private final EventListenerList listenList;
	/** 通知情報 */
	private transient ChangeEvent changeData;

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 要素画面モデルを生成します。
	 */
	public ColumnViewModel() {
		super();
		// 初期設定
		this.columnList = new ArrayList<>();
		this.marginData = 1;
		this.totalWidth = -1;
		this.selectFlag = false;
		this.selectData = null;
		this.listenList = new EventListenerList();
		this.changeData = null;
		// 共通設定
		setSelectionModel(new DefaultListSelectionModel());
	}

	// ===================================================================
	// 内部メソッド定義
	// ===================================================================
	/**
	 * 要素登録を通知します。
	 * 
	 * @param index1 開始番号
	 * @param index2 終了番号
	 */
	private void notifyInsertData(int index1, int index2) {
		var source = new TableColumnModelEvent(this, index1, index2);
		for (var choose : this.listenList.getListeners(TableColumnModelListener.class)) {
			choose.columnAdded(source);
		}
	}
	/**
	 * 要素削除を通知します。
	 * 
	 * @param index1 開始番号
	 * @param index2 終了番号
	 */
	private void notifyRemoveData(int index1, int index2) {
		var source = new TableColumnModelEvent(this, index1, index2);
		for (var choose : this.listenList.getListeners(TableColumnModelListener.class)) {
			choose.columnRemoved(source);
		}
	}
	/**
	 * 要素移動を通知します。
	 * 
	 * @param oldIndex 旧番号
	 * @param newIndex 新番号
	 */
	private void notifyOffsetData(int oldIndex, int newIndex) {
		var values = new TableColumnModelEvent(this, oldIndex, newIndex);
		for (var choose : this.listenList.getListeners(TableColumnModelListener.class)) {
			choose.columnMoved(values);
		}
	}
	/**
	 * 余白変更を通知します。
	 */
	private void notifyMarginData() {
		if (this.changeData == null) this.changeData = new ChangeEvent(this);
		for (var choose : this.listenList.getListeners(TableColumnModelListener.class)) {
			choose.columnMarginChanged(this.changeData);
		}
	}
	/**
	 * 選択変更を通知します。
	 * 
	 * @param values 通知情報
	 */
	private void notifySelectData(ListSelectionEvent values) {
		for (var choose : this.listenList.getListeners(TableColumnModelListener.class)) {
			choose.columnSelectionChanged(values);
		}
	}
	/**
	 * 要素横幅を算出します。
	 * 
	 * @param current 現在値
	 * @return 要素横幅
	 */
	private static int chooseColumnSize(int current) {
		return current * 6 + 3;
	}
	/**
	 * 要素横幅を算出します。
	 * 
	 * @param current 現在値
	 * @param minimum 最小値
	 * @param maximum 最大値
	 * @return 要素横幅
	 */
	private static int chooseColumnSize(int current, int minimum, int maximum) {
		var choose = Math.max(minimum, Math.min(current, maximum));
		return chooseColumnSize(choose);
	}

	// ===================================================================
	// 公開メソッド定義
	// ===================================================================
	/**
	 * 要素一覧を設定します。
	 * 
	 * @param columnList 要素一覧
	 */
	public void setColumnList(ColumnListModel columnList) {
		var before = this.columnList.size() - 1;
		this.columnList.clear();
		notifyRemoveData(before, 0);
		if (columnList != null) {
			for (var index = 0; index < columnList.getSize(); index ++) {
				var choose = columnList.getData(index);
				var column = new TableColumn(index);
				switch (String.valueOf(choose.getCode()).toUpperCase(Locale.ENGLISH)) {
				default:
					column.setPreferredWidth(chooseColumnSize(choose.getSize(), 1, 10));
					column.setMaxWidth(chooseColumnSize(choose.getSize(), 1, 5_000));
					column.setMinWidth(chooseColumnSize(1));
					break;
				case "TINYINT":
					column.setPreferredWidth(chooseColumnSize(4));
					column.setMaxWidth(chooseColumnSize(4));
					column.setMinWidth(chooseColumnSize(1));
					break;
				case "SMALLINT":
					column.setPreferredWidth(chooseColumnSize(4));
					column.setMaxWidth(chooseColumnSize(6));
					column.setMinWidth(chooseColumnSize(1));
					break;
				case "INT":
					column.setPreferredWidth(chooseColumnSize(6));
					column.setMaxWidth(chooseColumnSize(14));
					column.setMinWidth(chooseColumnSize(01));
					break;
				case "BIGINT":
					column.setPreferredWidth(chooseColumnSize(10));
					column.setMaxWidth(chooseColumnSize(26));
					column.setMinWidth(chooseColumnSize(01));
					break;
				case "DATETIME2":
					column.setPreferredWidth(chooseColumnSize(22));
					column.setMaxWidth(chooseColumnSize(27));
					column.setMinWidth(chooseColumnSize(01));
					break;
				}
				column.setHeaderValue(choose.getName());
				column.setCellRenderer(DEFAULT_RENDERER);
				column.addPropertyChangeListener(this);
				this.columnList.add(column);
			}
			notifyInsertData(0, this.columnList.size() - 1);
		}
	}

	// ===================================================================
	// 実装メソッド定義
	// ===================================================================
	/**
	 * 要素個数を取得します。
	 * 
	 * @return 要素個数
	 * @see TableColumnModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.columnList.size();
	}
	/**
	 * 要素情報を取得します。
	 * 
	 * @param columnCode 要素番号
	 * @return 要素情報
	 * @see TableColumnModel#getColumn(int)
	 */
	@Override
	public TableColumn getColumn(int columnCode) {
		return this.columnList.get(columnCode);
	}
	/**
	 * 要素番号を取得します。
	 * 
	 * @param uniqueData 一意情報
	 * @return 要素番号
	 * @see TableColumnModel#getColumnIndex(Object)
	 */
	@Override
	public int getColumnIndex(Object uniqueData) {
		if (uniqueData == null) {
			throw new IllegalArgumentException("uniqueData must be non null.");
		} else {
			for (var index = 0; index < this.columnList.size(); index ++) {
				var choose = this.columnList.get(index);
				if (uniqueData.equals(choose.getIdentifier())) {
					return index;
				}
			}
			throw new IllegalArgumentException("uniqueData is not found.");
		}
	}
	/**
	 * 要素一覧を取得します。
	 * 
	 * @return 要素一覧
	 * @see TableColumnModel#getColumns()
	 */
	@Override
	public Enumeration<TableColumn> getColumns() {
		return Collections.enumeration(this.columnList);
	}
	/**
	 * 要素情報を追加します。
	 * 
	 * @param columnData 要素情報
	 * @see TableColumnModel#addColumn(TableColumn)
	 */
	@Override
	public void addColumn(TableColumn columnData) {
		if (columnData == null) {
			throw new IllegalArgumentException("columnData must be non null.");
		} else {
			columnData.addPropertyChangeListener(this);
			this.columnList.add(columnData);
			this.totalWidth = -1;
			notifyInsertData(0, getColumnCount() - 1);
		}
	}
	/**
	 * 要素情報を削除します。
	 * 
	 * @param columnData 要素情報
	 * @see TableColumnModel#removeColumn(TableColumn)
	 */
	@Override
	public void removeColumn(TableColumn columnData) {
		var index2 = this.columnList.indexOf(columnData);
		if (index2 < 0) {
			// 処理なし
		} else {
			if (this.selectData != null) this.selectData.removeIndexInterval(index2, index2);
			columnData.removePropertyChangeListener(this);
			this.columnList.remove(index2);
			this.totalWidth = -1;
			notifyRemoveData(index2, 0);
		}
	}
	/**
	 * 要素情報を移動します。
	 * 
	 * @param oldIndex 旧番号
	 * @param newIndex 新番号
	 * @see TableColumnModel#moveColumn(int, int)
	 */
	@Override
	public void moveColumn(int oldIndex, int newIndex) {
		if (oldIndex < 0 || getColumnCount() <= oldIndex) {
			throw new IllegalArgumentException("moveColumn() - Index out of range");
		} else if (oldIndex == newIndex) {
			notifyOffsetData(oldIndex, newIndex);
		} else if (this.selectData == null) {
			var choose = this.columnList.remove(oldIndex);
			this.columnList.add(newIndex, choose);
			notifyOffsetData(oldIndex, newIndex);
		} else {
			var choose = this.columnList.remove(oldIndex);
			var select = this.selectData.isSelectedIndex(oldIndex);
			this.selectData.removeIndexInterval(oldIndex, oldIndex);
			this.columnList.add(newIndex, choose);
			this.selectData.insertIndexInterval(newIndex, 1, true);
			if (select) {
				this.selectData.addSelectionInterval(newIndex, newIndex);
			} else {
				this.selectData.removeSelectionInterval(newIndex, newIndex);
			}
			notifyOffsetData(oldIndex, newIndex);
		}
	}
	/**
	 * 合計横幅を取得します。
	 * 
	 * @return 合計横幅
	 * @see TableColumnModel#getTotalColumnWidth()
	 */
	@Override
	public int getTotalColumnWidth() {
		if (this.totalWidth == -1) {
			var result = 0;
			for (var choose : this.columnList) {
				result += choose.getWidth();
			}
			this.totalWidth = result;
		}
		return this.totalWidth;
	}
	/**
	 * 要素番号を取得します。
	 * 
	 * @param position 横軸座標
	 * @return 要素番号
	 * @see TableColumnModel#getColumnIndexAtX(int)
	 */
	@Override
	public int getColumnIndexAtX(int position) {
		if (position < 0) {
			return -1;
		} else {
			var remain = position;
			for (var index = 0; index < this.columnList.size(); index ++) {
				var choose = this.columnList.get(index);
				remain -= choose.getWidth();
				if (remain < 0) return index;
			}
			return -1;
		}
	}
	/**
	 * 余白情報を取得します。
	 * 
	 * @return 余白情報
	 * @see TableColumnModel#getColumnMargin()
	 */
	@Override
	public int getColumnMargin() {
		return this.marginData;
	}
	/**
	 * 余白情報を設定します。
	 * 
	 * @param marginData 余白情報
	 * @see TableColumnModel#setColumnMargin(int)
	 */
	@Override
	public void setColumnMargin(int marginData) {
		if (this.marginData != marginData) {
			this.marginData = marginData;
			notifyMarginData();
		}
	}
	/**
	 * 選択可否を取得します。
	 * 
	 * @return 選択可否
	 * @see TableColumnModel#getColumnSelectionAllowed()
	 */
	@Override
	public boolean getColumnSelectionAllowed() {
		return this.selectFlag;
	}
	/**
	 * 選択可否を設定します。
	 * 
	 * @param selectFlag 選択可否
	 * @see TableColumnModel#setColumnSelectionAllowed(boolean)
	 */
	@Override
	public void setColumnSelectionAllowed(boolean selectFlag) {
		this.selectFlag = selectFlag;
	}
	/**
	 * 選択処理を取得します。
	 * 
	 * @return 選択処理
	 * @see TableColumnModel#getSelectionModel()
	 */
	@Override
	public ListSelectionModel getSelectionModel() {
		return this.selectData;
	}
	/**
	 * 選択処理を設定します。
	 * 
	 * @param selectData 選択処理
	 * @see TableColumnModel#setSelectionModel(ListSelectionModel)
	 */
	@Override
	public void setSelectionModel(ListSelectionModel selectData) {
		if (selectData == null) {
			throw new IllegalArgumentException("selectData must be non null.");
		} else if (this.selectData == selectData) {
			// 処理なし
		} else if (this.selectData == null) {
			this.selectData = selectData;
			this.selectData.addListSelectionListener(this);
		} else {
			this.selectData.removeListSelectionListener(this);
			this.selectData = selectData;
			this.selectData.addListSelectionListener(this);
		}
		this.selectData = selectData;
	}
	/**
	 * 選択個数を取得します。
	 * 
	 * @return 選択個数
	 * @see TableColumnModel#getSelectedColumnCount()
	 */
	@Override
	public int getSelectedColumnCount() {
		return this.selectData == null? 0: this.selectData.getSelectedItemsCount();
	}
	/**
	 * 選択一覧を取得します。
	 * 
	 * @return 選択一覧
	 * @see TableColumnModel#getSelectedColumns()
	 */
	@Override
	public int[] getSelectedColumns() {
		if (this.selectData == null) {
			return new int[0];
		} else {
			return this.selectData.getSelectedIndices();
		}
	}
	/**
	 * 監視処理を追加します。
	 * 
	 * @param listener 監視処理
	 * @see TableColumnModel#addColumnModelListener(TableColumnModelListener)
	 */
	@Override
	public void addColumnModelListener(TableColumnModelListener listener) {
		this.listenList.add(TableColumnModelListener.class, listener);
	}
	/**
	 * 監視処理を削除します。
	 * 
	 * @param listener 監視処理
	 * @see TableColumnModel#removeColumnModelListener(TableColumnModelListener)
	 */
	@Override
	public void removeColumnModelListener(TableColumnModelListener listener) {
		this.listenList.remove(TableColumnModelListener.class, listener);
	}
	/**
	 * 選択変更を処理します。
	 * 
	 * @param values 通知情報
	 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent values) {
		notifySelectData(values);
	}
	/**
	 * 要素変更を処理します。
	 * 
	 * @param values 通知情報
	 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent values) {
		switch (values.getPropertyName()) {
		case "width":
		case "preferredWidth":
			this.totalWidth = -1;
			notifyMarginData();
			break;
		}
	}
}
