package com.samplecodes.ui;

import com.samplecodes.base.Constants;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.City;
import com.samplecodes.model.Customer;
import com.samplecodes.service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerUserInterface implements ActionListener, Constants {

    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{APPLICATION_CONTEXT_SOURCE});

    //@Resource
    private CustomerService customerService = (CustomerService) context.getBean("customerService");;

    private JPanel graphicPanel;
    private LoginUserInterface loginUserInterface;
    private Customer customer;

    JLabel title;

    JComboBox jcb;
    JTextField jtf;
    JComboBox cargoCount;

    JPanel newCargoForm;
    JTextField newCargoDescription, newCargoWeight;
    JComboBox newCargoOrigin, newCargoDestination;
    JTextField newCargoDueDate;
    JButton makeOrderButton;
    private static SimpleDateFormat formatter = new SimpleDateFormat(
            "dd/mm/yyyy");

    JButton mainMenuButton, refreshButton, logoutButton, newCargoButton;

    String page;

    public void update(String state) {
        this.page = state;
        update();
    }

    public void update() {
        graphicPanel.removeAll();
        graphicPanel.add(title);
        graphicPanel.add(mainMenuButton);
        graphicPanel.add(refreshButton);
        graphicPanel.add(logoutButton);
        graphicPanel.setLayout(new FlowLayout());

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setPreferredSize(new Dimension(600, 10));
        graphicPanel.add(separator);

        newCargoWeight.setBorder(new JTextField().getBorder());
        newCargoDueDate.setBorder(new JTextField().getBorder());

        if (page.matches("main")) {
            graphicPanel.add(newCargoButton);
        } else if (page.matches("newCargo")) {
            graphicPanel.add(newCargoForm);
        }

        graphicPanel.validate();
        graphicPanel.repaint();
    }

    public CustomerUserInterface(String userName, String password, JPanel graphicPanel, LoginUserInterface loginUserInterface) {
        this.customer = (Customer)customerService.login(userName, password);

        this.graphicPanel = graphicPanel;
        this.loginUserInterface = loginUserInterface;

        WindowUtilities.setNativeLookAndFeel();

        newCargoButton = new JButton("ایجاد بارنامه جدید", new ImageIcon(
                "img/newcargo.png"));
        newCargoButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newCargoButton.setHorizontalTextPosition(AbstractButton.CENTER);
        newCargoButton.setActionCommand("newCargo");
        newCargoButton.addActionListener(this);

        newCargoDescription = new JTextField(15);
        JPanel f1 = new JPanel();
        f1.add(newCargoDescription);
        f1.add(new JLabel("محتویات"));

        newCargoOrigin = new JComboBox(customerService.listCities().toArray());
        JPanel f2 = new JPanel();
        f2.add(newCargoOrigin);
        f2.add(new JLabel("مبدأ"));

        newCargoDestination = new JComboBox(customerService.listCities().toArray());
        JPanel f3 = new JPanel();
        f3.add(newCargoDestination);
        f3.add(new JLabel("مقصد"));

        newCargoWeight = new JTextField(7);
        JPanel f4 = new JPanel();
        f4.add(newCargoWeight);
        f4.add(new JLabel("وزن به گرم"));

        newCargoDueDate = new JTextField("dd/mm/yyyy");
        newCargoDueDate.setColumns(10);
        JPanel f5 = new JPanel();
        f5.add(newCargoDueDate);
        f5.add(new JLabel("موعد تحویل"));

        makeOrderButton = new JButton("سفارش");
        makeOrderButton.setActionCommand("order");
        makeOrderButton.addActionListener(this);

        mainMenuButton = new JButton(new ImageIcon("img/home.png"));
        mainMenuButton.setActionCommand("mainmenu");
        mainMenuButton.addActionListener(this);

        refreshButton = new JButton(new ImageIcon("img/refresh.png"));
        refreshButton.setActionCommand("update");
        refreshButton.addActionListener(this);

        logoutButton = new JButton(new ImageIcon("img/logout.png"));
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this);

        newCargoForm = new JPanel();
        newCargoForm.setPreferredSize(new Dimension(360, 360));
        newCargoForm.add(f1);
        newCargoForm.add(f3);
        newCargoForm.add(f2);
        newCargoForm.add(f5);
        newCargoForm.add(f4);
        newCargoForm.add(makeOrderButton);
        newCargoForm.setBackground(Color.WHITE);


        title = new JLabel("در سیستم به نام " + customer.getUsername());
        title.setFont(new Font("sansserif", Font.PLAIN, 24));
        title.setForeground(new Color(0.85f, 0.0f, 0.0f));

        this.update("main");
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().matches("newCargo")) {
            update("newCargo");
        } else if (event.getActionCommand().matches("order")) {
            try {

                customerService.addOrder(customer, new Cargo(newCargoDescription.getText(),
                        new Date(), formatter.parse(newCargoDueDate.getText()),
                        (City) newCargoOrigin.getSelectedItem(),
                        (City) newCargoDestination.getSelectedItem(), Integer
                        .parseInt(newCargoWeight.getText())));
                update("main");

            } catch (NumberFormatException e) {
                update();
                newCargoWeight.setBorder(new LineBorder(Color.RED, 2, true));
            } catch (ParseException e) {
                update();
                newCargoDueDate.setBorder(new LineBorder(Color.RED, 2, true));
            } catch (InstantiationException e) {
                update();
                newCargoDescription.setBorder(new LineBorder(Color.RED, 2, true));
            }
        } else if (event.getActionCommand().matches("mainmenu")) {
            update("main");
        } else if (event.getActionCommand().matches("logout")) {
            loginUserInterface.update("main");
            graphicPanel = null;
        } else if (event.getActionCommand().matches("update")) {
            update();
        }
    }
}
