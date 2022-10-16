/**
 * [Source.Code] RecordListModel.java
 */
package net.otchitta.utilities.rdb.source;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 要素一覧モデルクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class RecordListModel implements Serializable {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	// ===================================================================
	// メンバー変数定義
	// ===================================================================
	/** 要素配列 */
	private final RecordDataModel[] values;

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
	public RecordDataModel getData(int index) {
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
	private RecordListModel(RecordDataModel[] values) {
		super();
		this.values = values;
	}
	/**
	 * 要素一覧モデルを生成します。
	 * 
	 * @param source 読込処理
	 * @return 生成情報
	 * @throws SQLException 読込処理に失敗した場合
	 */
	static RecordListModel createData(ResultSet source) throws SQLException {
		var values = RecordDataModel.createList(source);
		return new RecordListModel(values);
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
		return String.format("recordSize=%,7d", Integer.valueOf(this.values.length));
	}
}
