package com.samplecodes.test;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.dao.EmployeeDao;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Company;
import com.samplecodes.model.Customer;
import com.samplecodes.model.Employee;
import com.samplecodes.service.AdminService;
import com.samplecodes.service.CustomerService;
import com.samplecodes.service.DriverService;
import com.samplecodes.ui.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.util.logging.Logger;


public class Main {

    static final Logger logger = Logger.getLogger(Main.class.getName());
    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

    public static void main(final String args[]) throws Exception {
         AdminServiceTest();
         uiTest();
         logger.info("Done");
    }

    private static void CustomerServiceTest() {
        CustomerService customerService = (CustomerService) context.getBean("customerService");
        Customer customer = customerService.refershCustomer("hossein", "1289");
        Cargo cargo = new Cargo();
        cargo = customerService.addOrder(customer, cargo);
        // This part id pretty important
        customer = cargo.getCustomer();
        customerService.deleteCargo(customer, cargo);
    }

    private static void DriverServiceTest() {
        DriverService driverService = (DriverService) context.getBean("driverService");
    }

    private static void AdminServiceTest() {
        AdminService adminService = (AdminService) context.getBean("adminService");
        adminService.refershAdmin("hossein", "123");
        Cargo cargo = new Cargo();
        cargo = adminService.assignShipments(cargo, 5);
    }

    private static Company CompanyTest() {
        CompanyDao companyDao = (CompanyDao) context.getBean("companyDao");
        EmployeeDao employeeDao = (EmployeeDao) context.getBean("employeeDao");

        Company company = new Company();
        company.setName("in20seconds");

        Employee employee = new Employee();
        employee.setName("hossein");
        employee.setAge(22);
        employee.setCompany(company);

        employeeDao.merge(employee);
        return company;
    }

    private static void uiTest() {
//        final JPanel graphicPanel4Admin = new JPanel();
//        final JPanel graphicPanel4Customer = new JPanel();
//        final JPanel graphicPanel4Driver = new JPanel();
        final JPanel graphicPanel4Login = new JPanel();

//        final String title4Admin = "User: admin";
//        final String title4Customer = "User: mohsen";
//        final String title4Driver = "User: gholam";
        final String title4Login = "Login";

//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new GraphicUserInitializer(title4Admin, graphicPanel4Admin);
//            }
//        });
//
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new GraphicUserInitializer(title4Customer, graphicPanel4Customer);
//            }
//        });
//
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new GraphicUserInitializer(title4Driver, graphicPanel4Driver);
//            }
//        });

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GraphicUserInitializer(title4Login, graphicPanel4Login);
			}
		});

        LoginUserInterface loginUserInterface = new LoginUserInterface(graphicPanel4Login);
        //new AdminUserInterface("admin", graphicPanel4Admin, loginUserInterface);
        //new CustomerUserInterface("mohsen", graphicPanel4Customer, loginUserInterface);
        //new DriverUserInterface("gholam", graphicPanel4Driver, loginUserInterface);


//        for (int i = 0; i < args.length; ++i) {
//
//            final String title = "User: " + args[i];
//            panels[i] = graphicPanel;
//
//            User user = commonService.getUser(args[0]);
//
//            ActionListener userInterface = null;
//
//            System.err.println(user.getUsername());
//
//            switch (user.getPrivilege()) {
//
//            case User.CUSTOMER:
////				userInterface = new CustomerInterface((Customer)(user), graphicPanel);
//                System.err.println("CUSTOMER");
//                break;
//
//            case User.ADMIN:
//                userInterface = new AdminUserInterface((Admin)(user), graphicPanel);
//                System.err.println("ADMIN");
//                break;
//
//
//            case User.DRIVER:
//                System.err.println("DRIVER");
//
//            default:
//                break;
//            }
//
//            final ActionListener listener = userInterface;
//
//            javax.swing.SwingUtilities.invokeLater(new Runnable() {
//                public void run() {
//                    new GraphicUserInitializer(title, graphicPanel);
//                }
//            });
//        }
//
//        if(panels[0] == panels[1]){
//            System.err.println("equal");
//        }else{
//            System.err.println("neq");
//        }
//
//        if(panels[0] == null || panels[1] == null){
//            System.err.println("null");
//        }

    }


}
