package net.otchitta.utilities.rdb.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * 要素画面エリアクラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
final class SourceViewFrame extends JLabel implements TableCellRenderer {
	// ===================================================================
	// メンバー定数定義
	// ===================================================================
	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;
	/** 日付書式 */
	private static final DateTimeFormatter FORMAT1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	/** 時刻書式 */
	private static final DateTimeFormatter FORMAT2 = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSS");
	/** 日時書式 */
	private static final DateTimeFormatter FORMAT3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS");
	/** 背景描画 */
	private static final Color BACKGROUND_NULL = new Color(192, 192, 192);

	// ===================================================================
	// 内部メソッド定義
	// ===================================================================
	/**
	 * 表示内容を設定します。
	 * 
	 * @param values 要素情報
	 */
	private void setText(Object values) {
		if (values == null) {
			setText("NULL");
		} else if (values instanceof Integer || values instanceof Long) {
			setText(String.format("%,d", values));
		} else if (values instanceof LocalDate) {
			setText(FORMAT1.format((LocalDate)values));
		} else if (values instanceof LocalTime) {
			setText(FORMAT2.format((LocalTime)values));
		} else if (values instanceof LocalDateTime) {
			setText(FORMAT3.format((LocalDateTime)values));
		} else {
			setText(values.toString());
		}
	}
	/**
	 * 文字情報を設定します。
	 * 
	 * @param source 文字情報
	 * @param values 要素情報
	 */
	private void setFont(Font source, Object values) {
		if (values == null) {
			setFont(source.deriveFont(Font.ITALIC));
		} else {
			setFont(source);
		}
	}
	/**
	 * 水平位置を設定します。
	 * 
	 * @param values 要素情報
	 */
	private void setHorizontalAlignment(Object values) {
		if (values == null) {
			setHorizontalAlignment(SwingConstants.CENTER);
		} else if (values instanceof Byte || values instanceof Short || values instanceof Integer || values instanceof Long) {
			setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			setHorizontalAlignment(SwingConstants.LEFT);
		}
	}
	/**
	 * 前景描画を設定します。
	 * 
	 * @param screen 画面情報
	 * @param values 要素情報
	 * @param select 選択状態
	 */
	private void setForeground(JTable screen, Object values, boolean select) {
		if (select) {
			setForeground(screen.getSelectionForeground());
		} else if (values == null) {
			setForeground(screen.getForeground());
		} else {
			setForeground(screen.getForeground());
		}
	}
	/**
	 * 背景描画を設定します。
	 * 
	 * @param screen 画面情報
	 * @param values 要素情報
	 * @param select 選択状態
	 */
	private void setBackground(JTable screen, Object values, boolean select) {
		if (select) {
			setBackground(screen.getSelectionBackground());
		} else if (values == null) {
			setBackground(BACKGROUND_NULL);
		} else {
			setBackground(screen.getBackground());
		}
	}
	/**
	 * 境界描画を設定します。
	 * 
	 * @param source 境界描画
	 */
	private void setBorder(Color source) {
		setBorder(BorderFactory.createLineBorder(source, 1));
	}
	/**
	 * 境界描画を設定します。
	 * 
	 * @param screen 画面情報
	 * @param values 要素情報
	 * @param select 選択状態
	 * @param active 焦点状態
	 */
	private void setBorder(JTable screen, Object values, boolean select, boolean active) {
		if (active) {
			setBorder(screen.getSelectionBackground());
		} else if (select) {
			setBorder(screen.getSelectionBackground());
		} else if (values == null) {
			setBorder(BACKGROUND_NULL);
		} else {
			setBorder(screen.getBackground());
		}
	}

	// ===================================================================
	// 実装メソッド定義
	// ===================================================================
	/**
	 * 描画情報を取得します。
	 * 
	 * @param screen 画面情報
	 * @param values 要素情報
	 * @param select 選択状態
	 * @param active 焦点状態
	 * @param record 要素番号
	 * @param column 要素番号
	 * @return 描画情報
	 */
	@Override
	public Component getTableCellRendererComponent(JTable screen, Object values, boolean select, boolean active, int record, int column) {
		if (screen == null) {
			setFont(null);
			setForeground(null);
			setBackground(null);
			setHorizontalAlignment(values);
			setBorder(BorderFactory.createEmptyBorder());
			setText(values);
		} else {
			setOpaque(true);
			setFont(screen.getFont(), values);
			setForeground(screen, values, select);
			setBackground(screen, values, select);
			setHorizontalAlignment(values);
			setBorder(screen, values, select, active);
			setText(values);
		}
		return this;
	}

	// ===================================================================
	// 継承メソッド定義
	// ===================================================================
	/**
	 * 描画準備を処理します。
	 */
	@Override
	public void invalidate() {
		// 処理抑制
	}
	/**
	 * 描画準備を開始します。
	 */
	@Override
	public void revalidate() {
		// 処理抑制
	}
	/**
	 * 描画準備を実行します。
	 */
	@Override
	public void validate() {
		// 処理抑制
	}
	/**
	 * 描画処理を実行します。
	 */
	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		// 処理抑制
	}
	/**
	 * 描画処理を実行します。
	 */
	@Override
	public void repaint(Rectangle r) {
		// 処理抑制
	}
	/**
	 * 描画処理を実行します。
	 */
	@Override
	public void repaint() {
		// 処理抑制
	}
	/**
	 * 要素変更を通知します。
	 * 
	 * @param memberName 要素名称
	 * @param olderValue 変更前値
	 * @param newerValue 変更後値
	 */
	protected void firePropertyChange(String memberName, Object olderValue, Object newerValue) {
		switch (memberName) {
		default:
			break;
		case "text":
		case "labelFor":
		case "displayedMnemonic":
		case "font":
		case "foreground":
		case "background":
			if (olderValue == newerValue) {
				// 処理なし
			} else {
				super.firePropertyChange(memberName, olderValue, newerValue);
			}
		}
	}
	/**
	 * 要素変更を通知します。
	 * 
	 * @param memberName 要素名称
	 * @param olderValue 変更前値
	 * @param newerValue 変更後値
	 */
	@Override
	public void firePropertyChange(String memberName, boolean oldValue, boolean newValue) {
		// 処理なし
	}
}
