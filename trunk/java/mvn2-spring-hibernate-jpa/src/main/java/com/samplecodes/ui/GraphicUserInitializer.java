package com.samplecodes.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicUserInitializer extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public GraphicUserInitializer(String title, JPanel jp) {
		super(title);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		
		jp.setSize(this.getSize());
		jp.setBackground(new Color(255, 255, 255));
		
		this.add(jp);
		this.setVisible(true);
		
		WindowUtilities.setNativeLookAndFeel();
	}
}
