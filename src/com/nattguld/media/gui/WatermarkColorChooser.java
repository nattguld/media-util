package com.nattguld.media.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import com.nattguld.media.watermarking.WatermarkColor;

/**
 * 
 * @author randqm
 *
 */

@SuppressWarnings("serial")
public abstract class WatermarkColorChooser extends JFrame {

	/**
	 * The content pane.
	 */
	private JPanel contentPane;
	
	/**
	 * The color chooser.
	 */
	private JColorChooser chooser;
	
	/**
	 * The selected color.
	 */
	private Color selectedColor;
	

	/**
	 * Create the frame.
	 * 
	 * @param parent The parent frame.
	 */
	public WatermarkColorChooser(JFrame parent) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				onClose();
			}
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(parent);
		
		chooser = new JColorChooser();
		AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
		
		for (AbstractColorChooserPanel accp : oldPanels) {
			chooser.removeChooserPanel(accp);
        }
		chooser.addChooserPanel(new AbstractColorChooserPanel() {
			@Override
			protected void buildChooser() {
				setTitle(getDisplayName());
				getContentPane().setLayout(new GridLayout(0, 10));
				
				for (WatermarkColor wmColor : WatermarkColor.values()) {
					JButton button = new JButton(wmColor.getName());
					button.setBackground(wmColor.getColor());
					button.setAction(new AbstractAction() {
						public void actionPerformed(ActionEvent evt) {
							JButton button = (JButton) evt.getSource();
							getColorSelectionModel().setSelectedColor(button.getBackground());
						}
					});
					getContentPane().add(button);
				}
			}

			@Override
			public String getDisplayName() {
				return "Color Chooser";
			}

			@Override
			public Icon getLargeDisplayIcon() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Icon getSmallDisplayIcon() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void updateChooser() {
				// TODO Auto-generated method stub
				
			}
		});
		getContentPane().add(chooser);
	}
	
	/**
	 * Actions performed when the window is closed.
	 */
	protected abstract void onClose();
	
	/**
	 * Retrieves the selected color.
	 * 
	 * @return The selected color.
	 */
	public Color getSelectedColor() {
		return selectedColor;
	}

}
