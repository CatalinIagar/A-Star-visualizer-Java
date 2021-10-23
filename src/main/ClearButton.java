package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ClearButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2069077252501788230L;

	public ClearButton() {
		this.setText("Clear");
		this.setBackground(new Color(173, 216, 230));
		this.setForeground(Color.black);
		this.setFont(new Font("serif", Font.BOLD, 25));
		this.setFocusable(false);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Select = true");
				PathFinding.selectStart = false;
				PathFinding.selectTarget = false;
				PathFinding.clearMap = true;
			}
		});
	}
}
