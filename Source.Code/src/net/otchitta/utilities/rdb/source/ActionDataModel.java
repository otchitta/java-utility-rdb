package net.otchitta.utilities.rdb.source;

/**
 * 要素処理モデルインターフェースです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public interface ActionDataModel {
	/**
	 * 要素一覧を更新します。
	 * 
	 * @param columnList 要素一覧
	 */
	public void updateColumnList(ColumnListModel columnList);
	/**
	 * 処理件数を実行します。
	 * 
	 * @param updateSize 処理件数
	 */
	public void invokeUpdateSize(int updateSize);
	/**
	 * 要素情報を登録します。
	 * 
	 * @param recordData 要素情報
	 */
	public void registRecordData(RecordDataModel recordData);
	/**
	 * 要素一覧を更新します。
	 * 
	 * @param resultData 例外情報
	 */
	public void updateRecordList(Exception resultData);
}
