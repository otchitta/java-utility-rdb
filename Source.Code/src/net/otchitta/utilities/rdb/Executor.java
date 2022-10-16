/**
 * [Source.Code] Executor.java
 */
package net.otchitta.utilities.rdb;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;

import net.otchitta.utilities.rdb.screen.ResultViewFrame;

/**
 * 実行処理クラスです。
 * 
 * @since   1.0.0
 * @version 1.0.0
 * @author  o.chikami
 */
public final class Executor {
	// ===================================================================
	// 内部メソッド定義
	// ===================================================================
	/**
	 * 入力領域を生成します。
	 * 
	 * @param action 実行処理
	 * @param finish 終了処理
	 * @return 入力領域
	 */
	private static JComponent createSourceView(BiConsumer<String, String> action, Consumer<ChangeListener> finish) {
		// 内部変数設定
		var value1 = new JTextField("jdbc:sqlserver://localhost; databaseName=xxx; user=yyy; password=zzz;");
		var value2 = new JTextArea("SELECT * FROM import_data");
		var value3 = new JScrollPane(value2);
		var button = new JButton("実行");
		var result = new JPanel(new BorderLayout());
		// 画面情報設定
		// 画面体裁設定
		result.add(value1, BorderLayout.NORTH);
		result.add(value3, BorderLayout.CENTER);
		result.add(button, BorderLayout.EAST);
		// イベント設定
		button.addActionListener(event -> {
			button.setEnabled(false);
			action.accept(value1.getText(), value2.getText());
		});
		finish.accept(event -> button.setEnabled(true));
		return result;
	}
	/**
	 * 全体領域を生成します。
	 * 
	 * @return 全体領域
	 */
	private static JComponent createScreenView() {
		var source = new ResultViewFrame();
		var value1 = createSourceView(source::invokeText, source::registHook);
		var value2 = source.chooseView();
		var result = new JSplitPane(JSplitPane.VERTICAL_SPLIT, value1, value2);
		result.setContinuousLayout(true);
		return result;
	}
	/**
	 * 文字体裁を設定します。
	 * 
	 * @param values 文字体裁
	 */
	private static void updateScreenFont(Font values) {
		var source = UIManager.getDefaults();
		for (var choose : source.entrySet()) {
			var cache1 = choose.getKey();
			if (String.valueOf(cache1).toLowerCase(Locale.ENGLISH).endsWith("font")) {
				choose.setValue(values);
			}
		}
	}

	// ===================================================================
	// 実行メソッド定義
	// ===================================================================
	/**
	 * 実行処理を実行します。
	 * 
	 * @param commands コマンドライン引数
	 */
	public static void main(String[] commands) {
		updateScreenFont(new Font("MS Gothic", Font.PLAIN, 12));
		var window = new JFrame("データベースツール");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.add(createScreenView());
		window.pack();
		window.setSize(800, 600);
		window.setVisible(true);
	}
}
