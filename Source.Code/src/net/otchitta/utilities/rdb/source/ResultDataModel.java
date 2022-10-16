/**
 * [Source.Code] ResultDataModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.IntFunction;

/**
 * 結果情報モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class ResultDataModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素一覧 */
	private final ColumnListModel columnList;
	/** 要素一覧 */
	private final RecordListModel recordList;

	// ===================================================================
	// プロパティー定義
	// ===================================================================
	/**
	 * 要素一覧を取得します。
	 * 
	 * @return 要素一覧
	 */
	public ColumnListModel getColumnList() {
		return this.columnList;
	}
	/**
	 * 要素個数を取得します。
	 * 
	 * @return 要素個数
	 * @see ColumnListModel#getSize()
	 */
	public int getColumnSize() {
		return this.columnList.getSize();
	}
	/**
	 * 要素情報を取得します。
	 * 
	 * @param index 要素番号
	 * @return 要素情報
	 * @see ColumnListModel#getData(int)
	 */
	public ColumnDataModel getColumnData(int index) {
		return this.columnList.getData(index);
	}
	/**
	 * 要素一覧を取得します。
	 * 
	 * @return 要素一覧
	 */
	public RecordListModel getRecordList() {
		return this.recordList;
	}
	/**
	 * 要素個数を取得します。
	 * 
	 * @return 要素個数
	 * @see RecordListModel#getSize()
	 */
	public int getRecordSize() {
		return this.recordList.getSize();
	}
	/**
	 * 要素情報を取得します。
	 * 
	 * @param index 要素番号
	 * @return 要素情報
	 * @see RecordDataModel#getData(int)
	 */
	public RecordDataModel getRecordData(int index) {
		return this.recordList.getData(index);
	}

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param columnList 要素一覧
	 * @param recordList 要素一覧
	 */
	private ResultDataModel(ColumnListModel columnList, RecordListModel recordList) {
		super();
		this.columnList = columnList;
		this.recordList = recordList;
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param source 読込処理
	 * @return 生成情報
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static ResultDataModel createData(ResultSet source) throws SQLException {
		var columnList = ColumnListModel.createData(source.getMetaData());
		var recordList = RecordListModel.createData(source);
		return new ResultDataModel(columnList, recordList);
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param parameter 接続引数
	 * @param selectSQL 抽出構文
	 * @return 生成情報
	 * @throws SQLException 抽出処理に失敗した場合
	 */
	public static ResultDataModel createData(String parameter, String selectSQL) throws SQLException {
		try (var connection = DriverManager.getConnection(parameter);
				var statement = connection.createStatement();
				var resultSet = statement.executeQuery(selectSQL)) {
			return createData(resultSet);
		}
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param parameter 接続引数
	 * @param selectSQL 抽出構文
	 * @return 生成配列
	 * @throws SQLException 抽出処理に失敗した場合
	 */
	static ResultDataModel[] createList(String parameter, String selectSQL) throws SQLException {
		try (var connection = DriverManager.getConnection(parameter);
				var statement = connection.createStatement()) {
			var result = new ArrayList<ResultDataModel>();
			var status = statement.execute(selectSQL);
			while (true) {
				if (status) {
					// 結果セットあり
					try (var resultSet = statement.getResultSet()) {
						result.add(createData(resultSet));
					}
				} else if (statement.getUpdateCount() == -1) {
					// 結果セットなし＋後続情報なし
					break;
				} else {
					// 結果セットなし＋後続情報あり
				}
				status = statement.getMoreResults();
			}
			return result.toArray(new ResultDataModel[result.size()]);
		}
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param source 読込処理
	 * @param action 実行処理
	 * @throws SQLException 読込処理に失敗した場合
	 */
	private static void invokeData(ResultSet source, ActionDataModel action) throws SQLException {
		var choose = source.getMetaData();
		ColumnListModel.invokeData(choose, action::updateColumnList);
		RecordDataModel.invokeList(source, action::registRecordData);
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param source 実行処理
	 * @param offset 実行番号
	 * @param status 結果種別
	 * @param action 生成処理
	 * @throws SQLException 読込処理に失敗した場合
	 */
	private static void invokeData(Statement source, int offset, boolean status, IntFunction<ActionDataModel> action) throws SQLException {
		if (status) {
			// 抽出処理の場合
			var choose = action.apply(offset);
			try (var result = source.getResultSet()) {
				invokeData(result, choose);
			}
			invokeData(source, offset + 1, source.getMoreResults(), action);
		} else if (source.getUpdateCount() != -1) {
			// 実行処理の場合
			var choose = action.apply(offset);
			choose.invokeUpdateSize(source.getUpdateCount());
			invokeData(source, offset + 1, source.getMoreResults(), action);
		} else {
			// 処理終了の場合
		}
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param source 実行処理
	 * @param offset 実行番号
	 * @param invoke 実行構文
	 * @param action 生成処理
	 */
	private static void invokeData(Statement source, int offset, String invoke, IntFunction<ActionDataModel> action) {
		try {
			invokeData(source, offset, source.execute(invoke), action);
		} catch (Exception errors) {
			var choose = action.apply(offset);
			choose.updateRecordList(errors);
		}
	}
	/**
	 * 結果情報モデルを生成します。
	 * 
	 * @param parameters 接続情報
	 * @param invokeText 実行情報
	 * @param actionHook 実行処理
	 * @throws SQLException 実行処理に失敗した場合
	 */
	public static void invokeList(String parameters, String invokeText, IntFunction<ActionDataModel> actionHook) throws SQLException {
		try (var connection = DriverManager.getConnection(parameters);
				var statement = connection.createStatement()) {
			invokeData(statement, 0, invokeText, actionHook);
		}
	}

	// ===================================================================
	// 継承メソッド定義
	// ===================================================================
	/**
	 * 当該情報を表現文字列へ変換します。
	 * 
	 * @return 表現文字列
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultDataModel[" + this.columnList + ", " + this.recordList + "]";
	}
}
