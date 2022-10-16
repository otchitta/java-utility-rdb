/**
 * [Source.Code] ColumnListModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * 要素一覧モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class ColumnListModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素配列 */
	private final ColumnDataModel[] values;

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
	public ColumnDataModel getData(int index) {
		return this.values[index];
	}

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 要素一覧モデルを生成します。
	 * 
	 * @param values 要素配列
	 */
	private ColumnListModel(ColumnDataModel[] values) {
		super();
		this.values = values;
	}
	/**
	 * 要素一覧モデルを生成します。
	 * 
	 * @param source 読込情報
	 * @return 生成情報
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static ColumnListModel createData(ResultSetMetaData source) throws SQLException {
		var values = ColumnDataModel.createList(source);
		return new ColumnListModel(values);
	}
	/**
	 * 要素一覧モデルを生成します。
	 * 
	 * @param source 読込情報
	 * @param action 実行処理
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static void invokeData(ResultSetMetaData source, Consumer<ColumnListModel> action) throws SQLException {
		action.accept(createData(source));
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
		return String.format("columnSize=%,3d", Integer.valueOf(this.values.length));
	}
}
