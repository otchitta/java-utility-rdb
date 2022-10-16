/**
 * [Source.Code] ColumnDataModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 要素情報モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class ColumnDataModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素種別 */
	private final String code;
	/** 要素名称 */
	private final String name;
	/** 要素桁数 */
	private final int size;

	// ===================================================================
	// プロパティー定義
	// ===================================================================
	/**
	 * 要素種別を取得します。
	 * 
	 * @return 要素種別
	 */
	public String getCode() {
		return this.code;
	}
	/**
	 * 要素名称を取得します。
	 * 
	 * @return 要素名称
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * 要素桁数を取得します。
	 * 
	 * @return 要素桁数
	 */
	public int getSize() {
		return this.size;
	}

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param code 要素種別
	 * @param name 要素名称
	 * @param size 要素桁数
	 */
	private ColumnDataModel(String code, String name, int size) {
		super();
		this.code = code;
		this.name = name;
		this.size = size;
	}
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param source 読込情報
	 * @return 生成配列
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static ColumnDataModel[] createList(ResultSetMetaData source) throws SQLException {
		var length = source.getColumnCount();
		var result = new ColumnDataModel[length];
		for (var index = 0; index < length; index ++) {
			var value1 = source.getColumnTypeName(index + 1);
			var value2 = source.getColumnName(index + 1);
			var value3 = source.getColumnDisplaySize(index + 1);
			result[index] = new ColumnDataModel(value1, value2, value3);
		}
		return result;
	}
}
