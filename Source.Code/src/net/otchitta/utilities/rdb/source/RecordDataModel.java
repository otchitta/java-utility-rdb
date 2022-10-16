/**
 * [Source.Code] RecordDataModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * 要素情報モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class RecordDataModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素配列 */
	private final Object[] values;

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
	public Object getData(int index) {
		return this.values[index];
	}

	// ===================================================================
	// 生成メソッド定義
	// ===================================================================
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param values 要素配列
	 */
	private RecordDataModel(Object[] values) {
		super();
		this.values = values;
	}
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param source 読込情報
	 * @param length 要素個数
	 * @return 生成情報
	 * @throws SQLException 読込処理に失敗した場合
	 */
	private static RecordDataModel createData(ResultSet source, int length) throws SQLException {
		var values = new Object[length];
		for (var index = 0; index < length; index ++) {
			var choose = source.getObject(index + 1);
			if (choose instanceof Timestamp) {
				values[index] = ((Timestamp)choose).toLocalDateTime();
			} else if (choose instanceof Date) {
				values[index] = ((Date)choose).toLocalDate();
			} else if (choose instanceof Time) {
				values[index] = ((Time)choose).toLocalTime();
			} else {
				values[index] = choose;
			}
		}
		return new RecordDataModel(values);
	}
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param source 読込処理
	 * @return 生成配列
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static RecordDataModel[] createList(ResultSet source) throws SQLException {
		var result = new ArrayList<RecordDataModel>();
		var choose = source.getMetaData();
		var length = choose.getColumnCount();
		while (source.next()) {
			result.add(createData(source, length));
		}
		return result.toArray(new RecordDataModel[result.size()]);
	}
	/**
	 * 要素情報モデルを生成します。
	 * 
	 * @param source 読込処理
	 * @param action 実行処理
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static void invokeList(ResultSet source, Consumer<RecordDataModel> action) throws SQLException {
		var choose = source.getMetaData();
		var length = choose.getColumnCount();
		while (source.next()) {
			action.accept(createData(source, length));
		}
	}
}
