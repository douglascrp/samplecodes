package com.samplecodes.ui;

import com.samplecodes.model.Driver;
import com.samplecodes.model.Location;
import com.samplecodes.model.Shipment;
import com.samplecodes.service.DriverService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DriverUserInterface implements ActionListener {

    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

    //@Resource
    private DriverService driverService = (DriverService) context.getBean("driverService");

    private JPanel graphicPanel;
    private LoginUserInterface loginUserInterface;
    private Driver driver;


    JComboBox locationComboBox;

    String page;
    JButton mainMenuButton, refreshButton, logoutButton;
    JLabel title;
    JTable shipmentTable;

    JButton reportLocationButton, loadShipmentButton, reportButton,
            pickupButton;

    private static String[] shipmentTableColumnTitles = {"محتویات",
            "وزن حدودی", "موعد تحویل", "مقصد"};

    ArrayList<Shipment> shipmentList;

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

        if (page.matches("main")) {
            graphicPanel.add(reportLocationButton);
            graphicPanel.add(loadShipmentButton);
        } else if (page.matches("reportloc")) {
            locationComboBox = new JComboBox(driver.getLocation()
                    .getNeighbours().toArray());
            graphicPanel.add(locationComboBox);
            graphicPanel.add(reportButton);
        } else if (page.matches("loadShipment")) {
            shipmentList = new ArrayList<Shipment>();

            for (int i = 0; i < driverService.getAssignedShipments().size(); ++i) {
                if (driverService.canPickup(driver, driverService.getAssignedShipments().get(i))) {
                    shipmentList.add(driverService.getAssignedShipments().get(i));
                }
            }

            Object[][] tableContent = new Object[shipmentList.size()][shipmentTableColumnTitles.length];
            for (int i = 0; i < shipmentList.size(); ++i) {
                tableContent[i][0] = shipmentList.get(i).getType();
                tableContent[i][1] = shipmentList.get(i).getApproximateWeight();
                tableContent[i][2] = shipmentList.get(i).getDueDate();
                tableContent[i][3] = shipmentList.get(i).getDestination();
            }

            shipmentTable = new JTable(tableContent, shipmentTableColumnTitles);
            shipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane tablePane = new JScrollPane(shipmentTable);
            tablePane.setPreferredSize(new Dimension(550, 250));
            graphicPanel.add(tablePane);
            graphicPanel.add(pickupButton);
        }

        graphicPanel.validate();
        graphicPanel.repaint();
    }

    public DriverUserInterface(String userName, String password, JPanel graphicPanel, LoginUserInterface loginUserInterface) {
        this.driver = (Driver)driverService.login(userName, password);
        this.graphicPanel = graphicPanel;
        this.loginUserInterface = loginUserInterface;

        WindowUtilities.setNativeLookAndFeel();

        reportLocationButton = new JButton("اعلام موقعیت", new ImageIcon(
                "img/reportloc.png"));
        reportLocationButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        reportLocationButton.setHorizontalTextPosition(AbstractButton.CENTER);
        reportLocationButton.setActionCommand("reportloc");
        reportLocationButton.addActionListener(this);

        loadShipmentButton = new JButton("بارگذاری", new ImageIcon(
                "img/load.png"));
        loadShipmentButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        loadShipmentButton.setHorizontalTextPosition(AbstractButton.CENTER);
        loadShipmentButton.setActionCommand("loadShipment");
        loadShipmentButton.addActionListener(this);

        mainMenuButton = new JButton(new ImageIcon("img/home.png"));
        mainMenuButton.setActionCommand("mainmenu");
        mainMenuButton.addActionListener(this);

        refreshButton = new JButton(new ImageIcon("img/refresh.png"));
        refreshButton.setActionCommand("update");
        refreshButton.addActionListener(this);

        logoutButton = new JButton(new ImageIcon("img/logout.png"));
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this);

        reportButton = new JButton("اعلام");
        reportButton.setActionCommand("report");
        reportButton.addActionListener(this);

        pickupButton = new JButton("بارگذاری");
        pickupButton.setActionCommand("pickup");
        pickupButton.addActionListener(this);

        title = new JLabel("در سیستم به نام " + driver.getUsername());
        title.setFont(new Font("sansserif", Font.PLAIN, 24));
        title.setForeground(new Color(0.85f, 0.0f, 0.0f));

        this.update("main");
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().matches("mainmenu")) {
            update("main");
        } else if (event.getActionCommand().matches("loadShipment")) {
            update("loadShipment");
        } else if (event.getActionCommand().matches("pickup")) {
            if (shipmentTable.getSelectedRowCount() != 0) {
                driverService.pickupShipment(driver, shipmentList.get(
                        shipmentTable.getSelectedRow()));
                update("main");
            }
            update();
        } else if (event.getActionCommand().matches("reportloc")) {
            update("reportloc");
        } else if (event.getActionCommand().matches("report")) {
            driverService.reportLocation(driver, (Location) locationComboBox
                    .getSelectedItem());
            update("main");
        } else if (event.getActionCommand().matches("logout")) {
            loginUserInterface.update("main");
            graphicPanel = null;
        } else if (event.getActionCommand().matches("update")) {
            update();
        }
    }
}
