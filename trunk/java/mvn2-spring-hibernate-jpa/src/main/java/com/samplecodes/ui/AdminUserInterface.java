package com.samplecodes.ui;

import com.samplecodes.model.Admin;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Driver;
import com.samplecodes.model.Shipment;
import com.samplecodes.service.AdminService;
import com.samplecodes.service.DriverService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminUserInterface implements ActionListener {

    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

    //@Resource
    private AdminService adminService = (AdminService) context.getBean("adminService");;
    //@Resource
    private DriverService driverService = (DriverService) context.getBean("driverService");

    private Admin admin;

	private JPanel graphicPanel;
	private LoginUserInterface loginUserInterface;

	JLabel title;

	JButton viewCargoButton, viewDriversButton, mainMenuButton, refreshButton, logoutButton,
			cargoDetailsButton, assignDriverButton, assignShipmentsButton;

	JComboBox driverComboBox;

	JTable cargoTable, shipmentTable;
	private static String[] cargoTableColumnTitles = { "فرستنده", "تاریخ",
			"مبدأ", "مقصد", "وضعیت" };
	private static String[] shipmentTableColumnTitles = { "راننده", "وضعیت",
			"تاریخ وصول" };
	List<Cargo> cargoList;

	JSpinner spinner;

	private Cargo chosenCargo;

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

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setPreferredSize(new Dimension(600, 10));
		graphicPanel.add(separator);

		if (page.matches("main")) {
			graphicPanel.add(viewCargoButton);
			graphicPanel.add(viewDriversButton);
		} else if (page.matches("viewcargo")) {
			cargoList = adminService.listCargos();

			Object[][] tableContent = new Object[cargoList.size()][cargoTableColumnTitles.length];
			for (int i = 0; i < cargoList.size(); ++i) {
				tableContent[i][0] = cargoList.get(i).getCustomer().getUsername();
				tableContent[i][1] = cargoList.get(i).getOrderDate();
				tableContent[i][2] = cargoList.get(i).getOrigin();
				tableContent[i][3] = cargoList.get(i).getDestination();
				if (cargoList.get(i).getShipmentList() == null) {
					tableContent[i][4] = "بلاتکلیف";
				} else if (cargoList.get(i).delivered()) {
					tableContent[i][4] = "رسید";
				} else {
					boolean hasDriver = true;
					for (int j = 0; j < cargoList.get(i).getShipmentList().size(); ++j) {
						hasDriver = hasDriver
								&& cargoList.get(i).getShipmentList().get(j)
										.hasDriver();
					}
					if (hasDriver) {
						tableContent[i][4] = "در راه";
					} else {
						tableContent[i][4] = "بلاتکلیف";
					}
				}
			}

			cargoTable = new JTable(tableContent, cargoTableColumnTitles);
			cargoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			JScrollPane tablePane = new JScrollPane(cargoTable);
			tablePane.setPreferredSize(new Dimension(550, 250));
			graphicPanel.add(tablePane);
			graphicPanel.add(cargoDetailsButton);
		} else if (page.matches("cargodetails")) {
			if (chosenCargo == null) {
				update("main");
			}
			if (chosenCargo.getShipmentList() == null) {
				graphicPanel.add(new JTextArea("تعداد محموله‌ها را مشخص کنید"));
				graphicPanel.add(spinner);
				graphicPanel.add(assignShipmentsButton);
			} else {

				Object[][] tableContent = new Object[chosenCargo.getShipmentList()
						.size()][shipmentTableColumnTitles.length];
				for (int i = 0; i < chosenCargo.getShipmentList().size(); ++i) {
					Shipment shipment = chosenCargo.getShipmentList().get(i);
					if (shipment.hasDriver()) {
						tableContent[i][0] = shipment.getDriver().getUsername();
					} else {
						tableContent[i][0] = "-";
					}

					if (shipment.getState() == Shipment.ON_THE_WAY) {
						tableContent[i][1] = "در راه";
					} else if (shipment.getState() == Shipment.DELIVERED) {
						tableContent[i][1] = "رسید";
					} else {
						tableContent[i][1] = "آماده";
					}

					tableContent[i][2] = shipment.getDeliveryDate();
				}

				shipmentTable = new JTable(tableContent,
						shipmentTableColumnTitles);
				shipmentTable
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				JScrollPane tablePane = new JScrollPane(shipmentTable);
				tablePane.setPreferredSize(new Dimension(550, 250));
				graphicPanel.add(tablePane);

				driverComboBox = new JComboBox(driverService.listDrivers().toArray());
				graphicPanel.add(driverComboBox);

				graphicPanel.add(assignDriverButton);
			}
		}

		graphicPanel.validate();
		graphicPanel.repaint();
	}

	public AdminUserInterface(String userName, String password, JPanel graphicPanel, LoginUserInterface loginUserInterface) {
        this.admin = (Admin)adminService.login(userName, password);
		this.graphicPanel = graphicPanel;
		this.loginUserInterface = loginUserInterface;

		WindowUtilities.setNativeLookAndFeel();

		viewCargoButton = new JButton("مشاهده‌ی بارنامه‌ها", new ImageIcon(
				"img/viewcargo.png"));
		viewCargoButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		viewCargoButton.setHorizontalTextPosition(AbstractButton.CENTER);
		viewCargoButton.setActionCommand("viewcargo");
		viewCargoButton.addActionListener(this);

		viewDriversButton = new JButton("مشاهده‌ی راننده‌ها", new ImageIcon(
				"img/viewdrivers.png"));
		viewDriversButton.setVerticalTextPosition(AbstractButton.BOTTOM);
		viewDriversButton.setHorizontalTextPosition(AbstractButton.CENTER);
		viewDriversButton.setActionCommand("update");
		viewDriversButton.addActionListener(this);

		mainMenuButton = new JButton(new ImageIcon("img/home.png"));
		mainMenuButton.setActionCommand("mainmenu");
		mainMenuButton.addActionListener(this);

		refreshButton = new JButton(new ImageIcon("img/refresh.png"));
		refreshButton.setActionCommand("update");
		refreshButton.addActionListener(this);
		
		logoutButton = new JButton(new ImageIcon("img/logout.png"));
		logoutButton.setActionCommand("logout");
		logoutButton.addActionListener(this);

		title = new JLabel("در سیستم به نام " + admin.getUsername());
		title.setFont(new Font("sansserif", Font.PLAIN, 24));
		title.setForeground(new Color(0.85f, 0.0f, 0.0f));

		cargoDetailsButton = new JButton("انتخاب بارنامه");
		cargoDetailsButton.setActionCommand("chosecargo");
		cargoDetailsButton.addActionListener(this);

		assignDriverButton = new JButton("تخصیص راننده");
		assignDriverButton.setActionCommand("assignDriver");
		assignDriverButton.addActionListener(this);

		assignShipmentsButton = new JButton("تأیید");
		assignShipmentsButton.setActionCommand("assignShipments");
		assignShipmentsButton.addActionListener(this);

		spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

		this.update("main");
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().matches("viewcargo")) {
			update("viewcargo");
		} else if (event.getActionCommand().matches("mainmenu")) {
			update("main");
		} else if (event.getActionCommand().matches("logout")) {
			loginUserInterface.update("main");
			graphicPanel = null;
		} else if (event.getActionCommand().matches("assignShipments")) {
			adminService.assignShipments(chosenCargo, ((Integer) spinner.getValue())
					.intValue());
			update();
		} else if (event.getActionCommand().matches("chosecargo")) {
			if (cargoTable.getSelectedRowCount() != 0) {
				chosenCargo = cargoList.get(cargoTable.getSelectedRow());
				update("cargodetails");
			}
		} else if (event.getActionCommand().matches("assignDriver")) {
			if (shipmentTable.getSelectedRowCount() != 0) {
				driverService.assignDriver((Driver) driverComboBox.getSelectedItem(), chosenCargo.getShipmentList().get(
						shipmentTable.getSelectedRow()));
				update("cargodetails");
			}
		} else if (event.getActionCommand().matches("update")) {
			update();
		}
	}
}
