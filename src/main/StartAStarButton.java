package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class StartAStarButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -270280279259093606L;

	public StartAStarButton() {
		this.setText("A* Algorithm");
		this.setBackground(new Color(173, 216, 230));
		this.setForeground(Color.black);
		this.setFont(new Font("serif", Font.BOLD, 25));
		this.setFocusable(false);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("Select = true");
				PathFinding.setStartAStar(true);
				;
			}
		});
	}
}
