package com.samplecodes.ui;

import com.samplecodes.base.Constants;
import com.samplecodes.dao.UserDao;
import com.samplecodes.model.Privilege;
import com.samplecodes.service.DriverService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUserInterface implements ActionListener, Constants {

    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{APPLICATION_CONTEXT_SOURCE});

    //@Resource
    private UserDao userDao = (UserDao) context.getBean("userDao");

	private JPanel graphicPanel;

	private JTextField userNameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JPanel loginForm;
	private JLabel message;

	private String page;

	public void update(String state) {
		this.page = state;
		update();
	}

	public void update() {
		graphicPanel.removeAll();
		passwordField.setText("");
		message.setText("");

		graphicPanel.add(loginForm);
		graphicPanel.add(loginButton);
		
		if(page.matches("invalid user/pass")){
			message.setText("ترکیب نام کاربری و گذرواژه اشتباه است.");
		}

		graphicPanel.validate();
		graphicPanel.repaint();
	}

	public LoginUserInterface(JPanel graphicPanel) {
		this.graphicPanel = graphicPanel;

		WindowUtilities.setNativeLookAndFeel();

		userNameField = new JTextField(15);
		userNameField.setActionCommand("login");
		JPanel f1 = new JPanel();
		f1.add(userNameField);
		f1.add(new JLabel("نام کاربری"));

		passwordField = new JPasswordField(15);
		passwordField.setActionCommand("login");
		JPanel f2 = new JPanel();
		f2.add(passwordField);
		f2.add(new JLabel("گذرواژه"));

		message = new JLabel();
		
		loginForm = new JPanel();
		loginForm.setPreferredSize(new Dimension(360, 360));
		loginForm.add(f1);
		loginForm.add(f2);
		loginForm.add(message);
		loginForm.setBackground(Color.WHITE);

		loginButton = new JButton("ورود");
		loginButton.setActionCommand("login");
		loginButton.addActionListener(this);

		this.update("main");
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().matches("login")) {
            String userName = userNameField.getText();
            String password = new String(passwordField.getPassword());
            for(Privilege privilege : userDao.getUserPrivilege(userName, password)) {
                switch (privilege) {
                    case ADMIN:
                        new AdminUserInterface(userName, password, graphicPanel, this);
                        break;
                    case CUSTOMER:
                        new CustomerUserInterface(userName, password, graphicPanel, this);
                        break;
                    case DRIVER:
                        new DriverUserInterface(userName, password, graphicPanel, this);
                        break;
                }
            }
		}
	}
}
