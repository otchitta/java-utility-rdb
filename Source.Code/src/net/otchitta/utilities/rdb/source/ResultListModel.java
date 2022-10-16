/**
 * [Source.Code] ResultListModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * 結果一覧モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class ResultListModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素配列 */
	private final ResultDataModel[] values;

	// ===================================================================
	// プロパティー定義
	// ===================================================================
	/**
	 * 要素個数を取得します。
	 * 
	 * @return 要素個数
	 */
	public int getSize() {
		return this.values.length;
	}
	/**
	 * 要素情報を取得します。
	 * 
	 * @param index 要素番号
	 * @return 要素情報
	 */
	public ResultDataModel getData(int index) {
		return this.values[index];
	}

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 結果一覧モデルを生成します。
	 * 
	 * @param values 要素配列
	 */
	private ResultListModel(ResultDataModel[] values) {
		super();
		this.values = values;
	}
	/**
	 * 結果一覧モデルを生成します。
	 * 
	 * @param parameter 接続引数
	 * @param selectSQL 抽出構文
	 * @return 生成情報
	 * @throws SQLException 抽出処理に失敗した場合
	 */
	public static ResultListModel createData(String parameter, String selectSQL) throws SQLException {
		var values = ResultDataModel.createList(parameter, selectSQL);
		return new ResultListModel(values);
	}
}
