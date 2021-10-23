package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SelectButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7982316913496613222L;

	public SelectButton() {
		this.setText("Select START");
		this.setBackground(new Color(173, 216, 230));
		this.setForeground(Color.black);
		this.setFont(new Font("serif", Font.BOLD, 25));
		this.setFocusable(false);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Select = true");
				PathFinding.setSelect(true);
			}
		});
	}
}
